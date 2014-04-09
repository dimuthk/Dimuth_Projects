package com.quipst;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        //TextView output = (TextView) findViewById(R.id.output);
        final EditText uid = (EditText) findViewById(R.id.uid);
        final EditText pass = (EditText) findViewById(R.id.password);
        Button confirm = (Button) findViewById(R.id.confirm);
        
        confirm.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				userAuth(uid.getText().toString(), pass.getText().toString());
			}
        	
        });
    }
    
    public void register() {
    	Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
    	intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	intent.putExtra("sender", "majorraynor001@gmail.com");
    	startService(intent);
    }
    
    String userAuth(String uid, String password) {
    	try {
    		final SharedPreferences settings = getSharedPreferences("settings", 0);
    	    //String data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
    	    //data += "&" + URLEncoder.encode("pw", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

    	    // Send data
    	    URL url = new URL("http://quipst.com:8000/survey/user_auth/");
    	    		
    	    URLConnection conn = url.openConnection();
  
    	    conn.setDoOutput(true);
    	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    	    wr.flush();

    	    // Get the response
    	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	    String line, result = "";
    	    while ((line = rd.readLine()) != null) {
    	       result += line;
    	    }
    	    wr.close();
    	    rd.close();
    	    Toast.makeText(this, result, 5).show();
    	    return result;
    	   
    	} catch (IOException e) {
    		Toast.makeText(this, e.toString(), 5).show();
    		e.printStackTrace();
    		return null;
    	}
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
    	    Toast.makeText(this, result, 5).show();
    	   
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}
