package com.wofang.demo.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;


import com.wofang.demo.utils.DateUtils;
import com.wofang.demo.utils.ObjectSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CacheDataManager {

    public static Map<String, String> createCacheColumnItems() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(CacheColumnItems._ID, CacheColumnItems._ID);
        map.put(CacheColumnItems.KEY, CacheColumnItems.KEY);
        map.put(CacheColumnItems.CACHEDATA, CacheColumnItems.CACHEDATA);
        map.put(CacheColumnItems.TIME_STAMP, CacheColumnItems.TIME_STAMP);
        return map;
    }

    public static void deleteCache(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        try {
            int count = context.getContentResolver().delete(CacheContentHelper.getCacheContentUri(context),
                    "KEY=?", new String[]{key});
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteCache(Context context, String[] keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("KEY IN (");
        for (int i = 0; i < keys.length; i++) {
            sb.append("'");
            sb.append(keys[i]);
            if (i < keys.length - 1) {
                sb.append("',");
            } else {
                sb.append("'");
            }
        }
        sb.append(")");
        try {
            int count = context.getContentResolver().delete(CacheContentHelper.getCacheContentUri(context),
                    sb.toString(), null);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /*
     * 只清除缓存
     */
    public static void clearCache(Context context) {
        try {
            int count = context.getContentResolver().delete(CacheContentHelper.getCacheContentUri(context),
                    null, null);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void clearAllCache(Context context) {
        try {
            CacheContentHelper.clearAllCache(context);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isKeyInTempCache(Context context, String key, int type, int value) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CacheContentHelper.getCacheContentUri(context),
                    null, "KEY=?", new String[]{key}, null);
            boolean exists = false;
            if (cursor != null) {
                if (cursor.moveToFirst()) { //如果存在相同的记录，那么需要删除之前的记录
                    int id = cursor.getInt(cursor.getColumnIndex(CacheColumnItems._ID));
                    long time = cursor.getLong(cursor.getColumnIndex(CacheColumnItems.TIME_STAMP));
                    Date d = new Date(time);
                    if (type == 0) {
                        if (!DateUtils.checkExpire(d, value)) { //30分钟过期
                            exists = true;
                        }
                    }
                    if (type == 1) {
                        if (!DateUtils.checkExpireByHour(d, value)) {
                            exists = true;
                        }
                    }
                }
                cursor.close();
                cursor = null;
            }
            return exists;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        }
    }

    public static boolean isKeyInTempCache(Context context, String key) {
        return isKeyInTempCache(context, key, 0, 30);
    }

    public static boolean isKeyInTempCacheInHour(Context context, String key, int hour) {
        return isKeyInTempCache(context, key, 1, hour);
    }

    public static EntityCacheject getDataFromCache(Context context, String key) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CacheContentHelper.getCacheContentUri(context),
                    null, "KEY=?", new String[]{key}, null);
            //Log.v(TAG, "KEY=" + key + ";cursor=" + cursor);
            EntityCacheject cacheObject = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) { //如果存在相同的记录，那么需要删除之前的记录
                    long time = cursor.getLong(cursor.getColumnIndex(CacheColumnItems.TIME_STAMP));
                    //Log.v(TAG, "KEY=" + key + ";time=" + time);
                    Date date = new Date(time);
                    Object data = null;
                    try {
                        data = ObjectSerializer.deserializeObject(cursor.getBlob(cursor.getColumnIndex(CacheColumnItems.CACHEDATA)));
                    } catch (Exception ex) {

                    }
                    if (data != null) {
                        cacheObject = new EntityCacheject(key, date, (Serializable) data);
                    }
                }
                cursor.close();
            }
            return cacheObject;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        }
    }

    public static boolean setDataToCache(Context context, String key, Serializable cacheData) {
        Cursor cursor = null;
        try {
            Uri uri = CacheContentHelper.getCacheContentUri(context);
            cursor = context.getContentResolver().query(uri,
                    CacheContentProvider.ID_PROJECTION, "KEY=?", new String[]{key}, null);
            String id = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    id = cursor.getString(0);
                }
                cursor.close();
            }
            ContentValues cv = new ContentValues();
            cv.put(CacheColumnItems.CACHEDATA, ObjectSerializer.serializeObject(cacheData));
            cv.put(CacheColumnItems.TIME_STAMP, new Date().getTime());

            if (id != null) {
                context.getContentResolver().update(uri, cv, "_id=?", new String[]{id});
            } else {
                cv.put(CacheColumnItems.KEY, key);
                context.getContentResolver().insert(uri, cv);
            }
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        }
    }

    private static class CacheColumnItems implements BaseColumns {
        public static final String KEY = "KEY";
        public static final String CACHEDATA = "CACHEDATA";
        public static final String TIME_STAMP = "TIME_STAMP";
    }
}
