package com.sdider.util;

import java.util.function.Supplier;

/**
 * @author yujiaxin
 */
public class ObjectUtils {
    private ObjectUtils() {}


    public static <T> T which(T obj1, T obj2) {
        if (obj1 != null) return obj1;
        return obj2;
    }

    public static <T> T or(T obj, Supplier<T> supplier) {
        if (obj != null) return obj;
        return supplier.get();
    }
}
