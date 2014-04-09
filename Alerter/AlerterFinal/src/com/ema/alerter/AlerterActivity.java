package com.ema.alerter;

import java.io.BufferedReader;





import java.io.File;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.PorterDuff;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;

import android.view.Menu;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

//the alerter causes a physical "alert" when activated. countdown timer starts. start/snooze button.
//times configurable. activate quiz when started. 
//turn the alerter into a service....

/*
 * on activation of activity, have it pass a number to the service (countdown timer). 
 */

/*
 * This activity is now nothing more than the alert itself. Main screen of the app is the 
 * configuration screen. 
 */

public class AlerterActivity extends Activity {
	
	
	
	Button startQuiz;

    /** Called when the activity is first created. */
	TextView output;
	boolean vibrate, flash, sound;
	String sound_source_file;
	String jsonFile;
	Handler serviceHandler;
	Runnable runAlert;
	Context context;
	MediaPlayer player;
	Vibrator vibrator;
	//orig limit 175
	final int LIMIT = 175, COLOR_1 = Color.parseColor("#1b1d26"),COLOR_2= Color.parseColor("#425955"), 
			COLOR_3=Color.parseColor("#778c7a"), COLOR_4=Color.parseColor("#f1f2d8"), COLOR_5 = Color.parseColor("#bfbd9f"); 
	//final String DIRECTORY = "/ekg/alerter/alert_config.txt";
	int counter, snoozeTime;
	boolean first = true;
	String appToTrigger;
	PendingIntent intentToStart;
	//AlarmManager am;
	PowerManager.WakeLock wakelock;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startQuiz = (Button) findViewById(R.id.start); 
		File sdcard = Environment.getExternalStorageDirectory();
        //file = new File(sdcard,DIRECTORY);
        context = this;
        sound_source_file = "";
        player = new MediaPlayer();
        
        //acknowledge = (Button) findViewById(R.id.snooze);
        //trigger_file = new File(sdcard,TRIGGER_LOCATION);
        output = (TextView) findViewById(R.id.info);
        output.setTextColor(COLOR_5);
        startQuiz.getBackground().setColorFilter(COLOR_4, PorterDuff.Mode.MULTIPLY);
        //am = (AlarmManager) getSystemService(ALARM_SERVICE);
	}

    @Override
    public void onStart(){
    	super.onStart();
    	
    	Bundle b = getIntent().getExtras();

    	try{
    		if(b==null){ //go to the default screen
        		finish();
        	}
    		
        	else{
        		jsonFile = b.getString("key");
        		Intent intent = new Intent("received");
        		sendBroadcast(intent);

        		initializeMain();
        		
        		
        		first = true;
        		vibrate = b.getBoolean("vibrate");
        		sound = b.getBoolean("sound");
        		flash = b.getBoolean("flash");
        		sound_source_file = b.getString("soundSource");
            	output.setText("A survey is ready for you.");
            	//setTimer();
                runAlert();
                    

        	}
    	}
    	catch(Exception e){
    		output.setText("Please install AlerterConfig.");
    		startQuiz.setOnClickListener(new OnClickListener(){
    	       	public void onClick(View arg0) {	
    	       	}
    	       });		
    	}
    	
    	
    }
    
    
    void initializeMain(){

        serviceHandler = new Handler();
        counter = 0;
    	output.setTextColor(COLOR_5);
    	vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
   	
    	final Runnable changeColor = new Runnable(){

			public void run() {
				try{
					View backgroundView = findViewById(R.id.background);
					if(counter%2==0) backgroundView.setBackgroundColor(COLOR_3);
					else backgroundView.setBackgroundColor(Color.BLACK);	
				}
				catch(Exception e){
				}
				}	
		};
   	
		/*
		 * This is the actual alert mechanism
		 */
		runAlert = new Runnable(){
   		
			public void run() {
					
						if(first){				       
							if(sound==true){
								playSound();
						}
							
						
							       
						}
							
						if(vibrate==true) vibrator.vibrate(150);    
						
						if(flash==true) serviceHandler.post(changeColor);	            
				         
				    	first = false;
						counter++;	
						if(counter<LIMIT) serviceHandler.postDelayed(runAlert,500);
						else{
							player.setLooping(false);
							View backgroundView = findViewById(R.id.background);
							backgroundView.setBackgroundColor(Color.BLACK);	
							triggerQuiz();
						}
						
				}	
		};
	
		
		startQuiz.setOnClickListener(new OnClickListener(){
       	public void onClick(View arg0) {	
       		View backgroundView = findViewById(R.id.background);
       		player.stop();
       		vibrator.cancel();
       		counter = LIMIT;
       		backgroundView.setBackgroundColor(Color.BLACK);
       		serviceHandler.removeCallbacks(runAlert);
       		triggerQuiz(); 
       	}
       });		

    }
    


    
    void playSound(){
    		player = new MediaPlayer();
    		try {
    			player.setDataSource(sound_source_file);
    			player.prepare();
    			player.setLooping(true);
    			
    			player.start();
    		} catch (IllegalArgumentException e) {
    			e.printStackTrace();
    		} catch (IllegalStateException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    }
    
    
    void triggerQuiz(){
    	player.setLooping(false);
    	serviceHandler.removeCallbacks(runAlert);
    	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
       Intent intent = new Intent();
       intent.setComponent(new ComponentName("com.msi.ekgqa", "com.msi.ekgqa.EkgQA"));
       intent.putExtra("key", jsonFile);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       PendingIntent sender = PendingIntent.getActivity(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.MILLISECOND, 5);
       am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
       /*try{
    	   startActivity(intent);
       }
       catch(Exception e){
    	   Toast.makeText(context,"Please install Quizzer.", 5).show();
       }*/
       finish();

    }
    
   
    
	//start the alerting process. 
	void runAlert(){
		
        serviceHandler.post(runAlert);
	}
	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add("About");
    	menu.add("Help");
		return super.onCreateOptionsMenu(menu);	
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	AlertDialog.Builder builder;
    	AlertDialog alert;
    	switch(item.getItemId()){
    	case 0:builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle("Alerter Info");
		builder.setMessage("Version 1.1\n"+
		"Uploaded 10/12/2011\n"+
		"Alerter was designed at the University of Arizona by Dimuth Kulasinghe, working on behalf of Stealth.");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.cancel();
				}});
		 alert = builder.create();
	        alert.show();
	        break;
    	case 1:builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle("Help");
		builder.setMessage("Click START to skip countdown and begin survey immediately.\n\nTo wait, click SNOOZE and choose number of minutes"+
		"for Quiz Alerter to wait. Quiz Alerter will automatically reinitiate countdown at end of snooze period. New snoozes will override" +
		" previous ones.");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.cancel();
				}});
		alert = builder.create();
	        alert.show();
	    break;
	    default: break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
     
}
