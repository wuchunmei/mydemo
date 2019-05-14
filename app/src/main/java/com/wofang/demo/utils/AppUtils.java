package com.wofang.demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

/**
 * App相关工具类
 *
 * @author chenyacheng
 * @date 2019/02/20
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getPhoneBrand() {
        return Build.BRAND == null ? "" : Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getPhoneModel() {
        return Build.MODEL == null ? "" : Build.MODEL;
    }

    /**
     * 获取手机Android版本
     *
     * @return Android版本
     */
    public static String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE == null ? "" : Build.VERSION.RELEASE;
    }

    /**
     * 获取App版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 0代表相等，1代表currentVersion大于serverVersion，-1代表currentVersion小于serverVersion
     *
     * @param currentVersion App当前版本号
     * @param serverVersion  获取服务器版本号
     * @return 0或1或-1
     */
    public static int compareVersion(String currentVersion, String serverVersion) {
        if (currentVersion.equals(serverVersion)) {
            return 0;
        }
        String[] currentVersionArray = currentVersion.split("\\.");
        String[] serverVersionArray = serverVersion.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = (currentVersionArray.length <= serverVersionArray.length) ? currentVersionArray.length : serverVersionArray.length;
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(currentVersionArray[index]) - Integer.parseInt(serverVersionArray[index])) == 0) {
            index++;
        }
        // 如果位数不一致，比较多余位数
        if (diff == 0) {
            for (int i = index; i < currentVersionArray.length; ++i) {
                if (Integer.parseInt(currentVersionArray[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < serverVersionArray.length; ++i) {
                if (Integer.parseInt(serverVersionArray[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * app版本比较
     *
     * @param versionServer
     *            服务器版本号
     * @param versionLocal
     *            当前版本号
     * @return if versionServer > versionLocal, return 1, if equal, return 0,
     *         else return -1
     */
    public static int versionComparison(String versionServer, String versionLocal) {
        String version1 = versionServer;
        String version2 = versionLocal;
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");

        int index1 = 0;
        int index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
            int[] number2 = getValue(version2, index2);

            if (number1[0] < number2[0]) {
                return -1;
            } else if (number1[0] > number2[0]) {
                return 1;
            } else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return 0;
        if (index1 < version1.length())
            return 1;
        else
            return -1;
    }

    /**
     *
     * @param version
     * @param index
     *            the starting point
     * @return the number between two dots, and the index of the dot
     */
    public static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

}
