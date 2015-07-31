package com.hear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class C2DMRegistrationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//C2dmtestActivity.text.setText("Received intent!");
		String action = intent.getAction();
		
		SharedPreferences settings = context.getSharedPreferences("settings", 0);
		SharedPreferences.Editor editor = settings.edit();
		
		
		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
			Log.w("C2DM", "Receiver action was: " + action);
			Log.w("C2DM", "Registration Receiver called");
			Log.w("C2DM", "Received registration ID");
			final String registrationId = intent
					.getStringExtra("registration_id");
			String error = intent.getStringExtra("error");
			if(error != null){
				Log.w("C2DM", error);
				return;
			}
			//
			if(registrationId == null){
				Log.w("C2DM", "This should cause a notification to appear.");
				NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
				int icon = R.drawable.icon;
				CharSequence tickerText = "You have a new survey to take.";
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText, when);
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				CharSequence contentTitle = "My notification";
				CharSequence contentText = "Go to QuiPST!";
				Intent notificationIntent = new Intent();
				notificationIntent.setComponent(new ComponentName("com.phonegap.helloworld", "com.phonegap.helloworld.App"));
				
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(1, notification);
			}else{
				editor.putString("registration_id",intent
						.getStringExtra("registration_id"));
				editor.commit();
				Log.w("C2DM", "Id is " + registrationId);
				//String error = intent.getStringExtra("error");
				
				Log.d("C2DM", "dmControl: registrationId = " + registrationId
						+ ", error = " + error);
			}
			
			// TODO Send this to my application server
			
			
		}
		else if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
			Log.w("C2DM", "Received message");
			final String payload = intent.getStringExtra("payload");
			Log.d("C2DM", "dmControl: payload = " + payload);
			editor.putBoolean("message_receieved",true);
			editor.commit();
		}
	}
	
	
}