package com.sdider.crawler;

import com.sdider.api.*;
import com.sdider.crawler.exception.CrawlerExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

class AbstractCrawlerTest {
    private AbstractCrawler crawler;
    private AutoCloseable closeable;
    @Mock private Downloader downloader;
    @Mock private ExceptionHandler handler;
    @Mock private Pipeline pipeline;
    @Mock private ResponseParser parser;
    @Mock private Scheduler scheduler;
    @Mock private Request mockRequest;
    @Mock private Configuration configuration;
    @Mock private Consumer<Scheduler> schedulerDump;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        doAnswer(call -> {
            AbstractCrawler.Config config = call.getArgument(0, AbstractCrawler.Config.class);
            config.setDownloader(downloader);
            config.setScheduler(scheduler);
            config.setSchedulerDump(schedulerDump);
            return null;
        }).when(configuration).configure(any());
        crawler = mock(AbstractCrawler.class, withSettings()
                .useConstructor(parser, Collections.singletonList(pipeline), handler, configuration)
                .defaultAnswer(CALLS_REAL_METHODS));
        crawler.startRequests(Collections.singletonList(mockRequest));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void constructor() {
        verify(configuration).configure(any(AbstractCrawler.Config.class));
        verify(downloader).start();
    }

    @Test
    void popRequest() {
        when(scheduler.pop()).thenReturn(mockRequest);
        Request request1 = crawler.popRequest();

        verify(scheduler).pop();
        assertSame(mockRequest, request1);
    }

    @Test
    void asyncDownload() {
        DownloadCallback callback = mock(DownloadCallback.class);

        crawler.download(mockRequest, callback);

        verify(downloader).download(same(mockRequest), same(callback));
    }

    @Test
    void syncDownload() throws Exception {
        Response response = mock(Response.class);
        doAnswer(i -> {
            DownloadCallback argument = i.getArgument(1, DownloadCallback.class);
            argument.succeeded(response);
            return null;
        }).when(downloader).download(same(mockRequest), any());

        assertSame(response, crawler.download(mockRequest));
        verify(downloader).download(same(mockRequest), any());
    }

    @Test
    void syncDownloadFail() {
        Exception ex = new Exception();
        doAnswer(i -> {
            DownloadCallback downloadCallback = i.getArgument(1, DownloadCallback.class);
            downloadCallback.failed(ex);
            return null;
        }).when(downloader).download(same(mockRequest), any());

        assertThrows(Exception.class, () -> crawler.download(mockRequest));
        verify(downloader).download(same(mockRequest), any());
    }

    @Test
    void processResponse() {
        Response response = when(mock(Response.class).getRequest()).thenReturn(mockRequest).getMock();
        when(parser.parse(same(response))).thenReturn(mock(Result.class));
        when(crawler.isStopped()).thenReturn(false);

        crawler.processResponse(response);
        verify(crawler, atLeastOnce()).isStopped();
    }

    @Test
    void handleException() {
        Exception exception = new RuntimeException();
        when(handler.handleException(same(exception), same(mockRequest), isNull())).thenReturn(mock(RequestContainer.class));
        when(crawler.isStopped()).thenReturn(false);

        crawler.handleException(exception, mockRequest, null);

        verify(crawler, atLeastOnce()).isStopped();
        verify(handler).handleException(same(exception), same(mockRequest), isNull());
        verify(crawler).schedule(anyList());
    }

    @Test
    void run() {
        assertFalse(crawler.isRunning());
        verify(downloader).start();

        crawler.run();

        assertFalse(crawler.isRunning());
        assertTrue(crawler.isStopped());
        verify(crawler).crawl();
        verify(schedulerDump).accept(any());
        verify(downloader).stop();
        verify(crawler).schedule(anyList());
    }

    @Test
    void startTwice() throws NoSuchFieldException {
        setField(crawler, AbstractCrawler.class.getDeclaredField("status"),
                new AtomicReference<>(AbstractCrawler.Status.RUNNING));
        assertThrows(CrawlerExecutionException.class, () -> crawler.run());
    }

    @Test
    void runFailed() {
        doThrow(RuntimeException.class).when(crawler).crawl();
        assertThrows(CrawlerExecutionException.class,
                () ->crawler.run());
    }

    @Test
    void schedule() {
        when(crawler.isStopped()).thenReturn(false);
        List<Request> requests = spy(Arrays.asList(mock(Request.class), mock(Request.class)));

        crawler.schedule(requests);

        verify(crawler).isStopped();
        verify(requests).iterator();
        verify(scheduler, times(2)).push(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    void scheduleWhenStopped() {
        assertThrows(CrawlerExecutionException.class, () ->crawler.schedule(mock(List.class)));
        verify(crawler).isStopped();
    }

    @Test
    void scheduleContainerWhenStopped() {
        assertThrows(CrawlerExecutionException.class, () ->crawler.schedule(mock(RequestContainer.class)));
        verify(crawler).isStopped();
    }

    @Test
    void scheduleContainer() {
        when(crawler.isStopped()).thenReturn(false);
        RequestContainer requestContainer = mock(RequestContainer.class);

        crawler.schedule(requestContainer);

        verify(crawler).isStopped();
        verify(requestContainer).getRequests();
        verify(crawler).schedule(anyList());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shutdown() throws NoSuchFieldException {
        setField(crawler, AbstractCrawler.class.getDeclaredField("status"),
                new AtomicReference<>(AbstractCrawler.Status.RUNNING));
        setField(crawler, AbstractCrawler.class.getDeclaredField("schedulerCopy"), scheduler);

        crawler.shutdown();

        FieldReader reader = new FieldReader(crawler, AbstractCrawler.class.getDeclaredField("status"));
        assertEquals(AbstractCrawler.Status.SHUTDOWN, ((AtomicReference<AbstractCrawler.Status>)reader.read()).get());
        reader = new FieldReader(crawler, AbstractCrawler.class.getDeclaredField("scheduler"));
        Scheduler scheduler1 = (Scheduler) reader.read();
        assertNotSame(scheduler, scheduler1);
        assertTrue(scheduler1.isEmpty());
        assertNull(scheduler1.pop());
        scheduler1.push(mock(Request.class));
        verify(scheduler).push(any(Request.class));
    }

    @Test
    void isStopped() throws NoSuchFieldException {
        assertTrue(crawler.isStopped());
        setField(crawler, AbstractCrawler.class.getDeclaredField("status"),
                new AtomicReference<>(AbstractCrawler.Status.RUNNING));
        assertFalse(crawler.isStopped());
    }

    @Test
    void isRunning() throws NoSuchFieldException {
        assertFalse(crawler.isRunning());
        setField(crawler, AbstractCrawler.class.getDeclaredField("status"),
                new AtomicReference<>(AbstractCrawler.Status.RUNNING));
        assertTrue(crawler.isRunning());
    }
}