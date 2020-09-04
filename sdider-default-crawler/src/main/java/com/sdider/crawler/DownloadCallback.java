package com.sdider.crawler;


import com.sdider.api.Response;

/**
 * 下载器下载请求完毕后的callback
 * @author yujiaxin
 */
public interface DownloadCallback {

    void succeeded(Response response);

    void failed(Exception ex);
}
