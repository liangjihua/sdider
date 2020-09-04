package sdider;

import com.sdider.crawler.Response;

/**
 * ResponseConverter将一个{@link Response}转换为{@link SdiderResponse}
 * @author yujiaxin
 */
public interface ResponseConverter {

    SdiderResponse convert(Response response);
}
