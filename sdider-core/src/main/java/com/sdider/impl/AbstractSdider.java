package sdider.impl;

import com.sdider.ExtensionContainer;
import com.sdider.Sdider;
import com.sdider.crawler.Crawler;
import com.sdider.crawler.CrawlerFactory;
import com.sdider.crawler.common.DynamicPropertiesObject;
import com.sdider.impl.exception.SdiderExecuteException;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;
import org.apache.logging.log4j.LogManager;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yujiaxin
 */
public abstract class AbstractSdider extends GroovyObjectSupport implements Sdider {
    private final AtomicBoolean running = new AtomicBoolean(true);

    protected abstract void callBeforeCrawl();

    protected abstract void callAfterCrawl();

    public void execute() {
        beforeExecute();
        //noinspection rawtypes
        Closure pipelines = (Closure) getConfiguration().get("pipelines");
        if (pipelines != null) {
            ClosureUtils.delegateRun(getPipelines(), pipelines);
        }
        checkConditions();
        Crawler crawler = createCrawler();
        crawler.startRequests(getStartRequests().getRequests());
        running.set(true);
        addShutdownHook(crawler);
        try {
            callBeforeCrawl();
            crawler.run();
            callAfterCrawl();
        } catch (Exception exception) {
            getLogger().error(exception.getMessage(), exception);
        } finally {
            afterExecute();
            LogManager.shutdown();
            running.set(false);
            synchronized (running) {
                running.notifyAll();
            }
        }
    }

    private void checkConditions() {
        if (getParsers().getMainParser() == null) {
            throw new SdiderExecuteException("the main parser not set");
        }
        if (getExceptionHandler() == null) {
            throw new SdiderExecuteException("exception handler not set");
        }
        if (getPipelines().getEnabledPipelines().isEmpty()) {
            throw new SdiderExecuteException("pipelines is empty");
        }
    }

    protected Crawler createCrawler() {
        CrawlerFactory factory = findCrawlerFactory();
        getLogger().debug("使用CrawlerFactory:{}", factory.getClass().getName());
        return factory.create(getParsers(), getPipelines().getEnabledPipelines(), getExceptionHandler(), getConfiguration());
    }

    protected CrawlerFactory findCrawlerFactory() {
        ServiceLoader<CrawlerFactory> factories = ServiceLoader.load(CrawlerFactory.class);
        String factoryClass = (String) getConfiguration().get("crawlerFactoryClass");
        getLogger().debug("configuration.crawlerFactoryClass={}", factoryClass);
        CrawlerFactory crawlerFactory = null;
        Iterator<CrawlerFactory> iterator = factories.iterator();
        while (iterator.hasNext()) {
            CrawlerFactory factory = iterator.next();
            if (crawlerFactory == null) {
                crawlerFactory = factory;
            }
            if (factoryClass != null) {
                if (Objects.equals(factory.getClass().getName(), factoryClass)) {
                    crawlerFactory = factory;
                    break;
                } else if (!iterator.hasNext()) {
                    getLogger().warn("未找到crawlerFactoryClass:{}", factoryClass);
                }
            } else {
                break;
            }
        }
        if (crawlerFactory == null) {
            throw new SdiderExecuteException("CrawlerFactory未提供");
        }
        return crawlerFactory;
    }

    private void addShutdownHook(Crawler crawler) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            crawler.shutdown();
            while (running.get()) {
                synchronized (running) {
                    try {
                        running.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }));
    }

    @Override
    public Object propertyMissing(String name) {
        ExtensionContainer extensions = getExtensions();
        if (extensions.contains(name)) {
            return extensions.getByName(name);
        }
        DynamicPropertiesObject<Object> properties = getProperties();
        if (properties.has(name)) {
            return properties.get(name);
        }
        throw new MissingPropertyException(name, this.getClass());
    }

    /**
     * override this to do something before execute
     */
    protected void beforeExecute() {}

    /**
     * override this to do something after execute
     */
    protected void afterExecute() {}
}
