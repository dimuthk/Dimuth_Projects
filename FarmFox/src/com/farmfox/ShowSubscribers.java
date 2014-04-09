package com.farmfox;

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

//This class displays message info; in other words, the message and other data written in the
//message URL. 

public class ShowSubscribers extends Activity {


	private TextView output;
	private Button back;
	private Context appContext;
	SharedPreferences settings;
    /** Called when the activity is first created. */
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinfo);
        

        back = (Button)findViewById(R.id.back);
        
        back.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent i = new Intent(appContext, FarmFoxActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
        	
        });
        
        appContext = this;
        output = (TextView)findViewById(R.id.output2);
        settings = this.getSharedPreferences("settings",0);
        showMessages();
        
    }
    
  //works correctly
   private void showMessages()
    {
	   DataSQLHelper eventsData = new DataSQLHelper(this);
	   SQLiteDatabase read = eventsData.getReadableDatabase();
	   Cursor cursor = read.query(DataSQLHelper.TABLE1, null, null, null, null,null, null);
	    startManagingCursor(cursor);
    	String ret = "";
    	int size = cursor.getCount();
    	//if(size > 200) cursor.moveToPosition(size-200);
        while (cursor.moveToNext()) {
        	  String subscription = cursor.getString(1);
        	  String users = cursor.getString(2);
              ret += subscription + "\t" + users + "\n\n";       
        }
        read.close();
        eventsData.close();
        ret = "Current Subscribers\n\n"+ret + size;
        output.setText(ret);
    }
    
}