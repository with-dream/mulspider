package com.example.core.download.httppool;

import com.example.core.models.Request;
import com.example.core.utils.StrUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientPool {
    private static class SingletonClassInstance {
        private static final HttpClientPool instance = new HttpClientPool();
    }

    private HttpClientPool() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    public static HttpClientPool getInstance() {
        return SingletonClassInstance.instance;
    }

    private final Map<String, HttpClientModel> httpClients = new HashMap<>();

    private PoolingHttpClientConnectionManager connectionManager;

    public HttpClientModel getHttpClient(Request request) {
        HttpClientModel httpClient = httpClients.get(request.getSite());
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(request.getSite());
                if (httpClient == null) {
                    httpClient = createClient(request);
                    httpClients.put(request.getSite(), httpClient);
                }
            }
        }

        httpClient.cookieStore.clear();
        return httpClient;
    }

    private HttpClientModel createClient(Request request) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.setUserAgent(StringUtils.isEmpty(request.userAgent) ? "" : request.userAgent);
        //解决post/redirect/post 302跳转问题
        httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(request.timeOut);
        SocketConfig socketConfig = socketConfigBuilder.build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(request.retryCount, true));

        HttpClientModel model = new HttpClientModel();
        model.cookieStore = generateCookie(httpClientBuilder, request);
        model.client = httpClientBuilder.build();
        return model;
    }

    private CookieStore generateCookie(HttpClientBuilder httpClientBuilder, Request request) {
        CookieStore cookieStore = new BasicCookieStore();
        httpClientBuilder.setDefaultCookieStore(cookieStore);
        return cookieStore;
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            SSLContext sslContext = createIgnoreVerifySSL();
            String[] supportedProtocols;
            if (SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_11)) {
                supportedProtocols = new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"};
            } else {
                supportedProtocols = new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"};
            }
//            D.d("supportedProtocols: {}", String.join(", ", supportedProtocols));
            return new SSLConnectionSocketFactory(sslContext, supportedProtocols,
                    null,
                    new DefaultHostnameVerifier()); // 优先绕过安全证书
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            System.err.println("ssl connection fail" + e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public HttpClientContext getHttpClientContext(Request request) {
        HttpClientContext httpContext = new HttpClientContext();
        if (!StrUtils.isEmpty(request.proxyUrl)) {
            AuthState authState = new AuthState();
            authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(request.proxyUserName, request.proxyPassword));
            httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
        }
        if (request.cookie != null && !request.cookie.isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            for (Map.Entry<String, String> cookieEntry : request.cookie.entrySet()) {
                BasicClientCookie cookie1 = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                String domain = request.getSite();
                int portIndex = domain.indexOf(":");
                if (portIndex != -1)
                    domain = domain.substring(0, portIndex);
                cookie1.setDomain(domain);
                cookieStore.addCookie(cookie1);
            }
            httpContext.setCookieStore(cookieStore);
        }
        return httpContext;
    }

    public HttpUriRequest httpUriRequest(Request request) {
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(fixIllegalCharacterInUrl(request.url));
        if (request.headers != null) {
            for (Map.Entry<String, String> headerEntry : request.headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectionRequestTimeout(request.timeOut)
                .setSocketTimeout(request.timeOut)
                .setConnectTimeout(request.timeOut)
                .setCookieSpec(CookieSpecs.STANDARD);

        if (!StrUtils.isEmpty(request.proxyUrl))
            requestConfigBuilder.setProxy(new HttpHost(request.proxyUrl, request.proxyPort));
        requestBuilder.setConfig(requestConfigBuilder.build());
        HttpUriRequest httpUriRequest = requestBuilder.build();
        if (request.headers != null && !request.headers.isEmpty()) {
            for (Map.Entry<String, String> header : request.headers.entrySet()) {
                httpUriRequest.addHeader(header.getKey(), header.getValue());
            }
        }
        return httpUriRequest;
    }

    private RequestBuilder selectRequestMethod(Request request) {
        String method = request.reqType;
        if (method == null || method.equalsIgnoreCase(Request.GET)) {
            //default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(Request.POST)) {
            return addFormParams(RequestBuilder.post(), request);
        }

        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    private RequestBuilder addFormParams(RequestBuilder requestBuilder, Request request) {
        if (request.params != null) {
            ByteArrayEntity entity = new ByteArrayEntity(paramBody(request));
            entity.setContentType(request.paramType.getType());
            requestBuilder.setEntity(entity);
        }
        return requestBuilder;
    }

    public String fixIllegalCharacterInUrl(String url) {
        return url.replace(" ", "%20").replaceAll("#+", "#");
    }

    public byte[] paramBody(Request request) {
        List<NameValuePair> nameValuePairs = new ArrayList<>(request.params.size());
        for (Map.Entry<String, String> entry : request.params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        try {
            return URLEncodedUtils.format(nameValuePairs, request.reqCharset).getBytes(request.reqCharset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + request.reqCharset, e);
        }
    }
}
