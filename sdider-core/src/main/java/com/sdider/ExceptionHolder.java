package com.sdider;

import com.sdider.api.Request;
import com.sdider.api.Response;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * ExceptionHolder是在爬取过程中产生异常时的一个容器。
 * @author yujiaxin
 */
public interface ExceptionHolder {

    Exception getException();

    Request getRequest();

    Response getResponse();

    void targets(@DelegatesTo(SdiderRequestContainer.class) Closure<?> requestConfig);

    SdiderRequestContainer getTargets();
}
