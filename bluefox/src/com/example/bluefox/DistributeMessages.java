package com.example.bluefox;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.SmsManager;



//At x frequency, take a message from the table (which came through the updateMessages service).
//If it has expired, remove it and select another message. If it hasn't expired, send it to its
//destination. 

public class DistributeMessages extends Service
{
	private String DATE_FORMAT = "yyyy-MM-dd HH:MM";
	Timer distributionTimer, waitTimer;
	boolean hour;
	DataSQLHelper eventsData;
	private SharedPreferences settings;
	@Override
	   public IBinder onBind(Intent arg0) {
	        return null;
	    }
	  
	
	    @Override
  public void onCreate() {
	    	super.onCreate();
	    	settings = this.getSharedPreferences("settings",MODE_WORLD_READABLE);
	    	
	    	distributionTimer = new Timer();
	    	TimerTask distribute = new TimerTask()
	    	{
				@Override
				public void run() 
				{
					updateTimeCounters();
					if(canRunToday())
					{ 
						if(settings.getInt("messageInterrupt", 0)==0) distributeMessage();
					}
				}	
	    	};
	    	distributionTimer.scheduleAtFixedRate(distribute,1000*5,settings.getInt("messageDistrRate", 5*1000));	
	   }

	    @Override
	  public void onDestroy() {
	      super.onDestroy();
	      distributionTimer.cancel();
	  }
	    
	    
	    
	    //pop message. if not waiting, send to temporary table. 
	    private String[] grabMessage()
	    {	
	    			DataSQLHelper eventsData = new DataSQLHelper(this);
	    			SQLiteDatabase db = eventsData.getWritableDatabase();
	            	//get id of most recent
	            	Cursor cursor = getCursor(1);
	            	cursor.moveToFirst();
	            	int id = cursor.getInt(0);
	            	String [] contents = new String[6];
	            	contents[0] = cursor.getString(1); //begin time
	            	contents[1] = cursor.getString(2); //end time
	            	contents[2] = cursor.getString(3); //message
	            	contents[3] = cursor.getString(4); //destination
	            	contents[4] = cursor.getString(5); //nid
	            	//contents[5] = cursor.getString(6); //ready
	            	String status = messageWithinTime(contents);
	            	if(status==null) return null;
	            	
	            	db.delete(DataSQLHelper.TABLE1, "_ID="+id, null);
	            	db.close();
	            	
	            	if(status.equals("Expired")) 
	            		{
	            			contents[5] = "Expired"; 
	            			try{postData(contents[4],"NotSent_OutOfTiime");}
	            			catch(Exception e){}
	            		}
	            	else if(status.equals("Ready")) contents[5] = "Ready";
	            	
	            	else if(status.equals("Waiting")) //message is new, add to bottom of message list
	            	{
	            		addMessage(contents[0],contents[1],contents[2],contents[3],contents[4],3);
	            		contents[5] = "Waiting";
	            	}
	            	return contents;
	    }
	    
	  //return a cursor for a table
	    private Cursor getCursor(int i) 
	    {
	    		DataSQLHelper eventsData = new DataSQLHelper(this);
	  	    	SQLiteDatabase db = eventsData.getReadableDatabase();
	  	    	Cursor cursor;
	  	    	if(i==1) cursor = db.query(DataSQLHelper.TABLE1, null, null, null, null,null, null);
	  	    	else if (i==2) cursor = db.query(DataSQLHelper.TABLE2, null, null, null, null,null, null);  
	  	    	else cursor = db.query(DataSQLHelper.TABLE3, null, null, null, null,null, null);  
	  	    	return cursor;
	  	}
	    
	    
	  //take message from database and if possible distributes it. this will indirectly 
		  //clear the table of old messages as well. 
		    private void distributeMessage()
		    {
		    	boolean foundMessage=false;
		    	String[] messageInfo; 
		    	while(foundMessage==false)
		    	{
		    		try{
		    			messageInfo = grabMessage();
		    			}
		    		catch(Exception e) //no more messages in table. 
		    		{
		    			moveAllToMainTable();
		    			return;
		    		}
		    		
		    		if(messageInfo[5].equals("Ready"))
		    			{
		    				foundMessage=true; //found a new message!
		    				try
		    				{
		    					sendSMS(messageInfo[3],messageInfo[2]);
		    					try{postData(messageInfo[4],"Sent");}
		            			catch(Exception e2){}
		    					//incrementCounters();		
		    				}
		    				catch(Exception e)
		    				{
		    					
		    					addUpdate("Could not send message: " + messageInfo[2] + ":\n" + e,0);
		    				}
		    				
		    			}
		    		
		    	}
		    	moveAllToMainTable();
		    }
		    
