package com.sdider;

import com.sdider.api.Configuration;
import com.sdider.api.ExceptionHandler;
import com.sdider.api.Pipeline;
import com.sdider.api.common.DynamicPropertiesObject;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;

/**
 * Sdider是爬虫脚本的基本模型。爬虫脚本的所有公开操作都由此模型提供。
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public interface Sdider extends GroovyObject, Extensible {

    /**
     * 应用执行给定脚本，脚本内的一切操作将委托给此对象执行
     * @param scriptFilePath 脚本文件路径
     */
    void apply(String scriptFilePath);

    /**
     * 设置动态属性。给定的闭包将会被代理给动态属性容器执行，用于添加/修改sdider的动态属性<br/>
     * sdider对象的动态属性不能删除，可以在脚本的任意地方访问，但是不能任意修改，只有此方法或者直接操作动态属性
     * 容器才可以修改动态属性，这样做是为了限制全局的动态属性使用的方式和目的。建议使用动态属性来定义
     * 脚本内使用的全局变量或配置属性。
     * @param propertiesConfig 动态属性配置闭包
     */
    void properties(@DelegatesTo(DynamicPropertiesObject.class) Closure propertiesConfig);

    /**
     * 返回动态属性容器。
     * @return 动态属性容器。
     */
    DynamicPropertiesObject<Object> getProperties();

    /**
     * 设置扩展对象。给定的闭包将会被代理给扩展容器执行，用于添加扩展
     * @param extensionsConfig 扩展配置闭包
     */
    void extensions(@DelegatesTo(ExtensionContainer.class) Closure extensionsConfig);

    /**
     * 配置Sdider。给定闭包将会被代理给配置容器{@link Configuration}执行，填充Sdider的配置<br/>
     * {@link Configuration}是Sdider或Sdider的爬虫实现内部预定义的配置项，这些配置项会被Sdider
     * 或爬虫实现内部使用。{@link Configuration}也是Sdider脚本与Sdider爬虫实现沟通的唯一方式。
     * @param configurationConfig 用于配置Sdider的闭包
     */
    void configuration(@DelegatesTo(Configuration.class) Closure configurationConfig);

    /**
     * 返回配置容器
     * @return 配置容器
     */
    Configuration getConfiguration();

    /**
     * 配置logger
     * @param loggerConfig logger配置闭包
     */
    void logger(Closure loggerConfig);

    /**
     * 设置日志配置文件，目前仅支持log4j2的配置文件。给定的日志配置将会
     * 替换默认的日志配置。
     * @param configLocation 日志配置文件路径
     */
    void logger(String configLocation);

    /**
     * 返回logger对象。该logger配置取决于{@link #logger(Closure)}和{@link #logger(String)}
     * 的设置，未配置logger时将使用默认配置。注意，如果使用了{@link #logger(Closure)}或{@link #logger(String)}
     * 来覆盖默认配置，必须在调用此方法之前调用，否则它们将不会生效，这是由于log4j2框架的初始化机制决定的。
     * @return logger
     */
    Logger getLogger();

    /**
     * 配置爬虫开始爬取前钩子
     * @param closure 爬取前钩子
     */
    void beforeCrawl(Closure closure);

    /**
     * 配置爬虫爬取完成钩子
     * @param closure 爬取完成钩子
     */
    void afterCrawl(Closure closure);

    /**
     * 配置开始请求。给定闭包代理给{@link SdiderRequestContainer}，用于生成开始请求
     * @param requestProvideClosure 用于生成开始请求的闭包
     */
    void startRequests(@DelegatesTo(SdiderRequestContainer.class) Closure requestProvideClosure);

    /**
     * 返回一个列表，包含所有添加的开始请求。修改返回列表不会影响到已添加的请求。
     * @return 开始请求
     */
    SdiderRequestContainer getStartRequests();

    /**
     * 配置Main Parser。Main Parser是默认的parser，当请求未指定parser时使用Main Parser。
     * @param parserAction 用于解析请求响应的闭包
     */
    void parser(Closure parserAction);

    /**
     * 添加一个命名的parser
     * @param name parser name
     * @param parserAction 用于解析请求响应的闭包
     */
    void parser(String name, Closure parserAction);

    /**
     * 返回parser容器
     * @return parser容器
     */
    ParserContainer getParsers();

    /**
     * 配置爬取异常处理器。给定的闭包将由于处理爬取异常
     * @param exceptionHandlerAction 用于爬取异常的闭包
     */
    void exceptionHandler(@DelegatesTo(ExceptionHolder.class) Closure exceptionHandlerAction);

    /**
     * 配置爬取异常处理器。
     * @param exceptionHandler 爬取异常处理器
     */
    void exceptionHandler(ExceptionHandler exceptionHandler);

    /**
     * 返回已配置的爬取异常处理器
     * @return 已配置的爬取异常处理器或null
     */
    ExceptionHandler getExceptionHandler();

    /**
     * 添加一个命名的{@link Pipeline}至pipeline容器。
     * 给定的闭包用作新建{@link Pipeline}的处理动作。
     * @param name pipeline name
     * @param pipelineAction pipeline action
     */
    void pipeline(String name, @DelegatesTo(SdiderItem.class) Closure pipelineAction);

    /**
     * 返回pipeline容器
     * @return pipeline容器
     */
    PipelineContainer getPipelines();
}
