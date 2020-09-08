package com.sdider.crawler.impl.crawler;

import com.sdider.api.Request;
import com.sdider.crawler.AbstractCrawler;

import java.util.concurrent.TimeUnit;

import static com.sdider.util.ObjectUtils.which;


/**
 * CrawlFactoryCommand生产{@link CrawlCommand}并把它们加入{@link ActiveObjectEngine}
 * @author yujiaxin
 */
public class CrawlFactoryCommand implements Command {
    private final ActiveObjectEngine engine;
    private final AbstractCrawler crawler;
    private final SleepCommand sleepCommand;
    public static final String INTERVAL_CONFIG_NAME = "interval";
    public static final String UNIT_CONFIG_NAME = "intervalUnit";

    public CrawlFactoryCommand(ActiveObjectEngine engine, AbstractCrawler crawler) {
        this.engine = engine;
        this.crawler = crawler;
        Config config = new Config();
        crawler.getConfiguration().configure(config);
        sleepCommand = createSleepCommand(engine, config);
    }

    private SleepCommand createSleepCommand(ActiveObjectEngine engine, Config config) {
        final SleepCommand sleepCommand;
        long interval = which(config.getInterval(), 5L);
        TimeUnit intervalUnit = which(config.getIntervalUnit(), TimeUnit.SECONDS);
        sleepCommand = new SleepCommand(engine, this, interval, intervalUnit);
        return sleepCommand;
    }

    public void run() {
        if (!engine.isRunning()) {
            return;
        }
        int restNum = this.engine.getMaxSize() - engine.getCurrentSize() - 1;//自己待会还要加进去
        if (restNum > 0) {
            final Request request = crawler.popRequest();
            if (request != null) {
                engine.add(create(request));
                sleep();
                return;
            }
        }
        await();
    }

    private void sleep() {
        engine.add(sleepCommand);
    }


    private void await() {
        if (!engine.isEmpty()) {
            engine.add(this);
        }
    }

    private Command create(Request request) {
        return new CrawlCommand(engine, crawler, request);
    }

    public static class Config {
        private Long interval;
        private TimeUnit intervalUnit;

        public Long getInterval() {
            return interval;
        }

        public void setInterval(Long interval) {
            this.interval = interval;
        }

        public TimeUnit getIntervalUnit() {
            return intervalUnit;
        }

        public void setIntervalUnit(TimeUnit intervalUnit) {
            this.intervalUnit = intervalUnit;
        }
    }
}
