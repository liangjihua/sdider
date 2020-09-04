package com.sdider.crawler.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertTest {

    @Test
    void notNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Assert.notNull(null, "obj"));
        assertEquals("obj 不能为null", exception.getMessage());

        Object obj = new Object();
        assertSame(obj, Assert.notNull(obj, "obj"));
    }

    @Test
    void notEmpty() {
        String foo = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Assert.notEmpty(foo, "obj"));
        assertEquals("obj 不能为null", exception.getMessage());
    }

    @Test
    void emptyString() {
        String foo = "";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Assert.notEmpty(foo, "obj"));
        assertEquals("obj 不能为空", exception.getMessage());
    }

    @Test
    void notEmptyString() {
        String foo = "test";
        assertSame(foo, Assert.notEmpty(foo, "obj"));
    }
}