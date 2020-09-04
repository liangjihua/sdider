package sdider.impl.handler;

import com.sdider.ResponseConverter;
import com.sdider.SdiderResponse;
import com.sdider.crawler.ExceptionHandler;
import com.sdider.crawler.Request;
import com.sdider.crawler.RequestContainer;
import com.sdider.crawler.Response;

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
