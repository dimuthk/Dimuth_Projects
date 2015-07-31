package com.example.bluefox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//This class displays message info; in other words, the message and other data written in the
//message URL. 

public class ShowMessages extends Activity {
	
	private DataSQLHelper eventsData;
	private TextView output;
	private Button back;
	private Context appContext;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinfo);
        
        eventsData = new DataSQLHelper(this);
        back = (Button)findViewById(R.id.back);
        appContext = this;
        output = (TextView)findViewById(R.id.output2);

        showMessages();
        
        back.setOnClickListener(new OnClickListener()
        {
    		@Override
    		public void onClick(View arg0) {
    			Intent i = new Intent(appContext, bluefox.class);
     			startActivity(i);
    		}
        });
    }
    
  //works correctly
   private void showMessages()
    {
    	Cursor cursor = getCursor();
    	String ret = "";
        while (cursor.moveToNext()) {
          String startTime = cursor.getString(1);
          String endTime = cursor.getString(2);
          String message;
          if(cursor.getString(3).length()>50) message = cursor.getString(3).substring(0,45)+"...";
          else message = cursor.getString(3);
          String destination = cursor.getString(4);
          String nid = cursor.getString(5);
          ret += ""+startTime+"\t"+endTime+"\n"+message+"\n"+destination+"\n"+ nid +"\n\n";
        }
        ret = "Publish Date, Send Time (BLD), NID, Sent, Groups, Nessage\n\n"+ret;
        output.setText(ret);
    }
    
   
   
    //return a cursor for the message table
    private Cursor getCursor() {
    	
  	    	SQLiteDatabase db = eventsData.getReadableDatabase();
  	    	Cursor cursor = db.query(DataSQLHelper.TABLE1, null, null, null, null,
  	    	        null, null);
  	    	 startManagingCursor(cursor);
  	    	    return cursor;
  	  }
}