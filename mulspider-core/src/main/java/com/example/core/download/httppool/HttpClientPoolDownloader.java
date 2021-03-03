package com.example.core.download.httppool;

import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.download.DownloadHandle;
import com.example.core.utils.CharsetUtils;
import com.example.core.utils.CollectionUtils;
import com.example.core.utils.Constant;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientPoolDownloader extends DownloadHandle {
    Logger logger = LoggerFactory.getLogger(HttpClientPoolDownloader.class);

    public HttpClientPoolDownloader() {

    }

    @Override
    public Response down(Request request) {
        CloseableHttpResponse httpResponse = null;
        HttpClientModel httpClient = HttpClientPool.getInstance().getHttpClient(request);

        if (request.cookie != null && !request.cookie.isEmpty())
            for (Map.Entry<String, String> cookieEntry : request.cookie.entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(request.getSite());
                httpClient.cookieStore.addCookie(cookie);
            }

        try {
            httpResponse = httpClient.client.execute(HttpClientPool.getInstance().httpUriRequest(request), HttpClientPool.getInstance().getHttpClientContext(request));
            return handleResponse(request, httpResponse, httpClient.cookieStore);
        } catch (IOException e) {
//            e.printStackTrace();
            if (downloadTimeout != null)
                downloadTimeout.timeOut(request, e);
        } finally {
            if (httpResponse != null) {
                //ensure the connection is released back to pool
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        return null;
    }

    private Response handleResponse(Request request, HttpResponse httpResponse, CookieStore cookieStore) throws IOException {
        Response response = Response.make(request, httpResponse.getStatusLine().getStatusCode());

        if (StringUtils.isEmpty(request.getMeta(Constant.DOWN_FILE))) {
            byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String content = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
            if (StringUtils.isEmpty(response.resCharset))
                response.resCharset = CharsetUtils.headerEncoding(content);
            if (StringUtils.isEmpty(response.resCharset))
                response.resCharset = CharsetUtils.guessEncoding(bytes);
            response.body = new String(bytes, response.resCharset);
        } else {
            FileOutputStream fos = null;

            try {
                String file = request.getMeta(Constant.DOWN_FILE);
                if (file.contains("/")) {
                    String path = file.substring(0, file.lastIndexOf("/"));
                    if (StringUtils.isNotEmpty(path)) {
                        File dir = new File(path);
                        if (!dir.exists())
                            if (!dir.mkdirs())
                                throw new RuntimeException("create dir failed:" + dir.getAbsolutePath());
                    }
                }

                fos = new FileOutputStream(file);
                HttpEntity httpEntity = httpResponse.getEntity();
                long contentLength = httpEntity.getContentLength();
                InputStream is = httpEntity.getContent();
                byte[] buffer = new byte[1024 * 8];
                int r = 0;
                long totalRead = 0;
                while ((r = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, r);
                    totalRead += r;
                }

                EntityUtils.consume(httpEntity);

                boolean succ = (contentLength <= 0 && totalRead > 0) || contentLength == totalRead;
                response.put(Constant.DOWN_FILE_RES, succ);
                if (!succ) {
                    if (!new File(file).delete())
                        logger.error("contentLength:{} totalRead:{}  file {} download failed and delete failed", contentLength, totalRead, file);
                }
            } catch (Exception e) {
                logger.error("down load file err:{}   req info:{}", e.getMessage(), request);
            } finally {
                fos.flush();
                fos.close();
            }
        }


        if (request.responseCookie) {
            List<Cookie> cookieList = cookieStore.getCookies();
            if (!CollectionUtils.isEmpty(cookieList)) {
                response.cookie = new HashMap<>();
                for (Cookie cookie : cookieList)
                    response.cookie.put(cookie.getName(), cookie.getValue());
            }
        }

        if (request.responseHeader) {
            Header[] headers = httpResponse.getAllHeaders();
            if (ArrayUtils.isNotEmpty(headers)) {
                response.headers = new HashMap<>();
                for (Header header : headers)
                    response.headers.put(header.getName().toLowerCase(), header.getValue());
            }
        }
        return response;
    }
}
