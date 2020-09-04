package com.sdider.crawler.impl.scheduler;

import com.sdider.api.Request;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultSchedulerTest {

    @Test
    void compositeTest() {
        DefaultScheduler scheduler = new DefaultScheduler(new LinkedList<>());
        Request request1 = mock(Request.class);
        Request request2 = mock(Request.class);
        scheduler.push(request1);
        scheduler.push(request2);

        assertEquals(request1, scheduler.pop());
        assertEquals(request2, scheduler.pop());
        assertNull(scheduler.pop());
    }

    @Test
    void isEmpty() {
        DefaultScheduler scheduler = new DefaultScheduler(new LinkedList<>());
        assertTrue(scheduler.isEmpty());

        scheduler.push(mock(Request.class));
        assertFalse(scheduler.isEmpty());

        scheduler.pop();
        assertTrue(scheduler.isEmpty());
    }
}