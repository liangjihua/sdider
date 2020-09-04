package com.sdider.crawler.impl.downloader;

import com.sdider.api.Request;
import com.sdider.crawler.DownloadCallback;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

class DefaultDownloaderTest {
    private DefaultDownloader downloader;
    private Request request;

    @BeforeEach
    void setUp() {
        downloader = new DefaultDownloader();
        request = mock(Request.class);
        when(request.getUrl()).thenReturn("http://foo.bar");
        when(request.getMethod()).thenReturn("GET");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void construct() {
        downloader = spy(new DefaultDownloader(2, 3));

        assertEquals(2, downloader.getMaxTotal());
        assertEquals(3, downloader.getMaxPerRoute());
        verify(downloader).getMaxTotal();
        verify(downloader).getMaxPerRoute();
    }

    @Test
    void start() throws NoSuchFieldException {
        CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
        setField(downloader, DefaultDownloader.class.getDeclaredField("httpAsyncClient"), client);
        downloader.start();

        verify(client).start();
    }

    @Test
    void stop() throws NoSuchFieldException, IOException {
        CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
        setField(downloader, DefaultDownloader.class.getDeclaredField("httpAsyncClient"), client);
        downloader.start();
        downloader.stop();

        verify(client).close();
    }

    @SuppressWarnings("unchecked")
    @Test
    void download() throws NoSuchFieldException {
        CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
        doAnswer(invocation -> {
            FutureCallback<SimpleHttpResponse> argument = invocation.getArgument(2, FutureCallback.class);
            argument.completed(createResponse());
            return null;
        }).when(client).execute(any(AsyncRequestProducer.class), any(), any());
        setField(downloader, DefaultDownloader.class.getDeclaredField("httpAsyncClient"), client);
        DownloadCallback callback = mock(DownloadCallback.class);

        downloader.download(request, callback);

        verify(client).execute(any(SimpleRequestProducer.class), any(), any());
        verify(callback).succeeded(any());
    }

    private SimpleHttpResponse createResponse() {
        SimpleHttpResponse simpleHttpResponse = SimpleHttpResponse.create(200, "-1");
        simpleHttpResponse.addHeader("foo", "bar");
        return simpleHttpResponse;
    }
}