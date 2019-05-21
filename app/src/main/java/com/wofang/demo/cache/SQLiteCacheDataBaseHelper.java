package com.wofang.demo.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteCacheDataBaseHelper extends SQLiteOpenHelper {
	private final static String TAG = "SQLiteCacheDataBaseHelpter";
	
	private static final int DB_VERSION = 1; // 数据库版本
	
	public static final String DATABASE_TABLE_CACHE="TB_CACHE";
	public SQLiteCacheDataBaseHelper(Context context) {
		this(context, CacheContentHelper.DB_NAME, null);
	}

	public SQLiteCacheDataBaseHelper(Context context, String name,
                                     CursorFactory factory) {
		super(context, name, factory, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDB1(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 0) {
			onCreate(db);
		}
//		else if (oldVersion == 1) {
//		}
	}

	private void createDB1(SQLiteDatabase db) {
		final String sqlCache = "CREATE TABLE IF NOT EXISTS TB_CACHE (_id integer primary key autoincrement, KEY nvarchar(200),CACHEDATA blob,TIME_STAMP int64)";
		db.execSQL(sqlCache);
	}
}
