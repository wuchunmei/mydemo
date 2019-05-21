package com.wofang.demo.cache;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;


import com.wofang.demo.utils.ContentProviderUtil;

import java.util.Map;


/**
 * 缓存ContentProvider
 *
 */
public class CacheContentProvider extends ContentProvider {
    private static final String TAG = "CacheContentProvider";
    public static final String CACHE_PATH = "caches";
    private static final int CACHES = 1;
    private static final int CACHE_ID = 2;

    
    protected SQLiteOpenHelper mOpenHelper;

    public static final String[] ID_PROJECTION=new String[]{
    	BaseColumns._ID};

    private static final String DEFAULT_ODEDER=BaseColumns._ID + " DESC";
    

    private static Map<String, String> sCacheColumnItemsProjectionMap;
    private static UriMatcher sUriMatcher;
    static{
    	sCacheColumnItemsProjectionMap=CacheDataManager.createCacheColumnItems();
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new SQLiteCacheDataBaseHelper(getContext());
        return true;
    }
    @Override
    public void attachInfo (Context context, ProviderInfo info) {
    	super.attachInfo(context, info);
  
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        sUriMatcher.addURI(info.authority, CACHE_PATH, CACHES);
        sUriMatcher.addURI(info.authority, CACHE_PATH + "/#", CACHE_ID);

        
    }
    protected int matchType(Uri uri){
    	if (sUriMatcher!=null){
    		return sUriMatcher.match(uri);
    	}
    	String urlText=uri.toString();
    	if (urlText.endsWith(CACHE_PATH)){
    		return CACHES;
    	}
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (matchType(uri)) {
            case CACHES:{
                count = db.delete(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE, where, whereArgs);
                break;
            }
            case CACHE_ID:{
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE, "ROWID="
                        + rowId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
        switch (matchType(uri)) {
	        case CACHES:
	            return ContentProviderUtil.CONTENT_TYPE;

	        case CACHE_ID:
	            return ContentProviderUtil.CONTENT_ITEM_TYPE;
	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
        if (initialValues==null){
        	throw new IllegalArgumentException("initialValues can not be null");
        }         
        int matchType = matchType(uri);
        // Long now = Long.valueOf(System.currentTimeMillis());
    	ContentValues values= new ContentValues(initialValues);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId =0;
        if (matchType == CACHES){
	        rowId = db.insert(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE, null, values);
	        if (rowId > 0) {
	            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
	            getContext().getContentResolver().notifyChange(noteUri, null);
	            return noteUri;
	        }
        }
        throw new SQLException("Failed to insert row into " + uri);
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String having=null;
        boolean isConfig = false;
        switch (matchType(uri)) {
            case CACHES:
                qb.setTables(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE);
                qb.setProjectionMap(sCacheColumnItemsProjectionMap);
                isConfig = true;
                break;
            case CACHE_ID:
                qb.setTables(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE);
                qb.setProjectionMap(sCacheColumnItemsProjectionMap);
                isConfig = true;
                qb.appendWhere("ROWID=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy = null;
        if (isConfig) {
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = DEFAULT_ODEDER;
            } else {
                orderBy = sortOrder;
            }
        }
        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, having, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source Data
        // changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (matchType(uri)) {
            case CACHES:{
                count = db.update(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE, values, where, whereArgs);
                break;
            }
            case CACHE_ID:{
                String noteId = uri.getPathSegments().get(1);
                count = db.update(SQLiteCacheDataBaseHelper.DATABASE_TABLE_CACHE, values,"ROWID=" + noteId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

}