		  //move all contents of temporary table back to main
		    private void moveAllToMainTable()
		    {
		    	DataSQLHelper eventsData = new DataSQLHelper(this);
    			SQLiteDatabase db = eventsData.getWritableDatabase();
    			Cursor cursor = getCursor(3);
    			
    			while(cursor.moveToNext())
    			{
    				int id = cursor.getInt(0);
    				String [] contents = new String[6];
	            	contents[0] = cursor.getString(1); //begin time
	            	contents[1] = cursor.getString(2); //end time
	            	contents[2] = cursor.getString(3); //message
	            	contents[3] = cursor.getString(4); //destination
	            	contents[4] = cursor.getString(5); //nid
            		addMessage(contents[0],contents[1],contents[2],contents[3],contents[4],1);	
    			}
    			db.delete(DataSQLHelper.TABLE3,null, null);
		    }
	    
	  //adds an entry into the update table.
		  //0 is error message, 1 is update statistic. 
		    private void addUpdate(String update, int type) {
		  	    SQLiteDatabase db = eventsData.getWritableDatabase();
		  	    ContentValues values = new ContentValues();
		  	    Calendar cal = Calendar.getInstance();
		  	    values.put(DataSQLHelper.STATUS, update);
		  	    values.put(DataSQLHelper.TYPE, type);
		  	    values.put(DataSQLHelper.TIME, cal.getTime().toString());
		  	    db.insert(DataSQLHelper.TABLE2, null, values);
		  	  }
		    
		  //return status of message time range
		    private String messageWithinTime(String[] dates)
		    {
		    	if(dates[0]==null||dates[1]==null) return null;
		    	//2011-01-01 18:11
		    	long beginTime = convertDate(dates[0]);
		    	long endTime = convertDate(dates[1]);
		    	Calendar cal = Calendar.getInstance();
		    	
		    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    	long currentTime = convertDate(sdf.format(cal.getTime()));
		    	if(currentTime>endTime)
		    	{
		    		return "Expired";
		    	}
		    	else if(currentTime<beginTime)
		    	{
		    		return "Waiting";
		    	}
		    	else return "Ready";
		    	
		    }
	    
		    //converts a string date in the format YYYY-MM-DD HH:MM to a concatenated long integer
		    private long convertDate(String str)
		  	{
		  		String[] contents = str.split(" ");

		  		String temp_year,temp_month,temp_day,temp_hour,
		  		temp_minute;
		  		
		  		String[] temp_date= contents[0].split("-");
		  		String[] temp_time= contents[1].split(":");
		  		
		  		temp_year=temp_date[0];
		  		temp_month=temp_date[1];
		  		temp_day=temp_date[2];
		  		temp_hour=temp_time[0];
		  		temp_minute=temp_time[1];
		  		
		  		String concatenatedDate = ""+temp_year+temp_month+temp_day
		  		+temp_hour+temp_minute;
		  		long result = Long.parseLong(concatenatedDate.trim());
		  		return Long.parseLong(concatenatedDate.trim());
		  	}    
	    
	    
	  //send an SMS 
	    private void sendSMS(String phoneNumber, String text)
	    {        
	    	SmsManager sm = SmsManager.getDefault();
	    	
	    		sm.sendTextMessage(phoneNumber, null, text, null, null);
	    		
	    	
	    } 
	    
	  //sees is the program will run on any given day
	    private boolean canRunToday()
	    {
	    	Calendar cal = Calendar.getInstance();
	    	String day = cal.getTime().toString().split(" ")[0];
	    		 if(day.equals("Mon")&&settings.getString("mon", "").equals("N"))return false;
	    	else if(day.equals("Tue")&&settings.getString("tue", "").equals("N"))return false;
	    	else if(day.equals("Wed")&&settings.getString("wed", "").equals("N"))return false;
	    	else if(day.equals("Thu")&&settings.getString("thu", "").equals("N"))return false;
	    	else if(day.equals("Fri")&&settings.getString("fri", "").equals("N"))return false;
	    	else if(day.equals("Sat")&&settings.getString("sat", "").equals("N"))return false;
	    	else if(day.equals("Sun")&&settings.getString("sun", "").equals("N"))return false;
	    	return true;
	    }
	    
