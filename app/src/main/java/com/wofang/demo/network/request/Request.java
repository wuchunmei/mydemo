package com.wofang.demo.network.request;




import com.wofang.demo.bean.requstbean.UpdateRequest;
import com.wofang.demo.bean.responsebean.CheckNewVersionResponse;
import com.wofang.demo.network.response.ResponseResult;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Create by wcm
 * on 2019/5/7
 * 封装请求的接口
 */

public interface Request {
    String HOST = "http://hub.naliwan.com/";

    @POST("system/lastVersion")
    Observable<ResponseResult<CheckNewVersionResponse>> checkUpdate(@Query("type") String type);
    @GET
    @Streaming
    Observable<ResponseBody> downApk(@Url String url);
}
