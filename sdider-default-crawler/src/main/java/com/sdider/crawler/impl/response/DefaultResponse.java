package com.sdider.crawler.impl.response;

import com.sdider.api.Request;
import com.sdider.api.Response;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Response的默认实现
 * @author yujiaxin
 */
public class DefaultResponse implements Response {
    /**
     * 原始的请求
     */
    private final Request request;
    /**
     * 响应头，按照该请求响应内容的原始顺序排列
     */
    private Map<String, String> headers;
    /**
     * 响应体，字节数组表示
     */
    private final byte[] body;
    /**
     * http 响应代码
     */
    private final int statusCode;

    private final String reasonPhrase;
    /**
     * 响应体，字符串表示
     */
    private volatile String text;

    private final String contentType;
    private final Charset charset;

    public DefaultResponse(Request request, SimpleHttpResponse response) {
        this.request = request;
        if (response.getHeaders() != null) {
            headers = new TreeMap<>();
            for (Header header : response.getHeaders()) {
                headers.put(header.getName(), header.getValue());
            }
        }
        Charset tempCharset = null;
        ContentType contentType = response.getContentType();
        if (contentType != null) {
            tempCharset = contentType.getCharset();
            this.contentType = contentType.toString();
        } else {
            this.contentType = null;
        }
        if (tempCharset == null){
            tempCharset = StandardCharsets.UTF_8;
        }
        charset = tempCharset;
        statusCode = response.getCode();
        reasonPhrase = response.getReasonPhrase();
        body = response.getBodyBytes();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    public String getText() {
        if (text == null && body != null) {
            text = new String(body, charset);
        }
        return text;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers == null? Collections.emptyMap():Collections.unmodifiableMap(headers);
    }

}
