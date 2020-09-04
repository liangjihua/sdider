package com.sdider.crawler.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayQueueTest {
    private ArrayQueue<Object> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayQueue<>(10);
    }

    @Test
    void add() {
        queue.add(new Object());
        queue.add(new Object());
        assertEquals(2, queue.getSize());
    }

    @Test
    void addNull() {
        assertThrows(NullPointerException.class, () ->queue.add(null));
    }

    @Test
    void addToAFullQueue() {
        for (int i = 0; i < 10; i++) {
            queue.add(new Object());
        }
        assertThrows(IllegalStateException.class, () -> queue.add(new Object()));

    }

    @Test
    void pop() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.add(obj1);
        queue.add(obj2);

        assertEquals(obj1, queue.pop());
        assertEquals(obj2, queue.pop());
        assertEquals(0, queue.getSize());
    }

    @Test
    void popACycle() {//添加的项目总数量大于最大容量时（一边弹出项目）
        for (int i = 0; i < 10; i++) {
            queue.add(new Object());
        }
        for (int i = 0; i < 10; i++) {
            queue.pop();
        }
        pop();
    }

    @Test
    void popFromAEmptyQueue() {
        assertThrows(NoSuchElementException.class, () -> queue.pop());
    }

    @Test
    void getSize() {
        queue.add(new Object());
        assertEquals(1, queue.getSize());

        queue.add(new Object());
        queue.add(new Object());
        assertEquals(3, queue.getSize());

        queue.pop();
        assertEquals(2, queue.getSize());

        for (int i = 0; i < 8; i++) {
            queue.add(new Object());
        }
        assertEquals(10, queue.getSize());
    }

    @Test
    void getSizeACycle() {
        for (int i = 0; i < 10; i++) {
            queue.add(new Object());
        }
        for (int i = 0; i < 5; i++) {
            queue.pop();
        }
        for (int i = 0; i < 3; i++) {
            queue.add(new Object());
        }
        assertEquals(8, queue.getSize());
        for (int i = 0; i < 8; i++) {
            queue.pop();
        }
        assertEquals(0, queue.getSize());
    }

    @Test
    void isEmpty() {
        assertTrue(queue.isEmpty());

        queue.add(new Object());
        assertFalse(queue.isEmpty());

        queue.pop();
        assertTrue(queue.isEmpty());
    }
}