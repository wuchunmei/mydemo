package com.wofang.demo.cache;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Date;


public class CacheContentHelper {
	public static final String DB_NAME = "cache.db";
    @SuppressLint("ResourceType")
	private static String getAuthority(){
    	return "com.example.demo.provider.cache";
    }
    public static Uri getCacheContentUri(Context context){
    	String authority=getAuthority();
    	return Uri.parse("content://" + authority + "/" + CacheContentProvider.CACHE_PATH);
    }

	/**
	 * 清除缓存和临时数据，包括Blog,Note,Book等数据
	 * @param context
	 */
	public static void clearAllCache(Context context){
		try{

			ArrayList<ContentProviderOperation> operations=new ArrayList<ContentProviderOperation>();
			operations.add(ContentProviderOperation.newDelete(getCacheContentUri(context)).build());
			
			context.getContentResolver().applyBatch(getAuthority(), operations);
								
		}catch(Throwable ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 清除过期的缓存和临时数据，包括Blog,Note,Book等数据
	 * @param context
	 */
	public static void clearAxpiredCache(Context context){
		try{
			Date date=new Date();
			long time=date.getTime()-20*DateUtils.DAY_IN_MILLIS;//20天前的数据
			//CacheContentHelper.getBlogContentUri(context)
			String[] args=new String[]{String.valueOf(time)};
			ArrayList<ContentProviderOperation> operations=new ArrayList<ContentProviderOperation>();
			operations.add(ContentProviderOperation.newDelete(getCacheContentUri(context))
					.withSelection("TIME_STAMP<?", args).build());
			context.getContentResolver().applyBatch(getAuthority(), operations);
		}catch(Throwable ex){
			ex.printStackTrace();
		}
	}
}
