package com.wofang.demo.network.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.wofang.demo.network.util.UrlUtils;
import com.wofang.demo.utils.LogUtil;
import com.wofang.demo.utils.MD5SimpleUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 网络请求拦截器
 */
public class CommonInterceptor implements Interceptor {

    private static final String TAG = CommonInterceptor.class.getSimpleName();
    private static Map<String, String> commonParams;

    public synchronized static void setCommonParam(Map<String, String> commonParams) {
        if (commonParams != null) {
            if (CommonInterceptor.commonParams != null) {
                CommonInterceptor.commonParams.clear();
            } else {
                CommonInterceptor.commonParams = new HashMap<>();
            }
            for (String paramKey : commonParams.keySet()) {
                CommonInterceptor.commonParams.put(paramKey, commonParams.get(paramKey));
            }
        }
    }

    public synchronized static void updateOrInsertCommonParam(@NonNull String paramKey, @NonNull String paramValue) {
        if (commonParams == null) {
            commonParams = new HashMap<>();
        }
        commonParams.put(paramKey, paramValue);
    }

    @Override
    public synchronized Response intercept(Chain chain) throws IOException {
        Request request = rebuildRequest(chain.request());
        Response response = chain.proceed(request);
        // 输出返回结果
        try {
            Charset charset;
            charset = Charset.forName("UTF-8");
            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
            Reader jsonReader = new InputStreamReader(responseBody.byteStream(), charset);
            BufferedReader reader = new BufferedReader(jsonReader);
            StringBuilder sbJson = new StringBuilder();
            String line = reader.readLine();
            do {
                sbJson.append(line);
                line = reader.readLine();
            } while (line != null);
            LogUtil.e("response: " + sbJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage(), e);
        }
//        saveCookies(response, request.url().toString());
        return response;
    }


