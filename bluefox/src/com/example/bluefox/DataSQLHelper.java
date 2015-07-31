package com.example.bluefox;

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
	public static final String TABLE1 = "messages";	//store your message queue here
	public static final String TABLE2 = "updates";  //any updates from the phone here
	public static final String TABLE3 = "temp";  //temporary place for newer messages. 
	
	//columns table 1
	public static final String START_TIME = "sendtime"; //the start of the time range where the message can be sent
	public static final String END_TIME = "range"; //the end of that time span (both should be written as a date)
	public static final String MESSAGE = "message"; //the message itself
	public static final String DESTINATION = "destination"; //the message itself
	public static final String NID = "nid"; //the message itself
	//public static final String READY = "ready"; //message range has started?
	//thats it! update table is the same. 
	
	
	//Columns table 2
	public static final String TIME = "time";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	
	//Columns table 3
	public static final String START_TIME2 = "sendtime2"; //the start of the time range where the message can be sent
	public static final String END_TIME2 = "range2"; //the end of that time span (both should be written as a date)
	public static final String MESSAGE2 = "message2"; //the message itself
	public static final String DESTINATION2 = "destination2"; //the message itself
	public static final String NID2 = "nid2"; //the message itself
	
	public DataSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		String sql1 = "create table " + TABLE1 + "( " + BaseColumns._ID
		+ " integer primary key autoincrement, " + START_TIME + " text not null, " 
		+ END_TIME + " text not null, " + MESSAGE + " text not null, " + DESTINATION + " text not null, "
		+ NID +  " text not null);";
	
		String sql2 = "create table " + TABLE2 + "( " + BaseColumns._ID
		+ " integer primary key autoincrement, " + TIME + " text not null, "
	    + TYPE + " integer, " + STATUS + " text not null);";
		
		String sql3 = "create table " + TABLE3 + "( " + BaseColumns._ID
		+ " integer primary key autoincrement, " + START_TIME2 + " text not null, " 
		+ END_TIME2 + " text not null, " + MESSAGE2 + " text not null, " + DESTINATION2 + " text not null, "
		+ NID2 + " text not null);";

		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
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

