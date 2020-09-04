package com.sdider.crawler.impl.crawler;

import com.sdider.api.*;

import java.util.List;

/**
 *
 * @author yujiaxin
 */
public class DefaultCrawlerFactory implements CrawlerFactory {

    @Override
    public Crawler create(ResponseParser parser, List<Pipeline> pipelines, ExceptionHandler handler, Configuration configuration) {
        return new DefaultCrawler(parser, pipelines, handler, configuration);
    }
}
