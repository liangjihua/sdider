package com.sdider;

import com.sdider.api.Request;
import com.sdider.api.common.DynamicPropertiesObject;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.Map;

/**
 * RequestConfig是{@link Request}请求的通用配置，提供一系列方法简化
 * {@link Request}的常用项目的配置。
 * @author yujiaxin
 */
public interface RequestConfig {
    String APPLICATION_JSON = "application/json;charset=UTF-8";
    String TEXT_HTML = "text/html; charset=UTF-8";
    String TEXT_PLAIN = "text/plain;charset=UTF-8";
    String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    String UA_FIREFOX = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0";
    String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";
    String UA_SDIDER = "Sdider/0.1";

    /**
     * 设置请求头Content-Type
     * @param contentType contentType
     */
    void contentType(String contentType);

    /**
     * 返回已设置的请求头Content-Type
     * @return 已设置的请求头Content-Type或null
     */
    String getContentType();

    /**
     * 设置请求头。
     * 传入的闭包用于设置请求头。代理给{@link DynamicPropertiesObject}执行。
     *
     * @param headersSetClosure closure to config headers
     */
    void headers(@DelegatesTo(DynamicPropertiesObject.class) Closure<?> headersSetClosure);

    /**
     * 使用给定的headers替换原有headers
     * @param headers headers
     */
    void setHeaders(Map<String, String> headers);

    /**
     * 返回headers
     * @return headers
     */
    Map<String, String> getHeaders();

    /**
     * 设置请求头User-Agent
     * @param userAgent userAgent
     */
    void userAgent(String userAgent);

    /**
     * 返回已设置的请求头User-Agent
     * @return 已设置的请求头User-Agent
     */
    String getUserAgent();

    /**
     * 设置请求代理
     * @param proxy 代理地址，例：127.0.0.1:1080
     */
    void proxy(String proxy);

    /**
     * 返回设置的请求代理
     * @return 设置的请求代理
     */
    String getProxy();
}
