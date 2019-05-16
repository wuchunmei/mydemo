package com.wofang.demo.network.request;




import com.wofang.demo.bean.CheckNewVersionResponse;
import com.wofang.demo.bean.HouseListResult;
import com.wofang.demo.network.response.ResponseResult;

import java.util.Map;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Create by wcm
 * on 2019/5/7
 * 封装请求的接口
 */

public interface Request {
    String HOST = "http://192.168.0.199:7392/";

    @POST("system/lastVersion")
    Observable<ResponseResult<CheckNewVersionResponse>> checkUpdate(@Query("type") String type);
    @GET
    @Streaming
    Observable<ResponseBody> downApk(@Url String url);

    /**
     * 房源列表接口
     *
     * @param map  封装的map
     * @return BaseResponse
     */
    @POST("product/hotel/getHotelList")
    Observable<ResponseResult<HouseListResult>> getHouseList(@QueryMap Map<String, String> map);

}
