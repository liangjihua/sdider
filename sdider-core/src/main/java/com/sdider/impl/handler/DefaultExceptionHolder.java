package com.sdider.impl.handler;

import com.sdider.ExceptionHolder;
import com.sdider.SdiderRequestContainer;
import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.impl.request.DefaultRequestContainer;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * @author yujiaxin
 */
public class DefaultExceptionHolder implements ExceptionHolder {
    private SdiderRequestContainer requestContainer;
    private final Exception exception;
    private final Request request;
    private final Response response;

    public DefaultExceptionHolder(Exception exception, Request request, Response response) {
        this.exception = exception;
        this.request = request;
        this.response = response;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public void targets(@DelegatesTo(SdiderRequestContainer.class) Closure<?> requestConfig) {
        requestContainer = new DefaultRequestContainer();
        ClosureUtils.delegateRun(requestContainer, requestConfig);
    }

    @Override
    public SdiderRequestContainer getTargets() {
        return requestContainer;
    }

    @Override
    public String toString() {
        return "DefaultExceptionHolder{" + "exception=" + exception +
                ", request=" + request +
                ", response=" + response +
                '}';
    }
}
