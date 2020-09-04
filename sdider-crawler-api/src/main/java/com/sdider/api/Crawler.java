package com.sdider.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Crawler是一个抽象爬虫。Sdider通过Crawler来实现爬虫功能。Crawler包含预定义组件：
 * {@link ResponseParser}、{@link Pipeline}、{@link ExceptionHandler}，
 * 它们由Sdider创建，Sdider依赖它们完成基本的爬虫功能：下载内容的处理、异常处理，
 * Crawler的实现需要回调这些组件来完成爬虫的功能。{@link Configuration}组件
 * 同样由Sdider创建，用于从Sdider向Crawler传递配置参数。<br/>
 * 实现Crawler主要需要实现的是{@link Request}s的调度和下载，请求下载完成后Crawler
 * 的实现需要调用{@link ResponseParser}组件来完成解析，然后消费解析结果并且调度新
 * 生成的请求。<br/>
 * Crawler预实现了下载内容处理的方法{@link #processResponse(Response)}，
 * 和异常处理方法{@link #handleException(Exception, Request, Response)}，
 * Crawler的实现者可以直接使用这些方法。<br/>
 * Sdider通过SPI机制创建Crawler的实现，因此实现Crawler的同时需要实现{@link CrawlerFactory}
 * @see CrawlerFactory
 * @see ResponseParser
 * @see Pipeline
 * @see ExceptionHandler
 * @see Configuration
 * @author yujiaxin
 */
public abstract class Crawler {
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    protected final ResponseParser parser;
    protected final List<Pipeline> pipelines;
    protected final ExceptionHandler exceptionHandler;
    protected final Configuration configuration;

    public Crawler(ResponseParser parser,
                   List<Pipeline> pipeline,
                   ExceptionHandler exceptionHandler,
                   Configuration configuration) {
        this.parser = parser;
        this.pipelines = Collections.unmodifiableList(pipeline);
        this.exceptionHandler = exceptionHandler;
        this.configuration = configuration;
    }

    /**
     * 下载一个请求
     * @param request 请求
     * @return 下载结果
     */
    public abstract Response download(Request request) throws Exception;


    /**
     * 处理下载结果。请求下载成功后调用此方法处理下载内容，提取数据项目以及生成
     * 新的下载请求。
     * @param response 下载结果
     */
    protected void processResponse(Response response) {
        try {
            Result result = parser.parse(response);
            result.consume(pipelines);
            schedule(result.getRequests());
        } catch (Exception exception) {
            handleException(exception, response.getRequest(), response);
        }
    }

    /**
     * 处理异常，爬取中任何阶段的异常都应该调用此方法处理。
     * @param exception 异常对象
     * @param request 发生异常的请求
     * @param response 请求响应，可以为null
     */
    protected void handleException(Exception exception, Request request, Response response) {
        try {
            RequestContainer result = exceptionHandler.handleException(exception, request, response);
            if (result != null) {
                schedule(result.getRequests());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 添加一些起始请求
     * @param requests 起始请求
     */
    public abstract void startRequests(List<Request> requests);

    /**
     * 调度下载请求
     * @param requests 下载请求列表
     */
    protected abstract void schedule(List<Request> requests);

    /**
     * 运行一个爬虫，没有请求可供爬取时或{@link #shutdown()}被调用时返回。
     */
    public abstract void run();

    /**
     * 停止此Crawler运行。方法立即返回。
     */
    public abstract void shutdown();
}
