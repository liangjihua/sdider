package com.sdider.api;

/**
 * ResponseParser处理{@link Response}，从中提取数据和请求。
 * @see Response
 * @see Request
 * @author yujiaxin
 */
@FunctionalInterface
public interface ResponseParser {

    /**
     * 解析一个{@link Response}，提取并返回数据和请求。
     * @param response response
     * @return 包含提取的数据和请求的 {@link Result}，永远不会返回null
     */
    Result parse(Response response);
}
