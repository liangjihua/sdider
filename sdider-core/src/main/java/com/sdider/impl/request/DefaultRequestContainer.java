package com.sdider.impl.request;

import com.sdider.SdiderRequest;
import com.sdider.SdiderRequestContainer;
import com.sdider.impl.log.Logger;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import static com.sdider.util.UrlUtils.urlJoin;

/**
 * @author yujiaxin
 */
public class DefaultRequestContainer extends AbstractRequestContainer implements SdiderRequestContainer {
    private static final Logger logger = Logger.getInstance(DefaultRequestContainer.class);
    private final String baseUrl;
    private final SdiderRequestFactory sdiderRequestFactory;
    public DefaultRequestContainer(SdiderRequestFactory requestFactory) {
        this(null ,requestFactory);
    }

    public DefaultRequestContainer(String baseUrl, SdiderRequestFactory requestFactory) {
        this.baseUrl = baseUrl;
        this.sdiderRequestFactory = requestFactory;
    }

    @Override
    public void request(@DelegatesTo(SdiderRequest.class)Closure<?> requestConfig) {
        SdiderRequest request = sdiderRequestFactory.create();
        ClosureUtils.delegateRun(request, requestConfig);
        if (request.getUrl() == null || request.getUrl().trim().equals("")) {
            logger.debug("request skipped: {'url':'{}', 'reason':'url is empty'}", request.getUrl());
            return;
        }
        request.url(urlJoin(baseUrl, request.getUrl()));
        addRequest(request);
    }

    @Override
    public void request(String... urls) {
        if (urls == null) {
            return;
        }
        for (String url : urls) {
            if (url == null || url.trim().equals("")){
                logger.debug("request skipped: {'url':'{}', 'reason':'url is empty'}", url);
                continue;
            }
            url = urlJoin(baseUrl, url);
            SdiderRequest request = sdiderRequestFactory.create();
            request.GET(url);
            addRequest(request);
        }
    }
}
