package sdider.impl.request;

import com.sdider.RequestConfig;
import com.sdider.constant.HttpHeader;
import com.sdider.crawler.common.DynamicPropertiesObject;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.Map;

/**
 * @author yujiaxin
 */
public class DefaultRequestConfigImpl implements RequestConfig {
    /**
     * 全局默认的requestConfig。注意不要随意修改这个全局实例。
     */
    public static final DefaultRequestConfigImpl GLOBAL_REQUEST_CONFIG = new DefaultRequestConfigImpl();
    private DefaultDynamicPropertiesObject<String> headers = new DefaultDynamicPropertiesObject<>();
    private String proxy;

    public DefaultRequestConfigImpl() {
//        userAgent(UA_CHROME);
    }

    @Override
    public void contentType(String contentType) {
        headers.set(HttpHeader.CONTENT_TYPE, contentType);
    }

    @Override
    public String getContentType() {
        if (headers.has(HttpHeader.CONTENT_TYPE)) {
            return headers.get(HttpHeader.CONTENT_TYPE);
        }
        return null;
    }

    @Override
    public void headers(@DelegatesTo(DynamicPropertiesObject.class) Closure<?> headersSetClosure) {
        ClosureUtils.delegateRun(headers, headersSetClosure);
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = new DefaultDynamicPropertiesObject<>();
        if (headers != null) {
            headers.forEach(this.headers::set);
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers.getProperties();
    }

    @Override
    public void userAgent(String userAgent) {
        headers.set(HttpHeader.USER_AGENT, userAgent);
    }

    @Override
    public String getUserAgent() {
        if (headers.has(HttpHeader.USER_AGENT)) {
            return headers.get(HttpHeader.USER_AGENT);
        }
        return null;
    }

    @Override
    public void proxy(String proxy) {
        this.proxy = proxy;
    }

    @Override
    public String getProxy() {
        return proxy;
    }

    @Override
    public String toString() {
        return "DefaultRequestConfigImpl{" + "headers=" + headers +
                ", proxy='" + proxy + '\'' +
                '}';
    }
}