	    private void updateTimeCounters()
	    {
	    	Calendar cal = Calendar.getInstance();
	    	SharedPreferences.Editor editor = settings.edit();
	    	
	    	String hour = cal.getTime().toString().split(" ")[3].split(":")[0];
	    	if(hour.equals("01")&&settings.getInt("dayInterrupt",0)==0)
	    	{
	    		editor.putInt("dayInterrupt",1);
	    		editor.putInt("messageCountToday",0);
	    		editor.putInt("messageCountHour",0);
	    		editor.commit();
	    		return;
	    	}
	    	else if(!(hour.equals("01")))
	    	{
	    		editor.putInt("dayInterrupt",0);
	    		editor.commit();
	    	}
	    	
	    	String minute = cal.getTime().toString().split(" ")[3].split(":")[1];
	    	if(minute.equals("00")&&settings.getInt("hourInterrupt",0)==0)
	    	{
	    		editor.putInt("hourInterrupt",1);
	    		editor.putInt("messageCountHour",0);
	    		editor.commit();
	    		return;
	    	}
	    	else if(!(minute.equals("00")))
	    	{
	    		editor.putInt("hourInterrupt",0);
	    		editor.commit();
	    	}
	    }
	    
	    private void postData(String nidList, String status) throws Exception
	    {
	    	try{
	    		 URL url = new URL("http://akshenweb.org/messages/status");
		    	  HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    	  conn.setDoInput (true);
		    	  conn.setDoOutput (true);
		    	  conn.setUseCaches (false);
		    	  conn.setRequestMethod("POST");
		    	  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    	  
		    	  // Make server believe we are form data...
		    	  String data = URLEncoder.encode("form_id", "UTF-8") + "=" + URLEncoder.encode("akshenmessages_updateform", "UTF-8");
		          data += "&" + URLEncoder.encode("form_build_id", "UTF-8") + "=" + URLEncoder.encode("form-b4750477a7055cd6f98ffc790755c5f5", "UTF-8");
		    	  data += "&" + URLEncoder.encode("nid_list", "UTF-8") + "=" + URLEncoder.encode(nidList, "UTF-8");
		          data += "&" + URLEncoder.encode("new_status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
		    	
		   
		    	  PrintWriter pout = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"8859_1"),true);
		    	  pout.print(data);
		    	  pout.flush();
		    	  if(!(conn.getResponseCode()==HttpURLConnection.HTTP_OK))  addUpdate("Could not establish http connection to server",0);
	    	}
	    	catch(Exception e)
	    	{
	    		addUpdate("Failed to acknowledge server: " + e,0);
	    	}  
	    }
	    
	    private void incrementCounters()
	    {
	    	SharedPreferences.Editor editor = settings.edit();
			int temp = settings.getInt("messageCountToday", 0);
			int temp2 = settings.getInt("messageCountTotal", 0);
			int temp3 = settings.getInt("messageCountHour", 0);
			editor.putInt("messageCountToday", ++temp);
			editor.putInt("messageCountTotal", ++temp2);
			editor.putInt("messageCountHour", ++temp3);
			editor.commit();
			
			if(temp2%40==0&&temp2!=0) //100 messages have been sent. wait for some time...
			{
				editor.putInt("messageInterrupt", 1);
				editor.commit();
				waitTimer = new Timer();
				TimerTask wait = new TimerTask()
		    	{
					@Override
					public void run() 
					{
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("messageInterrupt", 0);
						editor.commit();
					}	
		    	};
		    	
		    	waitTimer.schedule(wait, 30*1000);
			}
	    }
	    
	    //adds an entry into the message table
	    //CHANGE TO ADD AT PROPER INDEX. 
	    private boolean addMessage(String startTime, String endTime, String message, String destination, String nid,int tableNum) 
	    {
	    	if(startTime==null||endTime==null||message==null||destination==null||nid==null) return false;
	    	DataSQLHelper eventsData = new DataSQLHelper(this);
	  	    SQLiteDatabase db = eventsData.getWritableDatabase();
	  	    ContentValues values = new ContentValues();
	  	    if(tableNum==1)
	  	    {
	  	    	values.put(DataSQLHelper.START_TIME, startTime);
		  	    values.put(DataSQLHelper.END_TIME, endTime);
		  	    values.put(DataSQLHelper.MESSAGE, message);
		  	    values.put(DataSQLHelper.DESTINATION, destination);
		  	    values.put(DataSQLHelper.NID, nid);
		  	    db.insert(DataSQLHelper.TABLE1, null, values); 
	  	    }
	  	    
	  	    //values.put(DataSQLHelper.READY, ready);
	  	    		
	  	    else
	  	    {
	  	    	values.put(DataSQLHelper.START_TIME2, startTime);
		  	    values.put(DataSQLHelper.END_TIME2, endTime);
		  	    values.put(DataSQLHelper.MESSAGE2, message);
		  	    values.put(DataSQLHelper.DESTINATION2, destination);
		  	    values.put(DataSQLHelper.NID2, nid);
	  	    	db.insert(DataSQLHelper.TABLE3, null, values); 
	  	    }
	  	    db.close();
	  	    return true;
	  	  }
}
