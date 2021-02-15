package com.example.core.download.httppool;

import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.download.DownloadHandle;
import com.example.core.utils.CharsetUtils;
import com.example.core.utils.D;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

public class HttpClientPoolDownloader extends DownloadHandle {

    public HttpClientPoolDownloader() {

    }

    @Override
    public Response down(Request request) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient client = HttpClientPool.getInstance().getHttpClient(request);
        try {
            httpResponse = client.execute(HttpClientPool.getInstance().httpUriRequest(request), HttpClientPool.getInstance().getHttpClientContext(request));
            return handleResponse(request, httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                //ensure the connection is released back to pool
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        return null;
    }

    private Response handleResponse(Request request, HttpResponse httpResponse) throws IOException {
        Response response = Response.make(request, httpResponse.getStatusLine().getStatusCode());
        response.request = request;

        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String content = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        if (StringUtils.isEmpty(response.resCharset))
            response.resCharset = CharsetUtils.headerEncoding(content);
        if (StringUtils.isEmpty(response.resCharset))
            response.resCharset = CharsetUtils.guessEncoding(bytes);
        response.body = new String(bytes, response.resCharset);

        Header[] headers = httpResponse.getAllHeaders();
        if (ArrayUtils.isNotEmpty(headers)) {
            response.headers = new HashMap<>();
            for (Header header : headers)
                response.headers.put(header.getName().toLowerCase(), header.getValue());
        }

        return response;
    }
}
