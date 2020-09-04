package com.sdider.api;

/**
 * 处理爬取过程中的任何异常
 * @author yujiaxin
 */
public interface ExceptionHandler {

    /**
     * 处理爬取异常，返回待爬取请求。
     * @param ex 异常
     * @param request 原请求
     * @param response 请求响应，可为null
     * @return {@link RequestContainer}对象，包含了待爬取请求；没有请求返回null。
     */
    RequestContainer handleException(Exception ex, Request request, Response response);
}
