package com.wofang.demo.utils;

import android.provider.BaseColumns;

public class ContentProviderUtil {

	public static final String DEFAULT_ORDER="_id";
    /**
     * The MIME type of  providing a directory of configs.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

    /**
     * The MIME type of a  sub-directory of a single note.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";
    
    public static final String[] ID_PROJECTION=new String[]{
    	BaseColumns._ID};
}
