package com.wofang.demo.network.interceptor;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.wofang.demo.bean.requstbean.RequstEntity;
import com.wofang.demo.network.util.ParamAlias;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.utils.MD5SimpleUtils;
import com.wofang.demo.utils.MD5Utils;
import com.wofang.demo.utils.ToMapUtils;
import com.wofang.demo.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;

import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Create by wcm
 * on 2019/5/8
 * 参数MD5加密
 */
public class EncryptionInterceptor implements Interceptor {
    private static final String TAG = EncryptionInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        String method = request.method();
        RequestBody requestBody = request.body();
        //收集请求参数，方便调试
        if (null != requestBody) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String paramsStr = buffer.readString(charset);
            Map<String,String> map = ToMapUtils.toMap(paramsStr);
            String sign =MD5SimpleUtils.getMD5(MD5SimpleUtils.mapSortToString(map));
            map.put("sign",sign);
            FormBody.Builder builder = new FormBody.Builder();
            String text="";
            int m=0;
            for (String key : map.keySet()) {

                if (!key.equals("$change")&&!key.equals("serialVersionUID")){
                    if (m==0){
                        text=key+"="+map.get(key);

                    }else{
                        text+="&"+key+"="+map.get(key);

                    }
                    m++;
                }

                builder.add(key, map.get(key));
            }
            LogUtil.d("url== " + url+"?"+text);
            RequestBody newBody = builder.build();
            request = request.newBuilder().post(newBody).build();
        }
        return chain.proceed(request);
    }

}
