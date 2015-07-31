package com.example.bluefox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

//This class lets you change various settings on the phone. Specifically, you set the time at which
//breakfast, lunch, or dinner messages are sent, the days on which messages will be sent, 
//the URL's for message and group info, and the type of information the update log presents. 

public class Settings extends Activity
{
	private Context appContext;
	private Button back, changeDistrRate, changeUpdateRate, changeMessageURL,
	resetMessageURL, changeDrupalURL, resetDrupalURL, daysToSend, configureLog,
	clearUpdateLog;

	private TextView distrRate,updateRate,messageUrl, drupalUrl;
	private SharedPreferences settings;
	private Intent distributeMessages;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.settings);  
	        appContext=this;
	        back = (Button) findViewById(R.id.back);
	        changeDistrRate = (Button) findViewById(R.id.change_distr_rate);
	        changeUpdateRate = (Button) findViewById(R.id.change_update_rate);
	        changeMessageURL = (Button) findViewById(R.id.change_message_url);
	        resetMessageURL = (Button) findViewById(R.id.reset_message_url);
	        
	        changeDrupalURL = (Button) findViewById(R.id.change_drupal_url);
	        resetDrupalURL = (Button) findViewById(R.id.reset_drupal_url);
	        
	        configureLog = (Button) findViewById(R.id.configureLog);
	        daysToSend = (Button) findViewById(R.id.days_to_send);
	        messageUrl = (TextView) findViewById(R.id.messageURL_info);
	        
	        drupalUrl = (TextView) findViewById(R.id.drupalURL_info);
	        distrRate = (TextView) findViewById(R.id.message_distr_rate);
	        updateRate = (TextView) findViewById(R.id.message_update_rate);
	        clearUpdateLog = (Button)findViewById(R.id.clear_update_log);
	        distributeMessages = new Intent(this,DistributeMessages.class);
	        settings = this.getSharedPreferences("settings",MODE_WORLD_READABLE);
	        distributeMessages = new Intent(this,DistributeMessages.class);
	        
	        /////////////////////////////////////////////////////////////

	        distrRate.setText("Messages being sent every " + settings.getInt("messageDistrRate", -1)+ " ms.");
	        updateRate.setText("New messages being added every " + settings.getInt("updateMsgRate", -1)+ " ms.");
	        messageUrl.setText("Messages being added from " + settings.getString("message_url", ""));
	        
	        back.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			Intent i = new Intent(appContext, bluefox.class);
	     			startActivity(i);
	    		}
	        });
	        
	        //lets you configure update log settings
	        configureLog.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View view) {

	    	        final CharSequence[] items = {"Display error messages","Display message statuses","Display only most recent 20 updates"};
	    	         
	    	        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    	        builder.setTitle("Configure settings");
	    	        builder.setCancelable(true);

	    	        builder.setMultiChoiceItems(items, configureLogMarks(), new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						changeLogMark(which,isChecked);						
					}
	    	         });
	    	         AlertDialog alert = builder.create();
	    	         alert.show();   
	    		}
	        });
	        
	        //lets you configure which days to send messages on
	        daysToSend.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View view) {

	    	        final CharSequence[] items = {"Monday","Tuesday","Wednesday",
	    	        		 "Thursday","Friday","Saturday","Sunday"};
	    	         
	    	        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    	        builder.setTitle("Set days on which to send messages");
	    	        builder.setCancelable(true);

	    	        builder.setMultiChoiceItems(items, configureCheckMarks(), new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						changeCheckMark(which,isChecked);						
					}
	    	         });
	    	         AlertDialog alert = builder.create();
	    	         alert.show();   
	    		}
	        });
	        
	      //clears the update log
	        clearUpdateLog.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			builder.setMessage("This will permanently delete all entries in the update log. Are you sure?")
		 	 		
		 			.setCancelable(false)
		 			.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				clearUpdateLog();
		 			}})
		 			.setNegativeButton("No", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
		 	 		AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });
	        
	      //change distribution rate
	        changeDistrRate.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			
		 	 		final EditText input = new EditText(appContext);
		 	 		builder.setView(input)
		 	 		.setMessage("Set the distribution rate in milliseconds.")
		 	 		.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				changeRates(input.getText().toString(),false);
		 				distrRate.setText("Messages being sent every " + settings.getInt("messageDistrRate", -1)+ " ms.");
		 				stopService(distributeMessages);
		 				startService(distributeMessages);
		 				SharedPreferences.Editor editor = settings.edit();
		 				editor.putInt("messageInterrupt", 0);
		 				editor.commit();
		 				
		 			}})
		 			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
	    			AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });
	        
	      //change update rate
	        changeUpdateRate.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			
		 	 		final EditText input = new EditText(appContext);
		 	 		builder.setView(input)
		 	 		.setMessage("Set the update rate in milliseconds.")
		 	 		.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				changeRates(input.getText().toString(),true);
		 				updateRate.setText("New messages being added every " + settings.getInt("updateMsgRate", -1)+ " ms.");
		 			}})
		 			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
	    			AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });

	        
	    
	       
	      //change update rate
	        changeMessageURL.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			
		 	 		final EditText input = new EditText(appContext);
		 	 		builder.setView(input)
		 	 		.setMessage("Set the new URL.")
		 	 		.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				changeURL(input.getText().toString());
		 				messageUrl.setText("Messages being added from " + settings.getString("message_url", ""));
		 			}})
		 			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
	    			AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });

	        
	        //resets the message url to its original line
	        resetMessageURL.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			builder.setMessage("The message URL will be reset to http://www.akshenweb.org/factoids/gateway/rss.xml. Are you sure?")
		 	 		
		 			.setCancelable(false)
		 			.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				changeURL("http://www.akshenweb.org/factoids/gateway/rss.xml");
		 				messageUrl.setText("Messages being added from " + settings.getString("message_url", ""));
		 			}})
		 			.setNegativeButton("No", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
		 	 		AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });
	        
	        //change update rate
	        changeDrupalURL.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			
		 	 		final EditText input = new EditText(appContext);
		 	 		builder.setView(input)
		 	 		.setMessage("Set the new URL.")
		 	 		.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				changeURL(input.getText().toString());
		 				messageUrl.setText("Server website is " + settings.getString("drupal_url", ""));
		 			}})
		 			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
	    			AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });

	        
	        //resets the message url to its original line
	        resetDrupalURL.setOnClickListener(new OnClickListener()
	        {
	    		@Override
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
	    			builder.setMessage("The drupal URL will be reset to http://www.akshenweb.org. Are you sure?")
		 	 		
		 			.setCancelable(false)
		 			.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
		 			@Override
		 			public void onClick(DialogInterface arg0, int arg1) {
		 				changeURL("http://www.akshenweb.org");
		 				messageUrl.setText("Server website is " + settings.getString("drupal_url", ""));
		 			}})
		 			.setNegativeButton("No", new DialogInterface.OnClickListener() {
		 		           public void onClick(DialogInterface dialog, int id) {
		 		                dialog.cancel();
		 		           }
		 		       });
		 	 		AlertDialog alert = builder.create();
		 			alert.setTitle("Confirm Change");
		 			alert.show();	
	    		}
	        });
	        
	        
	    	       
	 }
	 
	private void changeRates(String text, boolean update)
	{
		int value = Integer.parseInt(text);
		SharedPreferences.Editor editor=settings.edit();
		if(update) editor.putInt("updateMsgRate", value);
		else editor.putInt("messageDistrRate", value);
		 editor.commit();
	}
	 
	 private void changeURL(String text)
	 {
		 SharedPreferences.Editor editor=settings.edit();
		 editor.putString("message_url", text);
		 editor.commit();
	 }
	 

	 
	 private boolean[] configureCheckMarks()
	 {
		 boolean[] result = new boolean[7];
		 if(settings.getString("mon","").equals("Y"))result[0]=true; else result[0]=false;
		 if(settings.getString("tue","").equals("Y"))result[1]=true; else result[1]=false;
		 if(settings.getString("wed","").equals("Y"))result[2]=true; else result[2]=false;
		 if(settings.getString("thu","").equals("Y"))result[3]=true; else result[3]=false;
		 if(settings.getString("fri","").equals("Y"))result[4]=true; else result[4]=false;
		 if(settings.getString("sat","").equals("Y"))result[5]=true; else result[5]=false;
		 if(settings.getString("sun","").equals("Y"))result[6]=true; else result[6]=false;
		 return result;
	 }
	 
	 private boolean[] configureLogMarks()
	 {
		 boolean[] result = new boolean[3];
		 if(settings.getString("logError","").equals("Y"))			result[0]=true; else result[0]=false;
		 if(settings.getString("logMessageStatus","").equals("Y"))	result[1]=true; else result[1]=false;
		 if(settings.getString("last20","").equals("Y"))			result[2]=true; else result[2]=false;
		 return result;
	 }
	 
	 private void changeCheckMark(int day, boolean isChecked)
	 {
		 String yesOrNo;
		 if(isChecked) yesOrNo="Y"; else yesOrNo="N";
		 SharedPreferences.Editor editor=settings.edit();
		 switch(day)
		 {
		 	case 0: editor.putString("mon", yesOrNo); break;
		 	case 1: editor.putString("tue", yesOrNo); break;
		 	case 2: editor.putString("wed", yesOrNo); break;
		 	case 3: editor.putString("thu", yesOrNo); break;
		 	case 4: editor.putString("fri", yesOrNo); break;
		 	case 5: editor.putString("sat", yesOrNo); break;
		 	case 6: editor.putString("sun", yesOrNo); break;
		 }
		 editor.commit();
	 }
	 
	 private void changeLogMark(int item, boolean isChecked)
	 {
		 String yesOrNo;
		 if(isChecked) yesOrNo="Y"; else yesOrNo="N";
		 SharedPreferences.Editor editor=settings.edit();
		 switch(item)
		 {
		 	case 0: editor.putString("logError", yesOrNo); break;
		 	case 1: editor.putString("logMessageStatus", yesOrNo); break;
		 	case 2: editor.putString("last20", yesOrNo); break;
		 }
		 editor.commit();
	 }
	 
	//clears message table 
	  private void clearUpdateLog()
	  {
		  DataSQLHelper eventsData = new DataSQLHelper(this);
		  SQLiteDatabase db = eventsData.getWritableDatabase();
		  	db.delete(DataSQLHelper.TABLE2,null, null);
	  }
	 
	 
}
