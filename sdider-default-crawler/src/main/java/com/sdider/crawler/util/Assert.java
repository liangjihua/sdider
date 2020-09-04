package com.sdider.crawler.util;

/**
 * @author yujiaxin
 */
public class Assert {
    private Assert(){}

    public static <T> T notNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format("%s 不能为null", name));
        }
        return obj;
    }

    public static String notEmpty(String str, String name) {
        notNull(str, name);
        if (str.trim().equals("")) {
            throw new IllegalArgumentException(String.format("%s 不能为空", name));
        }
        return str;
    }
}
