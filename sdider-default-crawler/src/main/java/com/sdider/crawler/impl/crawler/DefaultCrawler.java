package com.sdider.crawler.impl.crawler;

import com.sdider.api.Configuration;
import com.sdider.api.ExceptionHandler;
import com.sdider.api.Pipeline;
import com.sdider.api.ResponseParser;
import com.sdider.crawler.AbstractCrawler;
import com.sdider.crawler.common.ArrayQueue;
import com.sdider.crawler.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.sdider.crawler.util.ObjectUtils.which;


/**
 * @author yujiaxin
 */
public final class DefaultCrawler extends AbstractCrawler implements ActiveObjectEngine {
    private static final Logger logger = LoggerFactory.getLogger(DefaultCrawler.class);
    private ArrayQueue<Command> coreQueue;
    private int maxCoreSize;
    private int concurrentRequests;

    public DefaultCrawler(ResponseParser parser,
                          List<Pipeline> pipeline,
                          ExceptionHandler exceptionHandler,
                          Configuration configuration) {
        super(parser, pipeline, exceptionHandler, configuration);
        int concurrentRequests = which((Integer) configuration.get("concurrentRequests"), 2);
        init(concurrentRequests);
    }

    private void init(int concurrentRequests) {//concurrent
        this.concurrentRequests = concurrentRequests;
        maxCoreSize = concurrentRequests + 1;
        coreQueue = new ArrayQueue<>(maxCoreSize);
    }

    @Override
    public void crawl() {
        add(new CrawlFactoryCommand(this, this));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            while (!coreQueue.isEmpty()) {
                System.out.printf("剩余[%s]个任务%n", coreQueue.getSize());
                try {
                    //noinspection BusyWait
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        try {
            while (!coreQueue.isEmpty()) {
                Command command = coreQueue.pop();
                try {
                    command.run();
                } catch (Throwable e) {
                    logger.error("任务运行异常：{}", command.toString(), e);
                }
                //noinspection BusyWait
                Thread.sleep(1);
            }
        } catch (InterruptedException ignored) {
            //sleep(1)理论上没有被interrupt的可能
        }
    }

    @Override
    public void add(Command command) {
        Assert.notNull(command, "command");
        if (isCoreQueueFull()) {
            throw new IllegalStateException("Engine队列已满");
        }
        coreQueue.add(command);
    }

    private boolean isCoreQueueFull() {
        if (isRunning()) {
            return coreQueue.getSize() >= maxCoreSize;
        }
        return coreQueue.getSize() >= concurrentRequests;
    }

    @Override
    public boolean isEmpty() {
        return coreQueue.isEmpty();
    }

    @Override
    public int getMaxSize() {
        return maxCoreSize;
    }

    @Override
    public int getCurrentSize() {
        return coreQueue.getSize();
    }
}
