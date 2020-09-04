package com.sdider.crawler.impl.crawler;

import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.crawler.AbstractCrawler;
import com.sdider.crawler.DownloadCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class CrawlCommandTest {
    private CrawlCommand crawlCommand;
    private AbstractCrawler crawler;
    private ActiveObjectEngine engine;
    private Request request;

    @BeforeEach
    void setUp() {
        crawler = mock(AbstractCrawler.class);
        engine = mock(ActiveObjectEngine.class);
        request = mock(Request.class, "mockRequest");
        crawlCommand = new CrawlCommand(engine, crawler, request);
    }

    @Test
    void run() {
        Response response = mock(Response.class);
        doAnswer(invocation -> {
            DownloadCallback callback = invocation.getArgument(1, DownloadCallback.class);
            callback.succeeded(response);
            return null;
        }).when(crawler).download(same(request), any());

        crawlCommand.run();
        
        assertTrue(crawlCommand.isStart());
        assertTrue(crawlCommand.isDone());
        verify(crawler).download(same(request), any());
        verify(crawler).processResponse(response);
        verify(engine, never()).add(same(crawlCommand));
        verifyNoMoreInteractions(crawler);
    }

    @Test
    void run2() {
        AtomicReference<DownloadCallback> reference = new AtomicReference<>();
        doAnswer(invocation -> {
            DownloadCallback callback = invocation.getArgument(1, DownloadCallback.class);
            reference.set(callback);
            return null;
        }).when(crawler).download(same(request), any());

        crawlCommand.run(); //运行一次，这次“下载并没有完成”

        assertTrue(crawlCommand.isStart());
        assertFalse(crawlCommand.isDone());
        verify(crawler).download(same(request), any());
        verify(engine).add(same(crawlCommand));
        verifyNoMoreInteractions(crawler);
        verifyNoMoreInteractions(engine);

        reset(engine);
        reset(crawler);

        Response response = mock(Response.class);
        reference.get().succeeded(response); //“下载完成”
        assertTrue(crawlCommand.isStart());
        assertTrue(crawlCommand.isDone());

        crawlCommand.run(); //第二次运行，“下载已完成”
        verify(crawler, never()).download(any(), any());
        verify(engine, never()).add(any());
        verify(crawler).processResponse(same(response));
        verifyNoMoreInteractions(crawler);
        verifyNoMoreInteractions(engine);
    }

    @Test
    void runWhenExceptionThrown() {
        Exception ex = new RuntimeException();
        doAnswer(invocation -> {
            DownloadCallback callback = invocation.getArgument(1, DownloadCallback.class);
            callback.failed(ex);
            return null;
        }).when(crawler).download(same(request), any());

        crawlCommand.run();
        assertTrue(crawlCommand.isStart());
        assertTrue(crawlCommand.isDone());
        verify(crawler).download(same(request), any());
        verify(crawler).handleException(same(ex), same(request), isNull());
        verifyNoMoreInteractions(crawler);
    }
}