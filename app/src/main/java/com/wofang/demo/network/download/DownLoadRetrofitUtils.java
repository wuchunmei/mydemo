package com.wofang.demo.network.download;

import android.util.Log;
import com.wofang.demo.network.converter.NullOnEmptyConverterFactory;
import com.wofang.demo.network.request.Request;
import com.wofang.demo.network.util.DefaultDisposableObserver;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by wcm
 * on 2019/5/9 0009
 */
public class DownLoadRetrofitUtils {
    private static final String TAG = DownLoadRetrofitUtils.class.getSimpleName();
    private  Retrofit retrofit;
    private static volatile Request request = null;
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 2000;

    private DownloadListener mDownloadListener;

    public DownLoadRetrofitUtils(DownloadListener listener){
        this.mDownloadListener = listener;
        DownloadInterceptor interceptor = new DownloadInterceptor(listener);
        String baseUrl = Request.HOST;
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Request getRequest() {
        if (request == null) {
            synchronized (Request.class) {
                request = retrofit.create(Request.class);
            }
        }
        return request;
    }

    /**
     * 下载
     * @param url
     * @param filePath
     */
    public void downLoad(String url, String filePath) {
        getRequest().downApk(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDownloadListener.onStartDownload();
                    }
                })
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFie(inputStream, filePath);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultDisposableObserver<InputStream>() {
            @Override
            public void onNext(InputStream inputStream) {
                super.onNext(inputStream);
                mDownloadListener.onFinishDownload();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mDownloadListener.onFail(e.toString());
            }
        });

    }

    /**
     * 将输入流写入文件
     * @param inputString
     * @param filePath
     */
    private void writeFie(InputStream inputString,String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
            mDownloadListener.onFail("FileNotFoundException");
        } catch (IOException e) {
            mDownloadListener.onFail("IOException");
        }
    }


}
