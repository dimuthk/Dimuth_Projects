package com.farmfox;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * TODO:
 * Convert "view subscriptions" page into a list view. List view should have the
 * subscription itself in big letters and the subscribers in smaller. Be able to 
 * click on a given subscription and have the choice to add/remove users, or delete
 * the subscription altogether. The viewing page should also be the one that allows
 * you to add new subscriptions; the list should update itself accordingly. Don't let
 * "" be a subscription!
 * 
 *  Also, have auto-messages sent from the message processor to users. ex: LIST command
 *  should send back a list of all currently subscriptions available, (UN)SUBSCRIBE should 
 *  notify that they've been added/removed from the subscription, or if they're already not
 *  on/off the list.  
 */

public class FarmFoxActivity extends Activity {
    Context context = this;
    ArrayList<String> tags;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button a = (Button) findViewById(R.id.add_subscription);
        Button c = (Button) findViewById(R.id.subscribers);
        Button tag = (Button) findViewById(R.id.add_tags);
        Button send = (Button) findViewById(R.id.send);
        final EditText text = (EditText) findViewById(R.id.subscription_name);
        final EditText message = (EditText) findViewById(R.id.message);
        tags = new ArrayList<String>();
        a.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				addSubscription(text.getText().toString());
			}
        	
        });
        
        c.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent i = new Intent(context, ShowSubscribers.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
        	
        });
        
        tag.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	        builder.setTitle("Select Tags");
    	        builder.setCancelable(true);
    	        final String[] list = subscriptionList();
    	        builder.setMultiChoiceItems(list, tagList(list), new DialogInterface.OnMultiChoiceClickListener() {


				public void onClick(DialogInterface dialog, int which,
						boolean isChecked) {
						if(isChecked == true){
							if(tags.contains(list[which]) == false){
								tags.add(list[which]);
							}	
						}
						else{
							tags.remove(list[which]);
						}
				}
    	         });
    	        
    	        builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
	 		           public void onClick(DialogInterface dialog, int id) {
	 		                dialog.cancel();
	 		           }
	 		       });
    	        
    	         AlertDialog alert = builder.create();
    	         alert.show(); 
			}
        	
        });
        
        send.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				sendSMS(message.getText().toString());
			}
        	
        });

    }
    
    void sendSMS(String message){
    	ArrayList<String> destinations = new ArrayList<String>();
    	DataSQLHelper eventsData = new DataSQLHelper(this);
	    SQLiteDatabase reader = eventsData.getReadableDatabase();
	    
	    SmsManager sm = SmsManager.getDefault();

	    
	    Cursor cursor = reader.query(DataSQLHelper.TABLE1, null, null, null, null,
    	        null, null);
    	cursor.moveToFirst();
    	for(int i=0; i<tags.size();i++){
    		while(cursor.getString(1).equals(tags.get(i))==false){
    			cursor.moveToNext();
    		}
    		String[] contents = cursor.getString(2).split(";");
    		for(String sub: contents){
    			if(sub.equals("") == false && destinations.contains(sub) == false){
    				destinations.add(sub);
    			}	
    		}
    	}
    	
    	tags.clear();
    	
    	if(destinations.size() == 0){
    		Toast.makeText(context, "No destinations to send to!", Toast.LENGTH_SHORT).show();
    		reader.close();
    		eventsData.close();
    		return;
    	}
    	
    	for(String dest: destinations){
    		try{
    			sm.sendTextMessage(dest, null, message, null, null);
    		}
    		catch(Exception e){
    			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
    		}
    	}
    	Toast.makeText(context, "Message broadcasted to " + destinations.size() + " people!", Toast.LENGTH_SHORT).show();
    	reader.close();
		eventsData.close();
    }
    
    boolean[] tagList(String[] list){
    	boolean[] listTags = new boolean[list.length];
    	for(int i=0; i<list.length; i++){
    		String word = list[i];
    		if(tags.contains(word)){
    			listTags[i] = true;
    		}
    		else{
    			listTags[i] = false;
    		}
    	}
    	return listTags;
    }
    
    String[] subscriptionList(){
    	DataSQLHelper eventsData = new DataSQLHelper(this);
	    SQLiteDatabase reader = eventsData.getReadableDatabase();
	    Cursor cursor = reader.query(DataSQLHelper.TABLE1, null, null, null, null,
    	        null, null);
	    String[] list = new String[cursor.getCount()];
	    for(int i=0; i<list.length; i++){
	    	cursor.moveToNext();
	    	list[i] = cursor.getString(1);
	    }
	    return list;
    }
    
    void addSubscription(String subscription){
    	if(subscriptionExists(subscription)){
    		Toast.makeText(context, "The subscription " + subscription + " already exists.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	DataSQLHelper eventsData = new DataSQLHelper(this);
    	SQLiteDatabase writer = eventsData.getWritableDatabase();
		ContentValues values = new ContentValues();
  	    values.put(DataSQLHelper.SUBSCRIPTION, subscription);
  	    values.put(DataSQLHelper.USERS, "");
  	    writer.insert(DataSQLHelper.TABLE1, null, values);
  	    writer.close();
  	    Toast.makeText(context, "Subscription added!", Toast.LENGTH_SHORT).show();
    }
    
    boolean subscriptionExists(String word){
		DataSQLHelper eventsData = new DataSQLHelper(this);
	    SQLiteDatabase reader = eventsData.getReadableDatabase();
	    Cursor cursor = reader.query(DataSQLHelper.TABLE1, null, null, null, null,
    	        null, null);
	    while(cursor.moveToNext()){
	    	if(cursor.getString(1).equalsIgnoreCase(word)){
	    		reader.close();
	    		eventsData.close();
	    		return true;
	    	}
	    }
	    reader.close();
		eventsData.close();
		return false;
	}
}