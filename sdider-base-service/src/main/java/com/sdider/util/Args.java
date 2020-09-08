package com.sdider.util;

/**
 * @author yujiaxin
 */
public class Args {
    private Args() {}

    public static <T> T notNull(T arg, String name) {
        if (arg == null) {
            String msg = String.format("invalid argument: {'name':'%s', 'reason':'argument is null'}", name);
            throw new IllegalArgumentException(msg);
        }
        return arg;
    }

    public static String notEmpty(String arg, String name) {
        notNull(arg, name);
        if (arg.trim().equals("")) {
            String msg = String.format("invalid argument: {'name':'%s', 'reason':'String argument is empty'}", name);
            throw new IllegalArgumentException(msg);
        }
        return arg;
    }
}
