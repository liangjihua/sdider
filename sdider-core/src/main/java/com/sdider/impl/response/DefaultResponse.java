package sdider.impl.response;

import com.sdider.ExtensionContainer;
import com.sdider.SdiderResponse;
import com.sdider.SdiderResult;
import com.sdider.constant.HttpHeader;
import com.sdider.crawler.Request;
import com.sdider.crawler.Response;
import com.sdider.impl.common.DefaultExtensionContainer;
import com.sdider.impl.item.DefaultResult;
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
    private final DefaultResult result;
    private final Response response;
    private ExtensionContainer extensionContainer;
    private String contentType;
    private Charset charset = StandardCharsets.UTF_8;
    private volatile String text;
    private final byte[] body;
    private final Map<String, String> headers;

    public DefaultResponse(Response response) {
        this.request = response.getRequest();
        this.response = response;
        this.result = new DefaultResult(request);
        this.body = response.getBody();
        this.headers = response.getHeaders();
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
    public SdiderResult getResult() {
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
