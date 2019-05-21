package com.wofang.demo.network.request;


import android.text.TextUtils;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wofang.demo.base.BaseApplication;
import com.wofang.demo.network.converter.NullOnEmptyConverterFactory;
import com.wofang.demo.network.interceptor.CommonInterceptor;
import com.wofang.demo.network.interceptor.EncryptionInterceptor;
import com.wofang.demo.network.request.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wofang.demo.store.ConfigInfo;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.utils.NetWorkUtils;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import okhttp3.Response;
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
    private static volatile Request request = null;
    private static final int CONNECTION_TIMEOUT = 2000;
    private static final int READ_TIMEOUT = 2000;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;


    /**
     * 初始化必要对象和参数
     */
    static  {
        /**
         * 云端响应头拦截器，用来配置缓存策略
         */
       /* Interceptor mRewriteCacheControlInterceptor = chain -> {
            // 拦截器获取请求
            okhttp3.Request request = chain.request();
            // 服务器的缓存策略
            String cacheControl = request.cacheControl().toString();
            // 断网时配置缓存策略
            if (!NetWorkUtils.isNetConnected()) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            // 在线缓存
            if (NetWorkUtils.isNetConnected()) {
                String setCookie = originalResponse.headers().get("Set-Cookie");
                if (null != setCookie && !("").equals(setCookie)) {
                    ConfigInfo.getInstance().setData(ConfigInfo.HTTP_JSESSIONID, setCookie);
                }
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        // 应用服务端配置的缓存策略
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                // 离线缓存
                *//*
                 * only-if-cached:(仅为请求标头)
                 *　 请求:告知缓存者,我希望内容来自缓存，我并不关心被缓存响应,是否是新鲜的.
                 * max-stale:
                 *　 请求:意思是,我允许缓存者，发送一个,过期不超过指定秒数的,陈旧的缓存.
                 *　 响应:同上.
                 * max-age:
                 *   请求:强制响应缓存者，根据该值,校验新鲜性.即与自身的Age值,与请求时间做比较.如果超出max-age值,
                 *   则强制去服务器端验证.以确保返回一个新鲜的响应.
                 *   响应:同上.
                 *//*
                // 需要服务端配合处理缓存请求头，不然会抛出： HTTP 504 Unsatisfiable Request (only-if-cached)
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .build();
            }
        };

        // 缓存
        File cacheFile = new File(BaseApplication.getApplication().getCacheDir(), "cache");
        // 100Mb
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        // 增加头部信息
        Interceptor headerInterceptor = chain -> {
            String jsessionid = String.valueOf(ConfigInfo.getInstance().getData(ConfigInfo.HTTP_JSESSIONID, ""));
            okhttp3.Request build = chain.request().newBuilder()
                    // 设置允许请求json数据
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", jsessionid)
                    .build();
            return chain.proceed(build);
        };*/

        //打印日志
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.d("message>>" + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getApplication()));

        String baseUrl = Request.HOST;
        //解决Http请求中的日期转换问题
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create();

        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                // 没网的情况下
//                .addInterceptor(mRewriteCacheControlInterceptor)
                // 有网的情况下
//                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(logging)
//                .addInterceptor(headerInterceptor)
                //公共拦截器，加入sign的公共参数
                .addNetworkInterceptor(new CommonInterceptor())
                .cookieJar(cookieJar)
//                .cache(cache)
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static Request getRequest() {
        if (request == null) {
            synchronized (Request.class) {
                request = retrofit.create(Request.class);
            }
        }
        return request;
    }


}
