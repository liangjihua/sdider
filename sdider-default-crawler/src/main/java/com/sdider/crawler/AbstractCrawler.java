package com.sdider.crawler;

import com.sdider.api.*;
import com.sdider.crawler.exception.CrawlerExecutionException;
import com.sdider.crawler.impl.downloader.DefaultDownloader;
import com.sdider.crawler.impl.scheduler.DefaultScheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.sdider.util.ObjectUtils.or;

/**
 * Crawler是一个同步运行的爬虫，它包含了自身运行所需要的所有信息。
 * @author yujiaxin
 */
public abstract class AbstractCrawler extends Crawler {
    private final AtomicReference<Status> status = new AtomicReference<>(Status.STOPPED);
    private final Downloader downloader;
    private Scheduler schedulerCopy;
    private volatile Scheduler scheduler;
    private List<Request> startRequests;
    private final Config config;

    /**
     * 添加一些起始请求。起始请求并不是必须的，通常Crawler只需要scheduler中
     * 存有待爬取的{@link Request}即可工作。
     */
    public AbstractCrawler(ResponseParser parser,
                           List<Pipeline> pipeline,
                           ExceptionHandler exceptionHandler,
                           Configuration configuration) {
        super(parser, pipeline, exceptionHandler, configuration);
        config = new Config();
        configuration.configure(config);
        downloader = or(config.getDownloader(), DefaultDownloader::new);
        downloader.start();
        scheduler = or(config.getScheduler(), ()->new DefaultScheduler(new LinkedList<>()));
    }

    public Request popRequest() {
        return scheduler.pop();
    }

    public void download(Request request, DownloadCallback downloadCallback) {
        downloader.download(request, downloadCallback);
    }

    @Override
    public Response download(Request request) throws Exception {
        AtomicReference<Object> resultReference = new AtomicReference<>();
        downloader.download(request, new DownloadCallback() {
            @Override
            public void succeeded(Response response) {
                resultReference.set(response);
                synchronized (resultReference) {
                    resultReference.notifyAll();
                }
            }

            @Override
            public void failed(Exception ex) {
                resultReference.set(ex);
                synchronized (resultReference) {
                    resultReference.notifyAll();
                }
            }
        });
        if (resultReference.get() == null) {//用于辅助测试，实际情况下如果是异步执行，应该不会发生不等于null的情况。
            synchronized (resultReference) {
                resultReference.wait();
            }
        }
        if (resultReference.get() instanceof Response) {
            return (Response) resultReference.get();
        }
        throw (Exception) resultReference.get();
    }

    private void ensureRunning() {
        if (isStopped()) {
            throw new CrawlerExecutionException("Crawler 未运行");
        }
    }

    public void processResponse(Response response) {
        ensureRunning();
        super.processResponse(response);
    }

    /**
     * 给定请求集合添加到待爬取队列中
     * @param requests 请求队列
     */
    @Override
    protected void schedule(List<Request> requests) {
        ensureRunning();
        if (requests == null || requests.isEmpty()) return;
        for (Request request : requests) {
            scheduler.push(request);
        }
    }

    /**
     * 给定{@link RequestContainer}添加到待爬取队列中
     * @param requestContainer requestContainer
     */
    protected void schedule(RequestContainer requestContainer) {
        if (requestContainer == null) return;
        schedule(requestContainer.getRequests());
    }

    /**
     * @implSpec 起始请求并不是必须的，通常Crawler只需要{@link #scheduler}中
     * 存有待爬取的{@link Request}即可工作。
     * @param requests 起始请求
     */
    @Override
    public void startRequests(List<Request> requests) {
        if (requests != null) {
            if (startRequests == null) {
                startRequests = new ArrayList<>(requests.size());
            }
            startRequests.addAll(requests);
        }
    }

    public void handleException(Exception ex, Request request, Response response) {
        ensureRunning();
        super.handleException(ex, request, response);
    }

    /**
     * 爬取请求。方法调用后会持续运行至{@link Scheduler}中没有任何可爬取的{@link Request}后终止并返回。
     * 客户端也可以调用{@link #shutdown()}来提前终止Crawler，提前终止的Crawler可能不会爬取完
     * {@link Scheduler}中的所有{@link Request}s，因此使用{@link #shutdown()}提前终止的客户端需要
     * 自行处理{@link Scheduler}中剩余的{@link Request}
     */
    public void run() {
        if (!status.compareAndSet(Status.STOPPED, Status.RUNNING)) {
            throw new CrawlerExecutionException("Crawler已在运行中");
        }
        schedule(startRequests);
        schedulerCopy = scheduler;
        try {
            crawl();
        } catch (Exception ex) {
            throw new CrawlerExecutionException(ex);
        }finally {
            status.set(Status.STOPPED);
            scheduler = schedulerCopy;
            schedulerCopy = null;
            downloader.stop();
            if (config.getSchedulerDump() != null) {
                config.getSchedulerDump().accept(scheduler);
            }
        }
    }

    /**
     * 启动爬虫，这个方法的实现应该阻塞运行，直到当前Crawler没有任何正在处理中的{@link Request}，
     * 并且{@link #scheduler}内也不包含任何{@link Request}<br/>
     */
    protected abstract void crawl();

    /**
     * 关闭此Crawler。方法立即返回，但此时未必已经完全关闭，完全关闭取决于当前
     * 已发出的请求何时执行完毕。已发出请求的响应抽取的新的{@link Request}
     * 依然会进入{@link Scheduler}，但是不再调度。
     */
    public void shutdown() {
        if (status.compareAndSet(Status.RUNNING, Status.SHUTDOWN)) {
            //scheduler中通常会有大量的request，等待它们全部运行是不现实的，只需要等待已发出的request就行
            scheduler = new Scheduler() {
                @Override
                public void push(Request request) {
                    schedulerCopy.push(request);
                }

                @Override
                public Request pop() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return true;
                }
            };
        }
    }

    public boolean isStopped() {
        return status.get() == Status.STOPPED;
    }

    public boolean isRunning() {
        return status.get() == Status.RUNNING;
    }

    enum Status {
        STOPPED, RUNNING, SHUTDOWN
    }

    public static class Config {
        private Downloader downloader;
        private Scheduler scheduler;
        private Consumer<Scheduler> schedulerDump;

        public Downloader getDownloader() {
            return downloader;
        }

        public void setDownloader(Downloader downloader) {
            this.downloader = downloader;
        }

        public Scheduler getScheduler() {
            return scheduler;
        }

        public void setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        public Consumer<Scheduler> getSchedulerDump() {
            return schedulerDump;
        }

        public void setSchedulerDump(Consumer<Scheduler> schedulerDump) {
            this.schedulerDump = schedulerDump;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
