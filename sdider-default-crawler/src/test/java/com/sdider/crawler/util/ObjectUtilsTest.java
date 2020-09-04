package com.sdider.crawler.util;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class ObjectUtilsTest {

    @Test
    void which() {
        Object obj = new Object();
        assertSame(obj, ObjectUtils.which(null, obj));

        assertSame(obj, ObjectUtils.which(obj, null));
    }

    @Test
    void which2() {
        Object obj = new Object();
        Object obj2 = new Object();
        assertSame(obj, ObjectUtils.which(obj, obj2));
    }

    @Test
    void or() {
        Object obj = new Object();
        Supplier<Object> supplier = () -> obj;

        assertSame(obj, ObjectUtils.or(null, supplier));
    }

    @SuppressWarnings("unchecked")
    @Test
    void or2() {
        Object obj = new Object();
        Supplier<Object> supplier = mock(Supplier.class);

        assertSame(obj, ObjectUtils.or(obj, supplier));
        verify(supplier, never()).get();
    }
}