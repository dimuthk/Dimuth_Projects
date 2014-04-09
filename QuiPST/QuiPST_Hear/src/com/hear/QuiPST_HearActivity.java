package com.hear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Okay. First thing is to register the device and store the id. Second thing is to tell this to django.
 * Finally, create a second receiver for the actual push notifications. That will come after server implementation however. 
 */

public class QuiPST_HearActivity extends Activity {
   Context context = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button a = (Button) findViewById(R.id.reg_id);
        Button b = (Button) findViewById(R.id.send_data);
        Button c = (Button) findViewById(R.id.check_message);
        Button d = (Button) findViewById(R.id.uid_conf);
        final EditText text = (EditText) findViewById(R.id.uid);
        final SharedPreferences settings = getSharedPreferences("settings", 0);
        register();
        
        a.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), settings.getString("registration_id", "No id currently registered"), 5).show();
			}
        	
        });
        b.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				String regId = settings.getString("registration_id", "No id currently registered");
				String devId = settings.getString("uid", "No id currently registered");
				if(regId.equals("No id currently registered")){
					Toast.makeText(getApplicationContext(), settings.getString("registration_id", "No id currently registered"), 5).show();
				}
				else{
					sendRegistrationIdToServer(devId,regId);
				}	
			}
        	
        });
        c.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				if(settings.getBoolean("message_received", false) == true){
					Toast.makeText(getApplicationContext(), "YES!", 5).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "No...", 5).show();
				}
				
			}
        	
        });
        d.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("uid", text.getText().toString());
				editor.commit();
				Toast.makeText(getApplicationContext(), "Uid is now " + text.getText().toString(), 5).show();
			}
        	
        });
    }
    
    public void register() {
    	Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
    	intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	intent.putExtra("sender", "majorraynor001@gmail.com");
    	startService(intent);
    }
    
    
    void sendRegistrationIdToServer(String deviceId, String registrationId) {
    	try {
    		final SharedPreferences settings = getSharedPreferences("settings", 0);
    	    String data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(settings.getString("uid",""), "UTF-8");
    	    data += "&" + URLEncoder.encode("dev_id", "UTF-8") + "=" + URLEncoder.encode(registrationId, "UTF-8");

    	    // Send data
    	    URL url = new URL("http://quipst.com:8000/survey/dev_reg/?uid="+deviceId+"&dev_id=" + registrationId);
    	    		
    	    URLConnection conn = url.openConnection();
    	   
    	    conn.setDoOutput(true);
    	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    	    wr.write(data);
    	    wr.flush();

    	    // Get the response
    	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	    String line, result = "";
    	    while ((line = rd.readLine()) != null) {
    	       result += line;
    	    }
    	    wr.close();
    	    rd.close();
    	    Toast.makeText(context, result, 5).show();
    	   
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}