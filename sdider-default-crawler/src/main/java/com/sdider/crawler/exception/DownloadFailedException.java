package com.sdider.crawler.exception;

import com.sdider.api.Request;

/**
 * @author yujiaxin
 */
public class DownloadFailedException extends RuntimeException {
    private final Request request;

    public DownloadFailedException(Request request, Throwable cause) {
        super("下载请求失败", cause);
        this.request = request;
    }

    public DownloadFailedException(String message, Request request) {
        super(message);
        this.request = request;
    }

    public DownloadFailedException(Request request) {
        super("下载请求失败");
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
