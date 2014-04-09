package com.example.alerterconfig;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlerterConfigActivity extends Activity {
    
	private class ServiceTrigger extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Intent i = new Intent(context, SetAlarms.class);
			i.putExtra("triggeredFromAlerter", true);
			startService(i);	
		}
		
	}
	
	
	
	CheckBox vibrate, flash, sound, run;
	Button sound_source, playback, viewFile;
	ServiceTrigger setAlarmsTrigger;
	TextView soundOutput;

	static File schedule_file;
	final String SCHEDULE_LOCATION = "/ema/alerter/schedules";

	Context context;
	final int LIMIT = 15, COLOR_1 = Color.parseColor("#1b1d26"),COLOR_2= Color.parseColor("#425955"), 
			COLOR_3=Color.parseColor("#778c7a"), COLOR_4=Color.parseColor("#f1f2d8"), COLOR_5 = Color.parseColor("#bfbd9f"); 
	
	@Override
	public void onStart(){
		super.onStart();
		SharedPreferences settings = this.getSharedPreferences("settings",0);
		SharedPreferences.Editor editor = settings.edit();
		
		
	        if(settings.getBoolean("file4", true) == true){
	        	clearFiles();
			    createTextFile();
			    editor.putBoolean("file4", false);
			    editor.commit();
	        }
	        
		     displayInfo();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        setAlarmsTrigger = new ServiceTrigger();
        IntentFilter filter = new IntentFilter("received");
        
        registerReceiver(setAlarmsTrigger, filter);
        vibrate = (CheckBox) findViewById(R.id.vibrate);
        flash = (CheckBox) findViewById(R.id.flash);
        sound = (CheckBox) findViewById(R.id.sound);
        run = (CheckBox) findViewById(R.id.run);
        sound_source = (Button) findViewById(R.id.sound_source);
        playback = (Button) findViewById(R.id.playback);
        viewFile = (Button) findViewById(R.id.file_contents);
        soundOutput = (TextView) findViewById(R.id.output);
        context = this;
       
        //sound_source_file = "";
        File sdcard = Environment.getExternalStorageDirectory();
        
        playback.getBackground().setColorFilter(COLOR_2, PorterDuff.Mode.MULTIPLY);
        sound_source.getBackground().setColorFilter(COLOR_5, PorterDuff.Mode.MULTIPLY);
        soundOutput.setTextColor(COLOR_5);
        
        SharedPreferences settings = this.getSharedPreferences("settings",0);
		SharedPreferences.Editor editor = settings.edit();
        
		if(!settings.contains("vibrate")){
			editor.putBoolean("vibrate", true);
	        editor.putBoolean("flash", true);
	        editor.putBoolean("sound", true);
	        editor.putString("soundSource", "/mnt/sdcard/ema/alerter/On.mp3");
	        editor.commit();
		}
        
	        
	        File ekg = new File(sdcard,"/ema");
	        if(ekg.isDirectory() == false) ekg.mkdir();
	        
	        File alerter = new File(ekg,"/alerter");
	        if(alerter.isDirectory() == false) alerter.mkdir();
	        
	        schedule_file = new File(sdcard,SCHEDULE_LOCATION);
	        if(schedule_file.isDirectory() == false) schedule_file.mkdir();

	        
      

  
        
        vibrate.setOnClickListener(new OnClickListener(){
        	SharedPreferences settings = context.getSharedPreferences("settings",0);
    		SharedPreferences.Editor editor = settings.edit();
    		public void onClick(View arg0) {
    			if(vibrate.isChecked()) {
    				editor.putBoolean("vibrate", true);
    			}
    			else {
    				editor.putBoolean("vibrate", false);
    			}
    			editor.commit();
    		}
        });
        
        flash.setOnClickListener(new OnClickListener(){
        	SharedPreferences settings = context.getSharedPreferences("settings",0);
    		SharedPreferences.Editor editor = settings.edit();
    		public void onClick(View arg0) {
    			if(flash.isChecked()) {
    				editor.putBoolean("flash", true);
    			}
    			else {
    				editor.putBoolean("flash", false);
    			}
    			editor.commit();
    		}
        });

		sound.setOnClickListener(new OnClickListener(){
			SharedPreferences settings = context.getSharedPreferences("settings",0);
    		SharedPreferences.Editor editor = settings.edit();
    		public void onClick(View arg0) {
    			if(sound.isChecked()) {
    				editor.putBoolean("sound", true);
    			}
    			else {
    				editor.putBoolean("sound", false);
    			}
    			editor.commit();
    		}
        });
		
		viewFile.setOnClickListener(new OnClickListener(){
			
			public void onClick(View arg0) {
				Intent i = new Intent(context, FileContents.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		
		run.setOnClickListener(new OnClickListener(){
			
			public void onClick(View arg0) {
				SharedPreferences settings = context.getSharedPreferences("settings",0);
		    	SharedPreferences.Editor editor = settings.edit();
		    	if(settings.getBoolean("running",false) == false){
		    		editor.putBoolean("running", true);
		    		editor.putBoolean("alarmSet", false);
		    		Intent i = new Intent(context, SetAlarms.class);
					startService(i);
		    	}
		    	else{
		    		editor.putBoolean("running", false);
		    		Intent i = new Intent(context, SetAlarms.class);
					stopService(i);
		    	}
		    	editor.commit();
				
			}
		});

		playback.setOnClickListener(new OnClickListener(){
	
	public void onClick(View arg0) {
		MediaPlayer player = new MediaPlayer();
		try {
			SharedPreferences settings = context.getSharedPreferences("settings",0);
			player.setDataSource(settings.getString("soundSource",""));
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			Toast.makeText(context, "Could not play file", 5).show();
		} catch (IllegalStateException e) {
			Toast.makeText(context, "Could not play file", 5).show();
		} catch (IOException e) {
			Toast.makeText(context, "Could not play file", 5).show();
		}
	}
});

sound_source.setOnClickListener(new OnClickListener(){
	
	public void onClick(View arg0) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
	 		final EditText input = new EditText(context);
	 		builder.setView(input)
	 		.setMessage("Enter a sound destination. Destination should be in same directory as configuration file. Include the extension!")
	 		.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
			
			public void onClick(DialogInterface arg0, int arg1) {
				SharedPreferences settings = context.getSharedPreferences("settings",0);
		    	SharedPreferences.Editor editor = settings.edit();
				editor.putString("soundSource", input.getText().toString());
				displayInfo();
			
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
    }
    
    void displayInfo(){
    	SharedPreferences settings = context.getSharedPreferences("settings",0);

    	run.setChecked(settings.getBoolean("running",false));
    	vibrate.setChecked(settings.getBoolean("vibrate", false));
    	flash.setChecked(settings.getBoolean("flash", false));
    	sound.setChecked(settings.getBoolean("sound", false));
    	
    	soundOutput.setText(settings.getString("soundSource", ""));
    }
    
    
    void updateTable(File file){
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			 String temp;
	            DataSQLHelper eventsData = new DataSQLHelper(this);
		  	    SQLiteDatabase db = eventsData.getWritableDatabase();
		  	    db.delete(DataSQLHelper.TABLE, null, null); //clear EVERYTHING from before
		  	    String[] contents;
		  	    String quiz;
		  	    ContentValues values;
		  	    long startTime, endTime;
	          
					while((temp = br.readLine()) != null){
						//the format of the file is: startTime endTime quiz
						temp = temp.trim();
						contents = temp.split(" ");
						startTime = Long.parseLong(contents[0]);
						endTime = Long.parseLong(contents[1]);
						quiz = contents[2];
						values = new ContentValues();
						values.put(DataSQLHelper.START_TIME, startTime);
					    values.put(DataSQLHelper.END_TIME, endTime);
					    values.put(DataSQLHelper.QUIZ_TO_RUN, quiz);
					    db.insert(DataSQLHelper.TABLE, null, values);
					}
				db.close();
				eventsData.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
    	
	void clearFiles(){
		SharedPreferences settings = context.getSharedPreferences("settings",0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("latestDate", 0);
		editor.commit();
		File[] dir = schedule_file.listFiles();
		for(File file: dir){
			file.delete();
		}
	}
	

    //create one debugging text file right now. it will spawn alerts at 5 min intervals. 
    File createTextFile(){
    	
    	try {
    		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
  	  	  	Calendar cal = Calendar.getInstance();
  	  	  	long currTime = Long.parseLong(sdf.format(cal.getTime()));
    		File sdcard = Environment.getExternalStorageDirectory();
    		String fileName = "alerter_schedule_"+currTime;
        	String line, quizPath = "quiz";
        	File newFile = new File(sdcard,SCHEDULE_LOCATION+"/"+fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
			
	    	long startTime = currTime - 5, endTime = currTime;
	    	for(int i=0; i<300; i++){
	    		startTime += 5; endTime += 5;
	    		if(startTime % 100 >= 60){
	    			startTime -= 60;
	    			startTime += 100;
	    		}
	    		if(endTime % 100 >= 60){
	    			endTime -= 60;
	    			endTime += 100;
	    		}
	    		line = startTime + " " + endTime + " " + quizPath + "\n";
	    		bw.write(line);
	    	}
	    	bw.close();
	    	return newFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    	
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
		"Alerter designed at BCF facilities, University of Arizona by Dimuth Kulasinghe, working on behalf of Stealth.");
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
		builder.setMessage("Toggle Alerter using the checkbox; AlerterConfiguration.apk will periodically check to see if new " +
				"quizzes are available. Use checkboxes at bottom to designate alert style.");
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