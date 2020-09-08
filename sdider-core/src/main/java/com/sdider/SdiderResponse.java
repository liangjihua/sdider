package com.sdider;

import com.sdider.api.Response;

/**
 * SdiderResponse是一个聚合接口
 * @author yujiaxin
 */
public interface SdiderResponse extends Response, Extensible, ResultCollector{

    String getContentType();

    String getCharset();

    String getText();
}
