package com.sdider;

import com.sdider.api.Request;
import com.sdider.api.common.DynamicPropertiesObject;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;


/**
 * 一个SdiderRequest代表一个http请求。在sdider脚本中创建一个请求：
 * <pre>
 * request {
 *     GET 'http://foo.bar'
 *     headers {
 *         Host 'foo.bar'
 *     }
 *     params {
 *         foo 'bar'
 *     }
 * }
 * </pre>
 * @author yujiaxin
 */
public interface SdiderRequest extends Request, RequestConfig {
    String METHOD_GET = "GET";
    String METHOD_POST = "POST";

    default void GET(String url) {
        method(METHOD_GET);
        url(url);
    }

    default void POST(String url) {
        method(METHOD_POST);
        url(url);
    }

    void method(String method);

    void url(String url);

    /**
     * 设置请求参数，给定闭包将会被<b>delegate</b>给一个{@link DynamicPropertiesObject}
     * 用于设置请求参数<br/>
     * 请求参数的附加方式是视情况而定，如果此Request是POST/PUT请求并且没有设置请求体，
     * 那么请求参数将被附加到请求体（使用x-www-form-urlencoded）;否则请求参数会附加到queryString
     * @param closureToConfigParams 用于设置请求参数的闭包
     */
    void params(@DelegatesTo(DynamicPropertiesObject.class) Closure<?> closureToConfigParams);

    void body(String body);

    default void parser(Object parser) {
        set(ParserContainer.PARSER_KEY, parser);
    }
}
