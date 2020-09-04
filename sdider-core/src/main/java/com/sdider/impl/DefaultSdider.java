package sdider.impl;

import com.sdider.crawler.*;
import com.sdider.crawler.common.DynamicPropertiesObject;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.impl.common.DefaultExtensionContainer;
import com.sdider.impl.handler.ClosureExceptionHandler;
import com.sdider.impl.handler.ExceptionHandlerBase;
import com.sdider.impl.log.LogConfigAction;
import com.sdider.impl.parser.ClosureParser;
import com.sdider.impl.parser.DefaultParserContainer;
import com.sdider.impl.pipeline.ClosurePipeline;
import com.sdider.impl.pipeline.ConsolePipeline;
import com.sdider.impl.pipeline.DefaultPipelineContainer;
import com.sdider.impl.request.DefaultRequestContainer;
import com.sdider.impl.response.DefaultResponseConverter;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public class DefaultSdider extends AbstractSdider {
    private Closure beforeCrawl;
    private Closure afterCrawl;
    private final DynamicPropertiesObject<Object> properties = new DefaultDynamicPropertiesObject<>();
    private final ExtensionContainer extensions = new DefaultExtensionContainer();
    private final Configuration configuration = new DefaultConfigurationImpl();
    private final SdiderRequestContainer startRequests = new DefaultRequestContainer();
    private final ResponseConverter responseConverter = new DefaultResponseConverter();
    private final DefaultParserContainer parsers = new DefaultParserContainer();
    private ExceptionHandler exceptionHandler;
    private final PipelineContainer pipelines = new DefaultPipelineContainer();
    private final SdiderScriptExecutor scriptExecutor;
    private Logger logger;
    private final LogConfigAction logConfigAction;
    private String currentScriptName;

    public DefaultSdider(File script) throws IOException {
        this.logConfigAction = new LogConfigAction(script.getName(), script.getName()+".log");
        currentScriptName = script.getName();
        scriptExecutor = new SdiderScriptExecutor(this);
        scriptExecutor.inject(script);
    }
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    @Override
    public void apply(String scriptFilePath) {
        String temp = currentScriptName;
        currentScriptName = scriptFilePath;
        try {
            if (scriptFilePath.startsWith(CLASSPATH_URL_PREFIX)) {
                scriptExecutor.inject(scriptFilePath.substring(CLASSPATH_URL_PREFIX.length()));
            } else {
                scriptExecutor.inject(new File(scriptFilePath));
            }
        } catch (IOException | URISyntaxException e) {
            throw new GroovyRuntimeException(e);
        } finally {
            currentScriptName = temp;
        }
    }

    @Override
    public void properties(Closure propertiesConfig) {
        ClosureUtils.delegateRun(properties, propertiesConfig);
    }

    @Override
    public DynamicPropertiesObject<Object> getProperties() {
        return properties;
    }

    @Override
    public void extensions(@DelegatesTo(ExtensionContainer.class) Closure extensionsConfig) {
        ClosureUtils.delegateRun(extensions, extensionsConfig);
    }

    @Override
    public void configuration(Closure configurationConfig) {
        ClosureUtils.delegateRun(configuration, configurationConfig);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void logger(Closure loggerConfig) {
        logConfigAction.setConfigAction(loggerConfig);
    }

    @Override
    public void logger(String configLocation) {
        logConfigAction.setConfigLocation(URI.create(configLocation));
    }

    @Override
    public Logger getLogger() {
        if (logger == null) {
            logConfigAction.doConfigure();
            logger = LoggerFactory.getLogger("sdider");
        }
        return logger;
    }

    @Override
    public void beforeCrawl(Closure closure) {
        this.beforeCrawl = closure;
    }

    @Override
    public void afterCrawl(Closure closure) {
        this.afterCrawl = closure;
    }

    @Override
    public void startRequests(Closure requestProvideClosure) {
        ClosureUtils.delegateRun(startRequests, requestProvideClosure);
    }

    @Override
    public SdiderRequestContainer getStartRequests() {
        return startRequests;
    }

    @Override
    public void parser(Closure parserAction) {
        ClosureParser parser = new ClosureParser(parserAction, responseConverter);
        parsers.setMainParser(parser);
    }

    @Override
    public void parser(String name, Closure parserAction) {
        ClosureParser parser = new ClosureParser(parserAction, responseConverter);
        parsers.add(name, parser);
    }

    @Override
    public ParserContainer getParsers() {
        return parsers;
    }

    @Override
    public void exceptionHandler(@DelegatesTo(ExceptionHolder.class) Closure exceptionHandlerAction) {
        exceptionHandler = new ClosureExceptionHandler(responseConverter, exceptionHandlerAction);
    }

    @Override
    public void exceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public void pipeline(String name, Closure pipelineAction) {
        Pipeline pipeline = new ClosurePipeline(name, pipelineAction);
        pipelines.add(pipeline);
    }

    @Override
    public PipelineContainer getPipelines() {
        return pipelines;
    }

    @Override
    public ExtensionContainer getExtensions() {
        return extensions;
    }

    @Override
    protected void callBeforeCrawl() {
        if (beforeCrawl != null) {
            ClosureUtils.delegateRun(this, beforeCrawl);
        }
    }

    @Override
    protected void callAfterCrawl() {
        if (afterCrawl != null) {
            ClosureUtils.delegateRun(this, afterCrawl);
        }
    }

    @Override
    protected void beforeExecute() {
        if (getExceptionHandler() == null) {
            exceptionHandler(new ExceptionHandlerBase(responseConverter) {
                @Override
                protected RequestContainer handle(Exception ex, Request request, SdiderResponse response) {
                    getLogger().error("{};request:{}", ex.getMessage(), request, ex);
                    return null;
                }
            });
        }
        if (getPipelines().getEnabledPipelines().isEmpty()) {
            getPipelines().enable(new ConsolePipeline(ConsolePipeline.DEFAULT_CONSOLE_PIPELINE_NAME));
        }
    }
}
