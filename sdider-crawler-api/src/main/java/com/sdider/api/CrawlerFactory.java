package com.sdider.api;

import java.util.List;

/**
 * CrawlerFactory从给定参数返回一个{@link Crawler}的实例。
 * @author yujiaxin
 */
public interface CrawlerFactory {

    Crawler create(ResponseParser parser, List<Pipeline> pipelines,
                                   ExceptionHandler handler, Configuration configuration);

}
