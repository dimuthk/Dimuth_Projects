package com.example.alerterconfig;

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

	// Table name
	public static final String TABLE = "table1";	//store your message queue here

	
	//columns table 1
	public static final String START_TIME = "sendtime"; //the start of the time range where the message can be sent
	public static final String END_TIME = "endtime"; //the end of that time span (both should be written as a date)
	public static final String QUIZ_TO_RUN = "quizToRun"; //the message itself

	
	
	public DataSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	public void onCreate(SQLiteDatabase db) {
		
		
		String sql1 = "create table " + TABLE + "( " + BaseColumns._ID
		+ " integer primary key autoincrement, " + START_TIME + " long, " 
		+ END_TIME + " long, " 
		+ QUIZ_TO_RUN + " text not null);";
	

		db.execSQL(sql1);

	}

	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		String sql = null;
		if (oldVersion == 1) 
			sql = "alter table " + TABLE + " add note text;";

		if (oldVersion == 2)
			sql = "";

		Log.d("EventsData", "onUpgrade	: " + sql);
		if (sql != null)
			db.execSQL(sql);
	}
}

