package com.sdider.api;


import java.util.Map;

/**
 * Response是{@link Request}请求成功后的响应结果。
 * @see Request
 * @author yujiaxin
 */
public interface Response {
    Request getRequest();

    byte[] getBody();

    int getStatusCode();

    String getReasonPhrase();

    /**
     * 返回一个map，包含所有的响应headers。返回的map是安全的，
     * 修改该map不会影响到response对象自身的headers。
     * @return 包含所有响应headers的map
     */
    Map<String, String> getHeaders();
}
