package com.wofang.demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wofang.demo.base.BaseApplication;


/**
 * 网络连接工具类
 *
 * @author chenyacheng
 * @date 2019/01/18
 */
public class NetWorkUtils {

    /**
     * 网络是否连接判断工具
     */
    public static boolean isNetConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return info.getDetailedState() == NetworkInfo.DetailedState.CONNECTED;
            }
        }
        return false;
    }
}
