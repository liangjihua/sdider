package com.sdider.impl.handler;

import com.sdider.ExceptionHolder;
import com.sdider.SdiderRequestContainer;
import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.impl.request.DefaultRequestContainer;
import com.sdider.impl.request.SdiderRequestFactory;
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
    private final SdiderRequestFactory requestFactory;

    public DefaultExceptionHolder(Exception exception, Request request, Response response, SdiderRequestFactory requestFactory) {
        this.exception = exception;
        this.request = request;
        this.response = response;
        this.requestFactory = requestFactory;
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
        requestContainer = new DefaultRequestContainer(request.getUrl(), requestFactory);
        ClosureUtils.delegateRun(requestContainer, requestConfig);
    }

    @Override
    public SdiderRequestContainer getTargets() {
        return requestContainer;
    }
}
