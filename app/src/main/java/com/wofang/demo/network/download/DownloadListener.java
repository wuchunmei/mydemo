package com.wofang.demo.network.download;

/**
 * Create by wcm
 * on 2019/5/9
 */
public interface DownloadListener {
    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);
}
