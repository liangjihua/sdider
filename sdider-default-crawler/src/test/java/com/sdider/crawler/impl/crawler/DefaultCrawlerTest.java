package com.sdider.crawler.impl.crawler;

import com.sdider.api.Configuration;
import com.sdider.api.ExceptionHandler;
import com.sdider.api.Pipeline;
import com.sdider.api.ResponseParser;
import com.sdider.crawler.Downloader;
import com.sdider.crawler.Scheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultCrawlerTest {
    private DefaultCrawler crawler;
    private AutoCloseable closeable;
    @Mock private Downloader downloader;
    @Mock private ExceptionHandler handler;
    @Mock private Pipeline pipeline;
    @Mock private ResponseParser parser;
    @Mock private Scheduler scheduler;
    @Mock private Configuration configuration;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(configuration.get("downloader")).thenReturn(downloader);
        when(configuration.get("scheduler")).thenReturn(scheduler);
        when(configuration.get("concurrentRequests")).thenReturn(2);
        crawler = spy(new DefaultCrawler(parser, Collections.singletonList(pipeline), handler, configuration));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void crawl() {
        Command command = mock(Command.class);
        crawler.add(command);
        crawler.crawl();
        assertEquals(0, crawler.getCurrentSize());
        verify(command, atLeastOnce()).run();
    }

    @Test
    void crawlWhenCommandThrown() {
        Command command = mock(Command.class);
        doThrow(RuntimeException.class).when(command).run();
        crawler.add(command);
        crawler.crawl();
        assertEquals(0, crawler.getCurrentSize());
        verify(command, atLeastOnce()).run();
    }

    @Test
    void add() {
        crawler.add(mock(Command.class));
        assertEquals(1, crawler.getCurrentSize());
    }

    @Test
    void addNull() {
        assertThrows(IllegalArgumentException.class, () ->crawler.add(null));
    }

    @Test
    void addToFull() {
        crawler.add(mock(Command.class));
        crawler.add(mock(Command.class));
        assertThrows(IllegalStateException.class, () ->crawler.add(mock(Command.class)));
    }

    @Test
    void addWhenRunning() {
        when(crawler.isRunning()).thenReturn(true);

        crawler.add(mock(Command.class));
        crawler.add(mock(Command.class));
        crawler.add(mock(Command.class));
        assertThrows(IllegalStateException.class, () ->crawler.add(mock(Command.class)));
    }

    @Test
    void isEmpty() {
        assertTrue(crawler.isEmpty());
        crawler.add(mock(Command.class));
        assertFalse(crawler.isEmpty());
    }

    @Test
    void getMaxSize() {
        assertEquals(3, crawler.getMaxSize());
        when(configuration.get("concurrentRequests")).thenReturn(1);
        System.out.println(configuration.get("concurrentRequests"));
        crawler = new DefaultCrawler(parser, Collections.singletonList(pipeline), handler, configuration);
        assertEquals(2, crawler.getMaxSize());
    }

    @Test
    void getCurrentSize() {
        crawler.add(mock(Command.class));
        crawler.add(mock(Command.class));
        assertEquals(2, crawler.getCurrentSize());
    }
}