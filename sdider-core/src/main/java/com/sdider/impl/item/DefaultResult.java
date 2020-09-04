package com.sdider.impl.item;

import com.sdider.SdiderItem;
import com.sdider.SdiderRequest;
import com.sdider.SdiderResult;
import com.sdider.api.Item;
import com.sdider.api.Pipeline;
import com.sdider.api.Request;
import com.sdider.impl.request.AbstractRequestContainer;
import com.sdider.impl.request.DefaultRequestImpl;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yujiaxin
 */
public class DefaultResult extends AbstractRequestContainer implements SdiderResult {
    private static final Logger logger = LoggerFactory.getLogger(DefaultResult.class);
    private List<Item> items = new ArrayList<>();
    private final Request request;

    public DefaultResult(Request request) {
        this.request = request;
    }

    @Override
    public void item(@DelegatesTo(SdiderItem.class) Closure<?> itemConfig) {
        SdiderItem item = new DefaultItem(request);
        ClosureUtils.delegateRun(item, itemConfig);
        addItem(item);
    }

    @Override
    public void setItems(List<Item> items) {
        if (items == null) {
            this.items = new ArrayList<>();
            return;
        }
        this.items = new ArrayList<>(items);
    }

    @Override
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    @Override
    public void consume(List<Pipeline> pipelines) {
        for (Pipeline pipeline : pipelines) {
            for (Item item : items) {
                item.consume(pipeline);
            }
        }
    }

    @Override
    public void request(@DelegatesTo(SdiderRequest.class) Closure<?> requestConfig) {
        SdiderRequest request = new DefaultRequestImpl();
        ClosureUtils.delegateRun(request, requestConfig);
        request.url(urlClean(request.getUrl()));
        addRequest(request);
    }

    @Override
    public void request(String... urls) {
        if (urls == null) {
            logger.warn("请求url为null，跳过");
            return;
        }
        for (String url : urls) {
            url = urlClean(url);
            SdiderRequest request = new DefaultRequestImpl();
            request.GET(url);
            addRequest(request);
        }
    }

    /**
     * 清理合并给定的url参数，若给定url是一个http请求路径，原样返回它。
     * 否则按照RFC2396将其与当前response的上下文请求路径进行合并。
     * @see URL#URL(URL, String)
     * @param url 给定的url参数，不能为空
     * @return 按照方法所述规则进行合并后的新url；或者返回null，如果
     * url的格式不合法。
     */
    public String urlClean(String url) {
        if (url.startsWith("http://")||url.startsWith("https://")) {
            return url;
        }
        String refer = request.getUrl();
        if (url.startsWith("?")) {
            try {
                return new URL(refer+url).toExternalForm();
            } catch (MalformedURLException ignored) {
                //should never happen
            }
        }
        URL context;
        try {
            try {
                context = new URL(refer);
            } catch (MalformedURLException e) {
                return new URL(url).toExternalForm();
            }
            return new URL(context, url).toExternalForm();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "DefaultResult{" + "items=" + items +
                ", request=" + request +
                '}';
    }
}
