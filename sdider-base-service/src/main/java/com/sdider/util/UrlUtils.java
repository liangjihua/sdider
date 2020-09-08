package com.sdider.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author yujiaxin
 */
public class UrlUtils {
    private UrlUtils(){}

    /**
     * 清理合并给定的url参数，若给定url是一个http请求路径，原样返回它。
     * 否则按照RFC2396将其与当前response的上下文请求路径进行合并。
     * @see URL#URL(URL, String)
     * @param url 给定的url参数，不能为空
     * @return 按照方法所述规则进行合并后的新url；或者返回null，如果
     * url的格式不合法。
     */
    public static String urlJoin(String baseUrl, String url) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            return url;
        }
        if (url == null || url.trim().isEmpty()) {
            return baseUrl;
        }
        if (url.startsWith("http://")||url.startsWith("https://")) {
            return url;
        }
        if (url.startsWith("?")) {
            try {
                return new URL(baseUrl+url).toExternalForm();
            } catch (MalformedURLException ignored) {
                //should never happen
            }
        }
        URL context;
        try {
            try {
                context = new URL(baseUrl);
            } catch (MalformedURLException e) {
                return new URL(url).toExternalForm();
            }
            return new URL(context, url).toExternalForm();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
