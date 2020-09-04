package com.sdider.crawler.impl.crawler;

import com.sdider.api.Configuration;
import com.sdider.api.Request;
import com.sdider.crawler.AbstractCrawler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.internal.util.reflection.FieldReader;

import static com.sdider.crawler.impl.crawler.CrawlFactoryCommand.INTERVAL_CONFIG_NAME;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CrawlFactoryCommandTest {
    private CrawlFactoryCommand command;
    private AbstractCrawler crawler;
    private ActiveObjectEngine engine;
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        crawler = mock(AbstractCrawler.class);
        configuration = mock(Configuration.class);
        when(configuration.get(INTERVAL_CONFIG_NAME)).thenReturn(5);
        when(crawler.getConfiguration()).thenReturn(configuration);
        engine = mock(ActiveObjectEngine.class);
        command = spy(new CrawlFactoryCommand(engine, crawler));
        reset(crawler, engine, configuration);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void constructor() throws NoSuchFieldException {
        when(crawler.getConfiguration()).thenReturn(configuration);
        command = spy(new CrawlFactoryCommand(engine, crawler));

        verify(crawler).getConfiguration();
        verify(configuration).configure(any());
        FieldReader reader = new FieldReader(command, CrawlFactoryCommand.class.getDeclaredField("sleepCommand"));
        assertNotNull(reader.read());
    }

    @Test
    void runWhenEngineNotRunning() {
        command.run();

        verify(engine).isRunning();
        verifyNoMoreInteractions(crawler, engine);
    }

    /**
     * 测试{@link CrawlFactoryCommand}在引擎容量不足时不再生成新的Command
     */
    @ParameterizedTest
    @CsvSource({
            "10, 0",
            "10, 9",
    })
    void engineCommandSize(int maxSize, int currentSize) {
        when(engine.isRunning()).thenReturn(true);
        when(engine.getMaxSize()).thenReturn(maxSize);
        when(engine.getCurrentSize()).thenReturn(currentSize);
        when(engine.isEmpty()).thenReturn(false);

        command.run();

        verify(engine).isRunning();
        verify(engine).getMaxSize();
        verify(engine).getCurrentSize();
        verify(engine).isEmpty();
        verify(engine).add(same(command));
        if ((maxSize - currentSize) > 1) {
            verify(crawler).popRequest();
        }
        verifyNoMoreInteractions(crawler, engine);
    }

    @Test
    void interval() {
        when(engine.isRunning()).thenReturn(true);
        when(engine.getMaxSize()).thenReturn(10);
        when(engine.getCurrentSize()).thenReturn(0);
        when(crawler.popRequest()).thenReturn(mock(Request.class));

        command.run();

        verify(engine).add(any(SleepCommand.class));
    }

}