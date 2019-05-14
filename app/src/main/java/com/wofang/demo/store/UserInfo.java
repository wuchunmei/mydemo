package com.wofang.demo.store;

import android.content.Context;

import com.wofang.demo.utils.SharedPreferencesUtils;


/**
 * 用户信息存储
 */
public class UserInfo {

    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_ID = "user_id";
    private static SharedPreferencesUtils sharedPreferencesUtils;
    private volatile static UserInfo instance = null;

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        if (instance == null) {
            synchronized (UserInfo.class) {
                if (instance == null) {
                    instance = new UserInfo();
                }
            }
        }
        return instance;
    }

    public static void init(Context context) {
        sharedPreferencesUtils = new SharedPreferencesUtils(context, "userInfo");
    }

    public boolean isLogin() {
        return instance.getData(USER_NAME, null) != null && instance.getData(USER_PHONE, null) != null;
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
