package com.sdider.utils;

import groovy.lang.Closure;

/**
 * closure工具类，提供常用的closure操作。
 *
 * @author yujiaxin
 */
public class ClosureUtils {
    private ClosureUtils(){}

    /**
     * 以delegate first的模式运行closure
     * @param delegateObject delegate object
     * @param closure 要运行的closure
     */
    public static void delegateRun(Object delegateObject, Closure<?> closure) {
        delegateCall(delegateObject, closure);
    }

    /**
     * 以delegate first的模式运行closure，返回值closure的运行结果
     * @param delegateObject delegate object
     * @param closure 要运行的closure
     * @param <T> 返回值的泛型
     * @return closure的返回值
     */
    public static <T> T delegateCall(Object delegateObject, Closure<T> closure) {
        closure.setDelegate(delegateObject);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        if (closure.getMaximumNumberOfParameters() == 0) {
            return closure.call();
        }
        return closure.call(delegateObject);
    }
}
