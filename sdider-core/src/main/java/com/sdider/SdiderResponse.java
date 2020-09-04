package com.sdider;

import com.sdider.api.Response;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * SdiderResponse是请求成功后的响应结果。
 * @author yujiaxin
 */
public interface SdiderResponse extends Response, Extensible {

    default void items(@DelegatesTo(SdiderResult.class) Closure<?> itemConfig) {
        ClosureUtils.delegateRun(getResult(), itemConfig);
    }

    default void targets(@DelegatesTo(SdiderRequestContainer.class) Closure<?> requestConfig) {
        ClosureUtils.delegateRun(getResult(), requestConfig);
    }

    /**
     * 返回与此Response绑定的{@link SdiderResult}，此方法多次调用
     * 必须返回同一个实例。
     * @return 与此Response绑定的 {@link SdiderResult}
     */
    SdiderResult getResult();

    String getContentType();

    String getCharset();

    String getText();
}
