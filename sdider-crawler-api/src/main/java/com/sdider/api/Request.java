package com.sdider.api;

import com.sdider.api.common.DynamicPropertiesObject;

import java.util.Map;

/**
 * Request是http请求的抽象数据结构，包含了发起一个http请求所必须的信息。
 * 同时Request还可以动态的扩展额外的属性信息。
 * @see DynamicPropertiesObject
 * @author yujiaxin
 */
public interface Request extends DynamicPropertiesObject<Object> {

    /**
     * 返回请求method，不可空
     * @return method
     */
    String getMethod();

    /**
     * 返回完整的请求url，不可空
     * @return url
     */
    String getUrl();

    /**
     * 返回请求头
     * @return map of request headers
     */
    Map<String, String> getHeaders();

    /**
     * 返回请求参数。
     * @return map of request params
     */
    Map<String, String> getParams();

    String getBody();

    String getContentType();

    /**
     * 返回请求代理，请求代理由<b>主机地址+端口号</b>组成。eg：127.0.0.1:1080
     * @return 请求代理
     */
    String getProxy();

//    /**
//     * 返回关联的parser，可能返回null
//     * @return 关联的parser，可能为null
//     */
//    ResponseParser getParser();

//    /**
//     * 设置关联的parser
//     */
//    void setParser(ResponseParser parser);
}
