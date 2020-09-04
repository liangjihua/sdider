package com.sdider.crawler;


import com.sdider.api.Request;
import com.sdider.api.Response;
import com.sdider.crawler.exception.DownloadFailedException;


/**
 * @author yujiaxin
 */
public interface Downloader {
    void start();

    void stop();

    /**
     * 下载一个{@link Request}，下载成功后使用给定的{@code callback}
     * 去消费{@link Response}。
     * @param request 要下载的{@link Request}
     * @param callback 消费对象
     * @throws DownloadFailedException 下载失败时抛出该异常。
     */
    void download(Request request, DownloadCallback callback) throws DownloadFailedException;
}
