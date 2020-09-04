package com.sdider.impl.handler;

import com.sdider.ExceptionHolder;
import com.sdider.ResponseConverter;
import com.sdider.SdiderResponse;
import com.sdider.api.Request;
import com.sdider.api.RequestContainer;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;

/**
 *
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public class ClosureExceptionHandler extends ExceptionHandlerBase{
    private final Closure action;

    public ClosureExceptionHandler(ResponseConverter responseConverter, Closure action) {
        super(responseConverter);
        this.action = action;
    }

    @Override
    protected RequestContainer handle(Exception ex, Request request, SdiderResponse response) {
        ExceptionHolder exceptionHolder = new DefaultExceptionHolder(ex, request, response);
        ClosureUtils.delegateRun(exceptionHolder, action);
        return exceptionHolder.getTargets();
    }
}
