package com.wofang.demo.utils;

import android.annotation.SuppressLint;

import com.wofang.demo.network.util.ParamAlias;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by wcm
 * on 2019/5/9 0009
 */
public class ToMapUtils {
    public static Map<String, String> toMap(String jsonString) {
        ArrayList<String> listKey = new ArrayList<>();
        ArrayList<String> listValue = new ArrayList<>();
        int index = 0;
        readKey(listKey, listValue, index, jsonString);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < listKey.size(); i++) {
            String key = null;
            String value = null;
            if (listKey.get(i).charAt(0) == '"') {
                String s = listKey.get(i);
                key = s.substring(1, s.length() - 1);
            } else {
                key = listKey.get(i);
            }
            if (listValue.get(i).charAt(0) == '"') {
                String s = listValue.get(i);
                value = s.substring(1, s.length() - 1);
            } else {
                value = listValue.get(i);
            }
            map.put(key, value);
        }
        return map;
    }

    public static void readKey(List<String> listKey, List<String> listValue, int index, String jsonString) {
        int flag = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = index; i < jsonString.length(); i++) {
            char charAt = jsonString.charAt(i);
            if (charAt == '"') {
                flag = flag + 1;
            }
            if (flag == 1) {
                stringBuilder.append(jsonString.charAt(i));
            }
            if (flag == 2) {
                listKey.add(stringBuilder.toString().substring(1, stringBuilder.length()));
                readValue(listKey, listValue, i + 1, jsonString);
                return;
            }
        }
    }

    public static void readValue(List<String> listKey, List<String> listValue, int index, String jsonString) {
        int flag1 = 0;
        int flag2 = 0;
        int flag3 = 0;
        int flag4 = 0;
        int flag5 = 0;
        int flag6 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = index; i < jsonString.length(); i++) {
            char charAt = jsonString.charAt(i);
            if (charAt == '"') {
                flag1 = flag1 + 1;
            }
            if (charAt == ',') {
                flag2 = flag2 + 1;
            }
            if (charAt == '{') {
                flag3 = flag3 + 1;
            }
            if (charAt == '}') {
                flag4 = flag4 + 1;
            }
            if (charAt == '[') {
                flag5 = flag5 + 1;
            }
            if (charAt == ']') {
                flag6 = flag6 + 1;
            }
            stringBuilder.append(jsonString.charAt(i));
            if (flag5 > 0 && flag5 == flag6) {
                listValue.add(stringBuilder.toString().substring(1, stringBuilder.length()));
                readKey(listKey, listValue, i, jsonString);
                return;
            }
            if (flag5 == 0 && flag3 > 0 && flag4 == flag3) {
                listValue.add(stringBuilder.toString().substring(1, stringBuilder.length()));
                readKey(listKey, listValue, i, jsonString);
                return;
            }
            if (flag5 == 0 && flag3 == 0 && flag1 == 2) {
                listValue.add(stringBuilder.toString().substring(1, stringBuilder.length()));
                readKey(listKey, listValue, i + 1, jsonString);
                return;
            }
            if (flag5 == 0 && flag3 == 0 && flag1 == 2) {
                listValue.add(stringBuilder.toString().substring(1, stringBuilder.length()));
                readKey(listKey, listValue, i + 1, jsonString);
                return;
            }
            if (flag5 == 0 && flag3 == 0 && flag1 == 0 && (flag2 == 1 || flag4 == 1)) {
                listValue.add(stringBuilder.toString().substring(1, stringBuilder.length() - 1));
                readKey(listKey, listValue, i + 1, jsonString);
                return;
            }
        }
    }

    public static List<Map<String, String>> strArrToMapList(String strArr) {
        int flag1 = 0;
        int flag2 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        List<Map<String, String>> maps = new ArrayList<>();
        for (int i = 0; i < strArr.length(); i++) {
            if (strArr.charAt(i) == '{') {
                flag1 = flag1 + 1;
            }
            if (strArr.charAt(i) == '}') {
                flag2 = flag2 + 1;
            }
            stringBuilder.append(strArr.charAt(i));
            if (flag1 > 0 && flag1 == flag2) {
                stringList.add(stringBuilder.toString().substring(1, stringBuilder.length()));
                stringBuilder = new StringBuilder();
                flag1 = 0;
                flag2 = 0;
            }
        }
        for (String s : stringList) {
            Map<String, String> map = toMap(s);
            maps.add(map);
        }
        return maps;
    }

    /**
     * 将对象转成集合
     */
    @SuppressLint("DefaultLocale")
    public static Map<String, String> restructParamsObj(Object paramsObj) {
        Map<String, String> map = new HashMap<String, String>();

        for (Field field : paramsObj.getClass().getDeclaredFields()) {
            String paramKey;
            ParamAlias paramAlias = field.getAnnotation(ParamAlias.class);
            if (paramAlias != null) {
                paramKey = paramAlias.value();
            } else {
                paramKey = field.getName();
            }

            String paramValue = null;
            String getterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            try {
                Method method = paramsObj.getClass().getDeclaredMethod(getterName, (Class<?>[]) null);
                paramValue = (String) method.invoke(paramsObj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (paramValue == null) {
                map.put(paramKey, "");
            } else {
                map.put(paramKey, paramValue);
            }
        }

        return map;
    }


}
