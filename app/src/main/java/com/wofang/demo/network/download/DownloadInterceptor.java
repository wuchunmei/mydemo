package com.wofang.demo.network.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Create by wcm
 * on 2019/5/9 0009
 */
public class DownloadInterceptor implements Interceptor {

    private DownloadListener mDownloadListener;

    public DownloadInterceptor(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new DownResponseBody(response.body(),mDownloadListener)).build();
    }
}
