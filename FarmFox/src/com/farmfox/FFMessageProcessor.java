package com.farmfox;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;

public class FFMessageProcessor extends Service{

	static String TRIGGER = "FF";
	static String SUBSCRIBE = "subscribe";
	static String UNSUBSCRIBE = "unsubscribe";
	static String LIST = "list";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		String from = intent.getStringExtra("from").trim();
		String message = intent.getStringExtra("message").trim();
		String[] contents = message.split(" ");
		if(contents.length > 0 && contents[0].trim().equals(TRIGGER)){
			if(contents.length > 1 && contents[1].trim().equals(SUBSCRIBE)){
				for(int i=1; i<contents.length;i++){
					if(isValidSubscription(contents[i])){
						addUserToSubscription(from, contents[i]);
					}
				}
			}
			else if(contents.length > 1 && contents[2].trim().equals(UNSUBSCRIBE)){
				
			}
			
		}
		//tell(intent.getStringExtra("from"),intent.getStringExtra("message"));
		return 0;
	}
	
	void addUserToSubscription(String from, String subscription){
		DataSQLHelper eventsData = new DataSQLHelper(this);
	    SQLiteDatabase reader = eventsData.getReadableDatabase();
	    
	    Cursor cursor = reader.query(DataSQLHelper.TABLE1, null, null, null, null,
    	        null, null);
	    boolean alreadySubscribed = false;
	    while(cursor.moveToNext()){
	    	if(cursor.getString(1).equals(subscription)){
	    		String[] users = cursor.getString(2).split(";");
	    		for(String word: users){
	    			if(word.equals(from)){
	    				alreadySubscribed = true;
	    				break;
	    			}
	    		}
	    		if(alreadySubscribed == false){
	    			SQLiteDatabase writer = eventsData.getWritableDatabase();
    				ContentValues values = new ContentValues();
			  	    values.put(DataSQLHelper.USERS, cursor.getString(2) + ";" + from);
			  	    writer.update(DataSQLHelper.TABLE1, values, "_ID="+cursor.getInt(0), null);
			  	    writer.close();
	    		}
	    		break;
	    	}	
	    }
	    reader.close();
    	eventsData.close();
	}
	
	boolean isValidSubscription(String word){
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

	void tell(String from, String message){
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = from;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		CharSequence contentTitle = "My notification";
		CharSequence contentText = message;
		Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://quipst.com:8000/survey/start/?uid=111"));
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(1, notification);
    }
}
