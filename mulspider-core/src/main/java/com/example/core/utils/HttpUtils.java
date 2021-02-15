//package com.example.core.utils;
//
//import com.example.core.models.Request;
//import com.example.core.models.Response;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpRequestRetryHandler;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.conn.routing.HttpRoute;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class HttpUtils {
//    public final static int GET = 1;
//    public final static int POST = 2;
//
//    private static CloseableHttpClient httpClient = null;
//
//    static {
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        // 总连接池数量
//        connectionManager.setMaxTotal(150);
//        // 可为每个域名设置单独的连接池数量
//        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("xx.xx.xx.xx")), 80);
//        // setConnectTimeout：设置建立连接的超时时间
//        // setConnectionRequestTimeout：从连接池中拿连接的等待超时时间
//        // setSocketTimeout：发出请求后等待对端应答的超时时间
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(1000)
//                .setConnectionRequestTimeout(2000)
//                .setSocketTimeout(3000)
//                .build();
//        // 重试处理器，StandardHttpRequestRetryHandler
//        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
//
//        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
//                .setRetryHandler(retryHandler).build();
//    }
//
//    public static HttpGet createGet(Request request) {
//        try {
//            URIBuilder uriBuilder = new URIBuilder(request.url);
//            if (request.params != null && !request.params.isEmpty()) {
//                List<NameValuePair> list = new ArrayList<>();
//                for (Map.Entry<String, String> param : request.params.entrySet())
//                    list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
//                uriBuilder.setParameters(list);
//            }
//            return new HttpGet(uriBuilder.build());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static HttpPost createPost(Request request) {
//        HttpPost httpPost = new HttpPost(request.url);
//        if (request.params != null && !request.params.isEmpty()) {
//            List<NameValuePair> list = new ArrayList<>();
//            for (Map.Entry<String, String> param : request.params.entrySet()) {
//                list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
//            }
//            HttpEntity httpEntity = null;
//            try {
//                httpEntity = new UrlEncodedFormEntity(list, request.reqCharset);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            httpPost.setEntity(httpEntity);
//        }
//
//        return httpPost;
//    }
//
//    public static boolean doHttp(Request request) {
//        CloseableHttpResponse httpResponse = null;
//        //TODO 对象池
//        Response response = null;
//        try {
//            String body = null;
//            HttpRequestBase httpRequest = Request.GET.equalsIgnoreCase(request.method) ? createGet(request) : createPost(request);
//            httpResponse = httpClient.execute(httpRequest);
//            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            switch (statusCode) {
//                case HttpStatus.SC_OK:
//                    HttpEntity entity = httpResponse.getEntity();
//                    body = EntityUtils.toString(entity, response.resCharset);
//                    break;
//            }
//            response = Response.make(request, body, statusCode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (httpResponse != null)
//                    httpResponse.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return ReflectUtils.invokeMethod(response);
//    }
//}