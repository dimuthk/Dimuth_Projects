package com.farmfox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

//you just need the update log for now.
/** Helper to the database, manages versions and creation */
public class DataSQLHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;


	/*
	 * table should state the subscribers, and all the things each subscriber has subscribed to. 
	 */
	
	
	public static final String TABLE1 = "subscription_table";

	//columns table 1
	public static final String SUBSCRIPTION = "subscription";
	public static final String USERS = "users";

	

	public DataSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


		String sql1 = "create table " + TABLE1 + "( " + BaseColumns._ID
		+ " integer primary key autoincrement, " + SUBSCRIPTION + " text not null, " + USERS + " text not null);";
		

		db.execSQL(sql1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		String sql = null;
		if (oldVersion == 1) 
			sql = "alter table " + TABLE1 + " add note text;";

		if (oldVersion == 2)
			sql = "";

		Log.d("EventsData", "onUpgrade	: " + sql);
		if (sql != null)
			db.execSQL(sql);
	}
}