    public static byte[] toByteArray(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        InputStream inputStream = buffer.inputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] bufferWrite = new byte[4096];
        int n;
        while (-1 != (n = inputStream.read(bufferWrite))) {
            output.write(bufferWrite, 0, n);
        }
        return output.toByteArray();
    }

    private Request rebuildRequest(Request request) throws IOException {
        Request newRequest;
        if ("POST".equals(request.method())) {
            newRequest = rebuildPostRequest(request);
        } else if ("GET".equals(request.method())) {
            newRequest = rebuildGetRequest(request);
        } else {
            newRequest = request;
        }
        LogUtil.e("requestUrl: " + newRequest.url().toString());
        return newRequest;
    }

    /**
     * 对post请求添加统一参数
     */
    private Request rebuildPostRequest(Request request) {
        Map<String, String> signParams = new HashMap<>(); // 假设你的项目需要对参数进行签名
        Map<String, String> urlParams = new HashMap<>(); //接口直接把参数拼接在url上，截取其中的参数
        RequestBody originalRequestBody = request.body();
        String url = String.valueOf(request.url().url());
        urlParams = UrlUtils.urlSplit(url);
        String sign;
        assert originalRequestBody != null;
        RequestBody newRequestBody = null;
        if (originalRequestBody instanceof FormBody) { // 传统表单
            FormBody.Builder builder = new FormBody.Builder();
            FormBody requestBody = (FormBody) request.body();
            int fieldSize = requestBody == null ? 0 : requestBody.size();
            for (int i = 0; i < fieldSize; i++) {
                builder.add(requestBody.name(i), requestBody.value(i));
                signParams.put(requestBody.name(i), requestBody.value(i));
            }
            if (commonParams != null && commonParams.size() > 0) {
                signParams.putAll(commonParams);
                for (String paramKey : commonParams.keySet()) {
                    builder.add(paramKey, commonParams.get(paramKey));
                }
            }
            // ToDo 此处可对参数做签名处理 signParams
             sign =  MD5SimpleUtils.getMD5(MD5SimpleUtils.mapSortToString(signParams));
            builder.add("sign", sign);
            newRequestBody = builder.build();
        } else if (originalRequestBody instanceof MultipartBody) { // 文件
            MultipartBody requestBody = (MultipartBody) request.body();
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
            if (requestBody != null) {
                for (int i = 0; i < requestBody.size(); i++) {
                    MultipartBody.Part part = requestBody.part(i);
                    multipartBodybuilder.addPart(part);

                    /*
                     上传文件时，请求方法接收的参数类型为RequestBody或MultipartBody.Part参见uploadFile方法
                     Observable<String> uploadFile(@Part("userName") RequestBody userName, @Part MultipartBody.Part filePart);
                     RequestBody作为普通参数载体，封装了普通参数的value; MultipartBody.Part即可作为普通参数载体也可作为文件参数载体
                     当RequestBody作为参数传入时，框架内部仍然会做相关处理，进一步封装成MultipartBody.Part，因此在拦截器内部，
                     拦截的参数都是MultipartBody.Part类型
                     */

                    /*
                     1.若MultipartBody.Part作为文件参数载体传入，则构造MultipartBody.Part实例时，
                     需使用MultipartBody.Part.createFormData(String name, @Nullable String filename, RequestBody body)方法，
                     其中name参数可作为key使用(因为你可能一次上传多个文件，服务端可以此作为区分)且不能为null，
                     body参数封装了包括MimeType在内的文件信息，其实例创建方法为RequestBody.create(final @Nullable MediaType contentType, final File file)
                     MediaType获取方式如下：
                     String fileType = FileUtil.getMimeType(file.getAbsolutePath());
                     MediaType mediaType = MediaType.parse(fileType);

                     2.若MultipartBody.Part作为普通参数载体，建议使用MultipartBody.Part.createFormData(String name, String value)方法创建Part实例
                       name可作为key使用，name不能为null,通过这种方式创建的实例，其RequestBody属性的MediaType为null；当然也可以使用其他方法创建
                     */

                    /*
                      提取非文件参数时,以RequestBody的MediaType为判断依据.
                      此处提取方式简单暴力。默认part实例的RequestBody成员变量的MediaType为null时，part为非文件参数
                      前提是:
                      a.构造RequestBody实例参数时，将MediaType设置为null
                      b.构造MultipartBody.Part实例参数时,推荐使用MultipartBody.Part.createFormData(String name, String value)方法，或使用以下方法
                        b1.MultipartBody.Part.create(RequestBody body)
                        b2.MultipartBody.Part.create(@Nullable Headers headers, RequestBody body)
                        若使用方法b1或b2，则要求

                      备注：
                      您也可根据需求修改RequestBody的MediaType，但尽量保持外部传入参数的MediaType与拦截器内部添加参数的MediaType一致，方便统一处理
                     */

                    MediaType mediaType = part.body().contentType();
                    if (mediaType == null) {
                        String normalParamKey;
                        String normalParamValue;
                        try {
                            normalParamValue = getParamContent(requestBody.part(i).body());
                            Headers headers = part.headers();
                            if (!TextUtils.isEmpty(normalParamValue) && headers != null) {
                                for (String name : headers.names()) {
                                    String headerContent = headers.get(name);
                                    if (!TextUtils.isEmpty(headerContent)) {
                                        String[] normalParamKeyContainer = headerContent.split("name=\"");
                                        if (normalParamKeyContainer.length == 2) {
                                            normalParamKey = normalParamKeyContainer[1].split("\"")[0];
                                            signParams.put(normalParamKey, normalParamValue);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (commonParams != null && commonParams.size() > 0) {
                signParams.putAll(commonParams);
                for (String paramKey : commonParams.keySet()) {
                    // 两种方式添加公共参数
                    // method 1
                    multipartBodybuilder.addFormDataPart(paramKey, commonParams.get(paramKey));
                    // method 2
//                    MultipartBody.Part part = MultipartBody.Part.createFormData(paramKey, commonParams.get(paramKey));
//                    multipartBodybuilder.addPart(part);
                }
            }
            // ToDo 此处可对参数做签名处理 signParams
            sign =  MD5SimpleUtils.getMD5(MD5SimpleUtils.mapSortToString(signParams));
            multipartBodybuilder.addFormDataPart("sign", sign);
            newRequestBody = multipartBodybuilder.build();
        } else {
            //一般应用都是用这两种方式请求接口的
            //body装参数的方式请求
            if(!isSplitUrl(urlParams)){
                try {
                    JSONObject jsonObject;
                    if (originalRequestBody.contentLength() == 0) {
                        jsonObject = new JSONObject();
                    } else {
                        jsonObject = new JSONObject(getParamContent(originalRequestBody));
                    }
                    if (commonParams != null && commonParams.size() > 0) {
                        for (String commonParamKey : commonParams.keySet()) {
                            jsonObject.put(commonParamKey, commonParams.get(commonParamKey));
                        }
                    }
                    // ToDo 此处可对参数做签名处理
                    sign =  MD5SimpleUtils.getMD5(MD5SimpleUtils.mapSortToString(signParams));
                    jsonObject.put("sign", sign);
                    newRequestBody = RequestBody.create(originalRequestBody.contentType(), jsonObject.toString());
                    LogUtil.e(getParamContent(newRequestBody));
                } catch (Exception e) {
                    newRequestBody = originalRequestBody;
                    e.printStackTrace();
                }
            }else {
                sign =  MD5SimpleUtils.getMD5(MD5SimpleUtils.mapSortToString(urlParams));
                Map<String,String> map = new HashMap<>();
                map.put("sign",sign);
                url = UrlUtils.appendUrl(url,map);
            }
        }
//        可根据需求添加或修改header,此处制作示意
//       return request.newBuilder()
//                .addHeader("header1", "header1")
//                .addHeader("header2", "header2")
//                .method(request.method(), newRequestBody)
//                .build();
        if (isSplitUrl(urlParams)) {
            FormBody.Builder builder = new FormBody.Builder();

            String text="";
            int m=0;
            for (String key : urlParams.keySet()) {

                if (!key.equals("$change")&&!key.equals("serialVersionUID")){
                    if (m==0){
                        text=key+"="+urlParams.get(key);

                    }else{
                        text+="&"+key+"="+urlParams.get(key);

                    }
                    m++;
                }

                builder.add(key, urlParams.get(key));
            }
            System.out.println(url+"?"+text);
            RequestBody requestBody = builder.build();
            return request.newBuilder().method(request.method(),requestBody).url(url).build();
        } else {
            return request.newBuilder().method(request.method(), newRequestBody).build();
        }
    }



    /**
     * 是否把参数拼接在url上
     * @param map
     * @return
     */
    private boolean isSplitUrl(Map<String,String> map){
        if (map.isEmpty() && map.size() == 0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 获取常规post请求参数
     */
    private String getParamContent(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }

    /**
     * 对get请求做统一参数处理
     */
    private Request rebuildGetRequest(Request request) {
        if (commonParams == null || commonParams.size() == 0) {
            return request;
        }
        String url = request.url().toString();
        int separatorIndex = url.lastIndexOf("?");
        StringBuilder sb = new StringBuilder(url);
        if (separatorIndex == -1) {
            sb.append("?");
        }
        for (String commonParamKey : commonParams.keySet()) {
            sb.append("&").append(commonParamKey).append("=").append(commonParams.get(commonParamKey));
        }
        Request.Builder requestBuilder = request.newBuilder();
        return requestBuilder.url(sb.toString()).build();
    }
}
