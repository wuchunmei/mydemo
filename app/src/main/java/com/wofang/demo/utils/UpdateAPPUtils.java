package com.wofang.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import java.io.File;

public class UpdateAPPUtils {

    /**
     * 版本比较
     * @param version 服务器版本号
     * @return 是否需要更新
     */
    public static boolean isUpdateVersoin(Context context, String version) {
        String oldVersion = AppUtils.getVersionName(context);
        int i = AppUtils.versionComparison(version, oldVersion);

        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 安装新版本
     */
    public static void installAPK(Context context, File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 7.0 以下
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        } else { // 7.0 以上
            Uri contentUri = FileProvider.getUriForFile(context, "com.wofang.demo.mydemo.FileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

}
