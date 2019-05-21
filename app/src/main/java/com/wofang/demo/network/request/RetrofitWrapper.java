package com.wofang.demo.network.request;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wofang.demo.base.BaseApplication;
import com.wofang.demo.network.converter.NullOnEmptyConverterFactory;
import com.wofang.demo.network.interceptor.CacheControlInterceptor;
import com.wofang.demo.network.interceptor.CommonInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wofang.demo.utils.LogUtil;


import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by wcm
 * on 2019/5/7
 * API初始化类
 */

public class RetrofitWrapper {
    private static Retrofit retrofit;
    private static final Object object = new Object();
    private static volatile Request request = null;
    /**
     * 连接时长，单位：秒
     */
    private static final int CONNECT_TIME_OUT = 12;
    /**
     * 读超时长，单位：秒
     */
    private static final int READ_TIME_OUT = 60;
    /**
     * 写超时长，单位：秒
     */
    private static final int WRITE_TIME_OUT = 60;

    //这个人函数供外部调用，当请求数据时来调用
    @NonNull
    public static Retrofit getRetrofit() {
        synchronized (object) {
            if (retrofit == null) {
                //打印日志
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        LogUtil.d("message>>" + message);
                    }
                });
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                // 指定缓存路径,缓存大小 50Mb
                Cache cache = new Cache(new File(BaseApplication.getApplication().getCacheDir(), "HttpCache"),
                        1024 * 1024 * 50);

                // Cookie 持久化
                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getApplication()));

                //解决Http请求中的日期转换问题
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .serializeNulls()
                        .create();

                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .cache(cache)
                        .addInterceptor(interceptor)
                        //公共拦截器，加入sign的公共参数
                        .addNetworkInterceptor(new CommonInterceptor())
                        .addInterceptor(new CacheControlInterceptor())
                        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);



                retrofit = new Retrofit.Builder()
                        .baseUrl(Request.HOST)
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .build();
            }
            return retrofit;
        }
    }

    public static Request getRequest() {
        if (request == null) {
            synchronized (Request.class) {
                request = getRetrofit().create(Request.class);
            }
        }
        return request;
    }

}
