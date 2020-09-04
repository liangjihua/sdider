package com.sdider.crawler.impl.crawler;

import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.crawler.AbstractCrawler;
import com.sdider.crawler.DownloadCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CrawlCommand发出Http请求并解析响应。
 * @author yujiaxin
 */
class CrawlCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CrawlCommand.class);
    private final ActiveObjectEngine engine;
    private final Request request;
    private final AbstractCrawler crawler;
    private boolean start = false;
    private volatile boolean done = false;
    private volatile Response response;
    private volatile Exception exception;

    public CrawlCommand(ActiveObjectEngine engine, AbstractCrawler crawler, Request request) {
        this.engine = engine;
        this.request = request;
        this.crawler = crawler;
    }
    @Override
    public void run() {
        try {
            if (!start) {
                start();
            }
            await();
        } catch (Exception e) {
            crawler.handleException(e, request, response);
        }
    }

    private void start() {
        crawler.download(request, new DownloadCallback() {
            @Override
            public void succeeded(Response res) {
                done = true;
                response = res;
            }

            @Override
            public void failed(Exception ex) {
                done = true;
                exception = ex;
            }
        });
        logger.debug("发送异步请求，请求信息；{}", request.toString());
        start = true;
    }

    private void await() throws Exception {
        if (!done) {
            engine.add(this);
        } else {
            if (exception != null) {
                throw exception;
            }
            crawler.processResponse(response);
        }
    }

    public boolean isStart() {
        return start;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "CrawlCommand{" + "request=" + request +
                ", start=" + start +
                ", isDone=" + done +
                '}';
    }
}
