package com.sdider.impl;

import com.sdider.*;
import com.sdider.api.*;
import com.sdider.api.common.DynamicPropertiesObject;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.impl.common.DefaultExtensionContainer;
import com.sdider.impl.exception.SdiderExecuteException;
import com.sdider.impl.handler.ClosureExceptionHandler;
import com.sdider.impl.handler.ExceptionHandlerBase;
import com.sdider.impl.log.LogConfiguration;
import com.sdider.impl.log.Logger;
import com.sdider.impl.parser.ClosureParser;
import com.sdider.impl.parser.DefaultParserContainer;
import com.sdider.impl.pipeline.ClosurePipeline;
import com.sdider.impl.pipeline.ConsolePipeline;
import com.sdider.impl.pipeline.DefaultPipelineContainer;
import com.sdider.impl.request.DefaultRequestConfigImpl;
import com.sdider.impl.request.DefaultRequestContainer;
import com.sdider.impl.response.DefaultResponseConverter;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.apache.logging.log4j.LogManager;

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
    private final DynamicPropertiesObject<Object> properties = new DefaultDynamicPropertiesObject<>();
    private final ExtensionContainer extensions = new DefaultExtensionContainer();
    private final Configuration configuration = new DefaultConfigurationImpl();
    private final DefaultRequestFactory requestFactory = new DefaultRequestFactory();
    private final SdiderRequestContainer startRequests = new DefaultRequestContainer(requestFactory);
    private final ResponseConverter responseConverter = new DefaultResponseConverter(requestFactory);
    private final DefaultParserContainer parsers = new DefaultParserContainer();
    private final PipelineContainer pipelines = new DefaultPipelineContainer();
    private final LogConfiguration.Config logConfig = LogConfiguration.createConfig();
    private Closure beforeCrawl;
    private Closure afterCrawl;
    private ExceptionHandler exceptionHandler;
    private final SdiderScriptExecutor scriptExecutor;
    private Logger scriptLogger;
    private String currentScriptName;

    public DefaultSdider(File script) throws IOException {
        logConfig.setConfigName(script.getName());
        logConfig.setFileName(script.getName() + ".log");
        currentScriptName = script.getName();
        scriptExecutor = new SdiderScriptExecutor(this);//todo 在对象构造中发布该对象是一个隐患
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
            throw new SdiderExecuteException(e);
        } finally {
            currentScriptName = temp;
        }
    }

    public LogConfiguration.Config getLogConfig() {
        return logConfig;
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
        if (configuration.has("requests")) {
            Closure requests = (Closure) configuration.get("requests");
            if (requests != null) {
                RequestConfig requestConfig = new DefaultRequestConfigImpl();
                ClosureUtils.delegateRun(requestConfig, requests);
                requestFactory.setRequestConfig(requestConfig);
                configuration.set("requests", null);
            }
        }
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void logger(Closure loggerConfig) {
        ClosureUtils.delegateRun(logConfig, loggerConfig);
    }

    @Override
    public void logger(String configLocation) {
        logConfig.setConfigLocation(URI.create(configLocation));
    }

    @Override
    public org.slf4j.Logger getLogger() {
        if (scriptLogger == null) {
            LogConfiguration.configure(logConfig);
            scriptLogger = Logger.getInstance("sdider");
        }
        return scriptLogger;
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
        exceptionHandler = new ClosureExceptionHandler(responseConverter,requestFactory, exceptionHandlerAction);
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
        getLogger();//trigger log init
        if (getExceptionHandler() == null) {
            exceptionHandler(new ExceptionHandlerBase(responseConverter) {
                @Override
                protected RequestContainer handle(Exception ex, Request request, SdiderResponse response) {
                    getLogger().error("{};request:{}", ex.getMessage(), request, ex);
                    return null;
                }
            });
        }
        if (configuration.has("pipelines")){
            Closure pipelines = (Closure) configuration.get("pipelines");
            if (pipelines != null) {
                ClosureUtils.delegateRun(getPipelines(), pipelines);
            }
        }
        if (getPipelines().getEnabledPipelines().isEmpty()) {
            getPipelines().enable(new ConsolePipeline());
        }
    }

    @Override
    protected void afterExecute() {
        LogManager.shutdown();
    }
}
