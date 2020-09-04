package com.sdider.crawler.impl.downloader;

import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.crawler.DownloadCallback;
import com.sdider.crawler.Downloader;
import com.sdider.crawler.exception.DownloadFailedException;
import com.sdider.crawler.impl.response.DefaultResponse;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIAuthority;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager.DEFAULT_MAX_CONNECTIONS_PER_ROUTE;
import static org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS;

/**
 * 默认Downloader，使用Apache HttpClient 5.0异步模式下载，支持gzip等压缩格式
 * @author yujiaxin
 */
public class DefaultDownloader implements Downloader {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDownloader.class);
    private final CloseableHttpAsyncClient httpAsyncClient;
    private final int maxTotal;
    private final int maxPerRoute;

    public DefaultDownloader() {
        this(DEFAULT_MAX_TOTAL_CONNECTIONS, DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
    }

    public DefaultDownloader(int maxTotal, int maxPerRoute) {
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
        PoolingAsyncClientConnectionManager connMr = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(getMaxTotal())
                .setMaxConnPerRoute(getMaxPerRoute()).build();
        httpAsyncClient = createHttpClient(connMr);
    }

    private CloseableHttpAsyncClient createHttpClient(PoolingAsyncClientConnectionManager connMr) {
        return HttpAsyncClients.custom()
                .setDefaultRequestConfig(getDefaultRequestConfigBuilder().build())
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(5))
                .setConnectionManager(connMr)
                .addRequestInterceptorFirst((request, entity, context) -> {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                })
                .build();
    }

    private RequestConfig.Builder getDefaultRequestConfigBuilder() {
        return RequestConfig.custom().setCookieSpec(StandardCookieSpec.RELAXED)
                .setConnectTimeout(Timeout.ofMilliseconds(3000))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(5000))
                .setResponseTimeout(Timeout.ofMilliseconds(3000));
    }

    @Override
    public void start() {
        httpAsyncClient.start();
    }

    @Override
    public void stop() {
        try {
            httpAsyncClient.close();
        } catch (IOException e) {
            logger.error("关闭HttpClient异常", e);
        }
    }

    @Override
    public void download(Request request, DownloadCallback callback) throws DownloadFailedException {
        try {
            httpAsyncClient.execute(SimpleRequestProducer.create(buildRequest(request)),
                    new SimpleDecompressingResponseConsumer(),
                    new FutureCallback<SimpleHttpResponse>() {
                        @Override
                        public void completed(SimpleHttpResponse result) {
                            callback.succeeded(createResponse(request, result));
                        }

                        @Override
                        public void failed(Exception ex) {
                            ex = ex instanceof DownloadFailedException ? ex : new DownloadFailedException(request, ex);
                            callback.failed(ex);
                        }

                        @Override
                        public void cancelled() {
                            callback.failed(new DownloadFailedException("请求取消", request));
                        }
                    });
        } catch (Exception ex) {
            throw new DownloadFailedException(request, ex);
        }
    }

    private Response createResponse(Request request, SimpleHttpResponse response) {
        return new DefaultResponse(request, response);
    }

    private SimpleHttpRequest buildRequest(Request request) throws URISyntaxException {
        URI uri = new URI(request.getUrl());
        String body = request.getBody();
        ContentType contentType = request.getContentType() != null?ContentType.parse(request.getContentType()): ContentType.DEFAULT_TEXT;
        Map<String, String> params = request.getParams();
        if (params != null && !params.isEmpty()) {
            if (body == null && (Method.POST.isSame(request.getMethod()) || Method.PUT.isSame(request.getMethod()))) {
                body = URLEncodedUtils.format(params.entrySet()
                                .stream().map((entry)->new BasicNameValuePair(entry.getKey(), entry.getValue()))
                                .collect(Collectors.toList()), ContentType.APPLICATION_FORM_URLENCODED.getCharset());
                contentType = ContentType.APPLICATION_FORM_URLENCODED;
            } else {
                try {
                    URIBuilder uriBuilder = new URIBuilder(request.getUrl());
                    params.forEach(uriBuilder::addParameter);
                    uri = uriBuilder.build();
                } catch (final URISyntaxException ex) {
                    // should never happen
                }
            }
        }
        SimpleHttpRequest httpRequest = SimpleHttpRequests.create(request.getMethod(), uri);
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(httpRequest::addHeader);
        }
        if (body != null) {
            httpRequest.setBody(body, contentType);
        }
        if (request.getProxy() != null && !"".equals(request.getProxy().trim())) {
            RequestConfig.Builder builder = getDefaultRequestConfigBuilder()
                    .setProxy(new HttpHost(URIAuthority.create(request.getProxy())));
            httpRequest.setConfig(builder.build());
        }
        return httpRequest;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }
}
