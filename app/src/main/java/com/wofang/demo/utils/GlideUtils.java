package com.wofang.demo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Driver;

/**
 * 自定义Glide图片加载
 */
public class GlideUtils {

    public static void disPlayerImageView(Context context, ImageView imageView, String url, int imgDrawable) {
        RequestOptions requestOptions = new RequestOptions()
                // 加载成功之前占位图
                .placeholder(imgDrawable)
                // 加载错误之后的错误图
                .error(imgDrawable);
        Glide.with(context).load(url).apply(requestOptions).into(imageView);
    }

    public static void disPlayerImageView(Context context, ImageView imageView, String url, Drawable imgDrawable) {
        RequestOptions requestOptions = new RequestOptions()
                // 加载成功之前占位图
                .placeholder(imgDrawable)
                // 加载错误之后的错误图
                .error(imgDrawable);
        Glide.with(context).load(url).apply(requestOptions).into(imageView);
    }
}
