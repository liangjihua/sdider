package com.sdider.impl.response;

import com.sdider.ExtensionContainer;
import com.sdider.SdiderItemContainer;
import com.sdider.SdiderRequestContainer;
import com.sdider.SdiderResponse;
import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.api.Result;
import com.sdider.constant.HttpHeader;
import com.sdider.impl.common.DefaultExtensionContainer;
import com.sdider.impl.item.DefaultItemContainer;
import com.sdider.impl.item.DefaultResult;
import com.sdider.impl.request.DefaultRequestContainer;
import com.sdider.impl.request.SdiderRequestFactory;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *
 * @author yujiaxin
 */
public class DefaultResponse extends GroovyObjectSupport implements SdiderResponse {
    private final Request request;
    private final Response response;
    private ExtensionContainer extensionContainer;
    private String contentType;
    private Charset charset = StandardCharsets.UTF_8;
    private String text;
    private final byte[] body;
    private final Map<String, String> headers;
    private final SdiderRequestFactory factory;
    private SdiderRequestContainer requestContainer;
    private SdiderItemContainer itemContainer;

    public DefaultResponse(Response response, SdiderRequestFactory factory) {
        this.request = response.getRequest();
        this.response = response;
        this.body = response.getBody();
        this.headers = response.getHeaders();
        this.factory = factory;
        parseContentType();
    }

    private void parseContentType() {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            if (header.getKey().equalsIgnoreCase(HttpHeader.CONTENT_TYPE)) {
                contentType = header.getValue();
                for (String s : contentType.split(";")) {
                    String[] pair = s.split("=");
                    if (pair[0].equalsIgnoreCase("charset")) {
                        charset = Charset.forName(pair[1]);
                    }
                }
            }
        }
    }

    @Override
    public void items(Closure<?> itemConfig) {
        if (itemContainer == null) {
            itemContainer = new DefaultItemContainer(request);
        }
        ClosureUtils.delegateRun(itemContainer, itemConfig);
    }

    @Override
    public void targets(Closure<?> requestConfig) {
        if (requestContainer == null) {
            requestContainer = new DefaultRequestContainer(request.getUrl(), factory);
        }
        ClosureUtils.delegateRun(requestContainer, requestConfig);
    }

    public Result getResult(){
        DefaultResult result = new DefaultResult();
        if (requestContainer != null) {
            result.setRequests(requestContainer.getRequests());
        }
        if (itemContainer != null) {
            result.setItems(itemContainer.getItems());
        }
        return result;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getCharset() {
        return charset.displayName();
    }

    @Override
    public String getText() {
        if (text == null) {
            text = new String(body, charset);
        }
        return text;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String getReasonPhrase() {
        return response.getReasonPhrase();
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public ExtensionContainer getExtensions() {
        ensureExtensionInitialized();
        return extensionContainer;
    }

    private void ensureExtensionInitialized() {
        if (extensionContainer == null) {
            extensionContainer = new DefaultExtensionContainer();
        }
    }
}
