package com.sdider.impl;

import com.sdider.RequestConfig;
import com.sdider.SdiderRequest;
import com.sdider.impl.request.DefaultRequestImpl;
import com.sdider.impl.request.SdiderRequestFactory;

/**
 * {@link SdiderRequestFactory}的默认实现，此实现会所根据持有的{@link #requestConfig}
 * 的状态创建带有默认{@link RequestConfig}的{@link DefaultRequestImpl}
 * @author yujiaxin
 */
public class DefaultRequestFactory implements SdiderRequestFactory {
    private RequestConfig requestConfig;

    @Override
    public SdiderRequest create() {
        DefaultRequestImpl request = new DefaultRequestImpl();
        if (requestConfig != null) {
            request.setHeaders(requestConfig.getHeaders());
            request.proxy(requestConfig.getProxy());
        }
        return request;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }
}
