package com.sdider.crawler.exception;

import com.sdider.crawler.AbstractCrawler;

/**
 * {@link AbstractCrawler}运行过程中发生的异常
 * @author yujiaxin
 */
public class CrawlerExecutionException extends RuntimeException{

    public CrawlerExecutionException() {
    }

    public CrawlerExecutionException(String message) {
        super(message);
    }

    public CrawlerExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrawlerExecutionException(Throwable cause) {
        super(cause);
    }

    public CrawlerExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
