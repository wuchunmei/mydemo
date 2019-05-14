package com.wofang.demo.base;

import android.app.Application;
import android.content.Context;

import com.wofang.demo.store.ConfigInfo;


public class BaseApplication extends Application {

    private static BaseApplication application;

    public static BaseApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ConfigInfo.init(application);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 5.0以下机型无法运行应用
    }
}
