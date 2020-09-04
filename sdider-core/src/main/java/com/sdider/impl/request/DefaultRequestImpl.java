package com.sdider.impl.request;

import com.sdider.RequestConfig;
import com.sdider.SdiderRequest;
import com.sdider.api.exception.NoSuchPropertyException;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;

import java.util.Map;

/**
 *
 * @author yujiaxin
 */
public class DefaultRequestImpl extends GroovyObjectSupport implements SdiderRequest {
    private String url;
    private String method;
    private final RequestConfig requestConfig = new DefaultRequestConfigImpl();
    private String body;
    private final DefaultDynamicPropertiesObject<String> params = new DefaultDynamicPropertiesObject<>();
    private final DefaultDynamicPropertiesObject<Object> properties = new DefaultDynamicPropertiesObject<>();

    public DefaultRequestImpl() {
        DefaultRequestConfigImpl defaultRequestConfig = DefaultRequestConfigImpl.GLOBAL_REQUEST_CONFIG;
        //todo 这个样子会有一个问题：sdider脚本中，用户自定义request类型无法得到这种全局request config的覆盖（除非用户手动调用一次）
        //todo 结合DefaultConfigurationImpl中requests的实现去考虑，应该如何实现全局config的覆盖
        //todo 同时还要考虑用户使用的问题，用户在脚本中会打印多次request.toString()，最好能让用户在任意时刻打印都能得到已经合并过全局config的request
        //todo 应该尽量的延迟合并的时机，这样可以最大程度的保留全局config的灵活性，不至于被时序影响（全局request config必须在request之前配置）
        requestConfig.setHeaders(defaultRequestConfig.getHeaders());
        requestConfig.proxy(defaultRequestConfig.getProxy());
    }

    @Override
    public void contentType(String contentType) {
        requestConfig.contentType(contentType);
    }

    @Override
    public String getContentType() {
        return requestConfig.getContentType();
    }

    @Override
    public void headers(Closure<?> headersSetClosure) {
        requestConfig.headers(headersSetClosure);
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        requestConfig.setHeaders(headers);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getHeaders() {
        return requestConfig.getHeaders();
    }

    @Override
    public Map<String, String> getParams() {
        return params.getProperties();
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void userAgent(String userAgent) {
        requestConfig.userAgent(userAgent);
    }

    @Override
    public String getUserAgent() {
        return requestConfig.getUserAgent();
    }

    @Override
    public void proxy(String proxy) {
        requestConfig.proxy(proxy);
    }

    @Override
    public String getProxy() {
        return requestConfig.getProxy();
    }

    @Override
    public void method(String method) {
        this.method = method;
    }

    @Override
    public void url(String url) {
        this.url = url;
    }

    @Override
    public void params(Closure<?> closureToConfigParams) {
        ClosureUtils.delegateRun(params, closureToConfigParams);
    }

    @Override
    public void body(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultRequestImpl{");
        sb.append("url='").append(url).append('\'');
        sb.append(", method='").append(method).append('\'');
        sb.append(", proxy='").append(getProxy()).append('\'');
        requestConfig.getHeaders().forEach((k, v) -> sb.append(", ").append(k).append("='").append(v).append('\''));
        sb.append(", body='").append(body).append('\'');
        sb.append(", params=").append(params);
        properties.getProperties().forEach((k, v) ->sb.append(", ").append(k).append("='").append(v));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean has(String name) {
        return properties.has(name);
    }

    @Override
    public Object get(String name) {
        return properties.get(name);
    }

    @Override
    public Object getProperty(String propertyName) {
        switch(propertyName) {
            case "url":
                return getUrl();
            case "method":
                return getMethod();
            case "contentType":
                return getContentType();
            case "headers":
                return getHeaders();
            case "body":
                return getBody();
            case "params":
                return getParams();
            case "userAgent":
                return getUserAgent();
            case "proxy":
                return getProxy();
            case "properties":
                return getProperties();
            default:
                try {
                    return get(propertyName);
                } catch (NoSuchPropertyException e) {
                    throw new MissingPropertyException(propertyName, this.getClass());
                }
        }
    }

    @Override
    public void set(String name, Object value) {
        properties.set(name, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties.getProperties();
    }

    public Object methodMissing(String name, Object args) {
        return properties.methodMissing(name, args);
    }
}
