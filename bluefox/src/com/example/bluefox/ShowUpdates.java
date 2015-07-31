package com.example.bluefox;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//This class displays update info; in other words, a track of all the going on's in the phone such as
//error messages and sending data. 

public class ShowUpdates extends Activity{
	private DataSQLHelper eventsData;
	private Button back;
	private Context appContext;
	private TextView output;
	private SharedPreferences settings;
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.showinfo);
	        
	        eventsData = new DataSQLHelper(this);
	        back = (Button)findViewById(R.id.back);
	        appContext = this;
	        output = (TextView)findViewById(R.id.output2);
	        settings = this.getSharedPreferences("settings",MODE_WORLD_READABLE);
	        showUpdates();
	        back.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			Intent i = new Intent(appContext, bluefox.class);
	     			startActivity(i);
	    		}
	        });
	  }
	
	  private void showUpdates()
	  {
		  Cursor cursor = getCursor();
		  String ret = "";
		  int count =0;
		  while(cursor.moveToNext())
		  {
			  String time =cursor.getString(1);
			  String update = cursor.getString(3);
			  int type = cursor.getInt(2);
			  if((settings.getString("last20", "Y").equals("Y")&&count<20)||(settings.getString("last20", "Y").equals("N")))
			  {
				  if((settings.getString("logError", "Y").equals("Y")&&type==0)||(settings.getString("logMessageStatus", "Y").equals("Y")&&type==1))
				  {
					  ret += update+"\n"+time+"\n\n";
					  count++;
				  }
			  } 
		  }
		  output.setText(ret);
	  }
	  
	  private Cursor getCursor() {
	    	
	    	SQLiteDatabase db = eventsData.getReadableDatabase();
	    	Cursor cursor = db.query(DataSQLHelper.TABLE2, null, null, null, null,
	    	        null, null);
	    	 startManagingCursor(cursor);
	    	    return cursor;
	  }
}
