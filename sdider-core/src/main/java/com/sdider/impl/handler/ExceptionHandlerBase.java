package com.sdider.impl.handler;

import com.sdider.ResponseConverter;
import com.sdider.SdiderResponse;
import com.sdider.api.ExceptionHandler;
import com.sdider.api.Request;
import com.sdider.api.RequestContainer;
import com.sdider.api.Response;

/**
 * ExceptionHandlerBase将{@link Response}转换为{@link SdiderResponse}
 * @author yujiaxin
 */
public abstract class ExceptionHandlerBase implements ExceptionHandler {
    private final ResponseConverter responseConverter;

    public ExceptionHandlerBase(ResponseConverter responseConverter) {
        this.responseConverter = responseConverter;
    }

    @Override
    public RequestContainer handleException(Exception ex, Request request, Response response) {
        return handle(ex, request, response==null?null:responseConverter.convert(response));
    }

    protected abstract RequestContainer handle(Exception ex, Request request, SdiderResponse response);

}
