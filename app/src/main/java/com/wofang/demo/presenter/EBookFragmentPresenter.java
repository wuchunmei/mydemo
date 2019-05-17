package com.wofang.demo.presenter;

import android.os.Bundle;

import com.wofang.demo.base.BaseFragmentPresenter;
import com.wofang.demo.base.BaseView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by wcm
 * on 2019/5/7 0007
 */
public class EBookFragmentPresenter extends BaseFragmentPresenter<BaseView> {
    public final static int DADA_CODE = 100;
    public final static String LIST_KEY = "list_key";
    private List<String> bannerList;
    private final static String url1 = "http://upload-images.jianshu.io/upload_images/589909-d046f5ca2abbd31e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
    private final static String url2 = "http://upload-images.jianshu.io/upload_images/589909-da8eaee55c62a4dd.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
    private final static String url3 = "http://upload-images.jianshu.io/upload_images/589909-88189759a24f42da.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
    private final static String url4 = "http://upload-images.jianshu.io/upload_images/589909-fad4a3da8703501c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";


    @Override
    public void onCreate() {
        initData();
    }

    private void initData() {
        bannerList = new ArrayList<>(4);
//        BannerBean banner1 = new BannerBean("测试图片1", url1, "");
//        BannerBean banner2 = new BannerBean("测试图片2", url2, "");
//        BannerBean banner3 = new BannerBean("测试图片3", url3, "");
//        BannerBean banner4 = new BannerBean("测试图片4", url4, "");
        bannerList.add(url1);
        bannerList.add(url2);
        bannerList.add(url4);
        bannerList.add(url3);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_KEY, (Serializable) bannerList);
        onPresenterEvent(DADA_CODE,bundle);
    }
}
