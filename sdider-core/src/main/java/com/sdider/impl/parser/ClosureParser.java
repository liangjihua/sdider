package com.sdider.impl.parser;

import com.sdider.ResponseConverter;
import com.sdider.SdiderResponse;
import com.sdider.api.Response;
import com.sdider.api.ResponseParser;
import com.sdider.api.Result;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;

/**
 *
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public class ClosureParser implements ResponseParser {
    private final Closure parseAction;
    private final ResponseConverter responseConverter;

    public ClosureParser(Closure parseAction, ResponseConverter responseConverter) {
        this.parseAction = parseAction;
        this.responseConverter = responseConverter;
    }

    @Override
    public Result parse(Response response) {
        SdiderResponse sdiderResponse = responseConverter.convert(response);
        ClosureUtils.delegateRun(sdiderResponse, parseAction);
        return sdiderResponse.getResult();
    }
}
