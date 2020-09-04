package com.sdider;

import com.sdider.api.RequestContainer;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * SdiderRequestContainer管理一组{@link SdiderRequest}
 * @author yujiaxin
 */
public interface SdiderRequestContainer extends RequestContainer {

    /**
     * 创建并添加一个{@link SdiderRequest}，给定的闭包会被委托给该request，用于配置
     * 该request
     * @param requestConfig 用于配置request的闭包
     */
    void request(@DelegatesTo(SdiderRequest.class) Closure<?> requestConfig);

    /**
     * 创建并添加一组{@link SdiderRequest}，这些requests的method为<b>GET</b><br/>
     * 给定的urls，若包含null、empty string将会被忽略。这在从其他方法中提取urls，并调用
     * 此方法加入时非常有用，调用者不必再手动过滤掉空的字符串。
     * @param urls 请求urls
     */
    void request(String... urls);
}
