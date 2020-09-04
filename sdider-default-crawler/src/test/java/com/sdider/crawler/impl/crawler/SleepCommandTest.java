package com.sdider.crawler.impl.crawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class SleepCommandTest {
    private ActiveObjectEngine engine;
    private SleepCommand sleepCommand;
    private Command command;

    @BeforeEach
    void setUp() {
        engine = mock(ActiveObjectEngine.class);
        command = mock(Command.class);
        sleepCommand = new SleepCommand(engine, command, 100, TimeUnit.MILLISECONDS);
    }

    @Test
    void run() throws InterruptedException {
        sleepCommand.run();

        verify(engine).add(same(sleepCommand));
        verify(engine, never()).add(same(command));

        reset(engine);
        Thread.sleep(100);
        sleepCommand.run();

        verify(engine, never()).add(same(sleepCommand));
        verify(engine).add(same(command));
    }
}