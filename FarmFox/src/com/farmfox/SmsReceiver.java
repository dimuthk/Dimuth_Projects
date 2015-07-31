package com.farmfox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver
{
	Context appContext; 
	 @Override
	    public void onReceive(Context context, Intent intent) 
	    {
	        //---get the SMS message passed in---
	        Bundle bundle = intent.getExtras();        
	        SmsMessage[] msgs = null;    
	        appContext = context;
	        if (bundle != null)
	        {
	            //---retrieve the SMS message received---
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            msgs = new SmsMessage[pdus.length];           
	            String message="",from="";
	            for (int i=0; i<msgs.length; i++){
	                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);               
	                from = msgs[i].getOriginatingAddress();
	                message = msgs[i].getMessageBody().toString();      
	            }
	            Intent i = new Intent(context, FFMessageProcessor.class);
	            i.putExtra("message",message);
	            i.putExtra("from", from);
	            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            context.startService(i);
	            /*try
	            {
	            	Calendar cal = Calendar.getInstance();
		            String DATE_FORMAT = "yyyyMMddHHmm";
		            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		            String startTime = sdf.format(cal.getTime());
		            DataSQLHelper eventsData = new DataSQLHelper(context);
		            SQLiteDatabase db = eventsData.getWritableDatabase();
		            ContentValues values = new ContentValues();
			  	    values.put(DataSQLHelper.USER, startTime);
			  	    values.put(DataSQLHelper.SUBSCRIPTIONS, message);
			  	    db.insert(DataSQLHelper.TABLE1, null, values); 
			  	    db.close(); eventsData.close();
	            }
	            catch(Exception e)
	            {
	            	e.printStackTrace();
	            	//addUpdate("Failed to properly run SmsReceiver: " +e,0);
	            }*/

	        }                         
	    }

}