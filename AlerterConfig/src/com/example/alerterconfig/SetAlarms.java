package com.example.alerterconfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.io.StringReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



/*
 * Service will read new file, and possibly update database. 
 * It will then read database to see if there's current quiz to be administered.
 * If so, it administers it. 
 * Otherwise, waits for the next pass. 
 * Handler will go through the passes on some kind of routine. 
 */



public class SetAlarms extends Service
{	  
	
	Context context;
	AlarmManager am;
	

	@Override
    public int onStartCommand(Intent i, int flags, int startId) {
    
		//there has to be two different intent receptions. one is for alerter reception, other is for
		//reboot reception. Both will start a new alarm. Alerter reception will delete the first entry,
		//reboot reception will not. 
		SharedPreferences settings = context.getSharedPreferences("settings",0);
    	SharedPreferences.Editor editor = settings.edit();
		Bundle b = i.getExtras();
		
		boolean currentlyTriggering = false;

		if(b!= null){
			
			boolean triggeredFromAlerter = b.getBoolean("triggeredFromAlerter"); //if not, it was triggered by reboot.
			
			//Alerter has triggered due to alarm for this entry, now remove it.
	        if(triggeredFromAlerter == true){
	            //Toast.makeText(context, "Entry Deleted", 5).show();
	        	editor.putBoolean("alarmSet", false);
	        	editor.commit();
	        	deleteFirstEntry();
	        	currentlyTriggering = true;
	        }
	        else{
	        	
	        	editor.putBoolean("alarmSet", false);
	        	editor.commit();
	        }  
		}
		
		
        if(settings.getBoolean("running",false) == true){
        	//no alarm is currently set; set one if possible. 
            if(!alarmAlreadySet()){
    			String info[] = getQuizNameAndTime();
    			
    			if(info != null){
    				String quiz = info[0];
    				String triggerTime = info[1];
    				
    				Intent intent = new Intent();
    				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    				intent.putExtra("key", quiz);
    				intent.putExtra("vibrate", settings.getBoolean("vibrate",false));
    				intent.putExtra("flash", settings.getBoolean("flash",false));
    				intent.putExtra("sound", settings.getBoolean("sound",false));
    				intent.putExtra("soundSource", settings.getString("soundSource",""));
    				intent.setComponent(new ComponentName("com.ema.alerter", "com.ema.alerter.AlerterActivity"));
    				
    				PendingIntent sender = PendingIntent.getActivity(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    				Calendar cal = getCalendarAtTime(triggerTime, currentlyTriggering);

    		        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    		        
    		        //SharedPreferences settings = context.getSharedPreferences("settings",0);
    	        	//SharedPreferences.Editor editor = settings.edit();
    	        	editor.putBoolean("alarmSet", true);
    	        	editor.commit();
    	        	
    			}
    			
    			
    		}
        }
		
 
        this.stopSelf();
        return 0;
    }
	
		@Override
		public void onCreate(){
			super.onCreate();
			//you have an alarm manager; use it to set alarms for everything on the file at the given times. 
			context = this;
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
		}
	

		@Override
		   public IBinder onBind(Intent arg0) {
		        return null;
		    }
		
		boolean alarmAlreadySet(){
			SharedPreferences settings = context.getSharedPreferences("settings",0);
			return settings.getBoolean("alarmSet", false);
		}
		
		/*
		 * you MUST take time overlaps into account. The problem occurs when two triggers occur
		 * simultaneously, ie while an alert is being presented to user, another one is triggered
		 * due to some strange happenings. This cannot happen! Should be simple enough to prevent
		 * though: you know that an alert runs for 1.5 minutes, therefore you will never encounter
		 * this problem if you make sure there is at LEAST a delay of 1.5+ minutes in the new alarm
		 * trigger. 
		 */
		Calendar getCalendarAtTime(String time, boolean currentlyTriggering){ //yyyyMMddHHmm

			int targetYear = Integer.parseInt(time.substring(0,4));
			int targetMonth = Integer.parseInt(time.substring(4,6));
			int targetDay = Integer.parseInt(time.substring(6,8));
			int targetHour = Integer.parseInt(time.substring(8,10));
			int targetMinute = Integer.parseInt(time.substring(10));
			
			Calendar targetCal = Calendar.getInstance();
			targetCal.set(targetYear, targetMonth-1, targetDay, targetHour, targetMinute);

			Calendar currCal = Calendar.getInstance();
			
			int diffMinute = (int) ((targetCal.getTimeInMillis() - currCal.getTimeInMillis())/(1000*60));
			int currSecond = currCal.get(Calendar.SECOND);
			int diffSecond = (diffMinute*60) - currSecond;
			
			if(currentlyTriggering == true){
				//an alert is currently in progress. make sure the new alert doesn't land for at least another 1.5 minutes. 
				if(diffSecond < 100){
					diffSecond = 100;
				}
			}
			else{
				if(diffSecond < 0) {
					diffSecond = 0;
				}
			}
			
			
			notificationMessage("Alarm set to ring in " + diffMinute + " minutes" );
			currCal.add(Calendar.SECOND, diffSecond + 1);

			return currCal;
		}
		
		 void deleteFirstEntry(){
			 DataSQLHelper eventsData = new DataSQLHelper(this);
		  	    SQLiteDatabase db = eventsData.getReadableDatabase();
		  	  Cursor c = db.query(DataSQLHelper.TABLE, null, null, null, null,
						null, null);
		  	  c.moveToFirst();
		  	  deleteEntry(c.getInt(0)+"");
		 }
		    
		    
		    void deleteEntry(String id){
		    	DataSQLHelper eventsData = new DataSQLHelper(this);
		    	SQLiteDatabase writer = eventsData.getWritableDatabase();
		    	writer.delete(DataSQLHelper.TABLE, "_ID="+id, null);
		    	writer.close();
		    	eventsData.close();
		    }
		    
		    //run through the database and find the first quiz entry that hasn't expired. 
		    //for quizzes that have expired, the method will delete those entries. The quiz
		    //will be set to trigger at the start time of the quiz entry. 
			String[] getQuizNameAndTime(){
				DataSQLHelper eventsData = new DataSQLHelper(this);
		  	    SQLiteDatabase db = eventsData.getReadableDatabase();
		  	  final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		  	  Calendar cal = Calendar.getInstance();
		  	  long currTime = Long.parseLong(sdf.format(cal.getTime()));
		  	  Cursor c = db.query(DataSQLHelper.TABLE, null, null, null, null,
						null, null);
		  	  
		  	  long startTime, endTime;
		  	  while(c.moveToNext()){
		  		  startTime = c.getLong(1);
		  		  endTime = c.getLong(2);

		  			  if(currTime < endTime){
		  				String[] result = new String[2]; //{quizName, startTime}
		  				result[0] = c.getString(3);
		  				result[1] = startTime+"";
		  				
			  			  return result;
		  			  }
		  			  
		  			  else{
		  				  deleteEntry(c.getInt(0)+"");
		  			  }
		  		  
		  	  }
		  	  
		  	  //no new quiz to administer.
				return null;
			}
			
			void notificationMessage(String message){
				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
				int icon = R.drawable.config;
				CharSequence tickerText = "New Alarm Set";
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText, when);
				Context context = getApplicationContext();
				CharSequence contentTitle = "";
				CharSequence contentText = message;
				Intent notificationIntent = new Intent(this, AlerterConfigActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
				int HELLO_ID = 1;

				mNotificationManager.notify(HELLO_ID, notification);
			}
			
			
		
	}
		    

