package com.wofang.demo.store;

import android.content.Context;

import com.wofang.demo.utils.SharedPreferencesUtils;

/**
 * 其他的配置信息
 */
public class ConfigInfo {

    /**
     * http会话session_id
     */
    public static final String HTTP_JSESSIONID = "jsessionid";
    private static SharedPreferencesUtils sharedPreferencesUtils;
    private volatile static ConfigInfo instance = null;

    private ConfigInfo() {
    }

    public static ConfigInfo getInstance() {
        if (instance == null) {
            synchronized (ConfigInfo.class) {
                if (instance == null) {
                    instance = new ConfigInfo();
                }
            }
        }
        return instance;
    }

    public static void init(Context context) {
        sharedPreferencesUtils = new SharedPreferencesUtils(context, "configInfo");
    }

    public Object getData(String key, Object defaultObject) {
        return sharedPreferencesUtils.getSharedPreference(key, defaultObject);
    }

    public void setData(String key, Object object) {
        sharedPreferencesUtils.putSharedPreference(key, object);
    }

    public void clearAllData() {
        sharedPreferencesUtils.clear();
    }
}
