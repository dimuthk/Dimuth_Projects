package com.example.alerterconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FileContents extends Activity{
	
	TextView triggerFileOutput, output;
	final String SCHEDULE_LOCATION = "/ema/alerter/schedules";
	RadioButton currSchedule, filesInDir;
	Button upload, clear;
	Context context;
	final int LIMIT = 15, COLOR_1 = Color.parseColor("#1b1d26"),COLOR_2= Color.parseColor("#425955"), 
			COLOR_3=Color.parseColor("#778c7a"), COLOR_4=Color.parseColor("#f1f2d8"), COLOR_5 = Color.parseColor("#bfbd9f"); 
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.input_contents);
	        context = this;
	        triggerFileOutput = (TextView) findViewById(R.id.times);
	        output = (TextView) findViewById(R.id.stats);
	        output.setTextColor(COLOR_5);
	        triggerFileOutput.setTextColor(COLOR_5);
	        
	        currSchedule = (RadioButton) findViewById(R.id.curr_schedule);
	        filesInDir = (RadioButton) findViewById(R.id.files_in_sys) ;
	        upload = (Button) findViewById(R.id.upload);
	        upload.getBackground().setColorFilter(COLOR_2, PorterDuff.Mode.MULTIPLY);
	        clear = (Button) findViewById(R.id.clear);
	       
	        currSchedule.setOnClickListener(new OnClickListener(){
	    		
	    		public void onClick(View arg0) {
	    			
	    			showUpdates();
	    		}
	        });
	        
	        filesInDir.setOnClickListener(new OnClickListener(){
	    		
	    		public void onClick(View arg0) {
	    			
	    			showFiles();
	    		}
	        });
	        
	        upload.setOnClickListener(new OnClickListener(){
	    		
	    		public void onClick(View arg0) {
	    			File file = checkForNewFile();
					if(file!=null){
						updateTable(file);
						displayInfo();
						SharedPreferences settings = getApplicationContext().getSharedPreferences("settings",0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("alarmSet",false);
						editor.commit();
						Toast.makeText(context, "New schedule: " + "alerter_schedule_" + settings.getLong("latestDate", 0), 5).show();
					}
					else{
						Toast.makeText(context, "No newer schedules available", 5).show();
					}
	    		}
	        });
	        
	        clear.setOnClickListener(new OnClickListener(){
	    		
	    		public void onClick(View arg0) {
	    			AlertDialog.Builder builder;
	    	    	AlertDialog alert;
	    	    	builder = new AlertDialog.Builder(context);
	    			builder.setCancelable(true);
	    			builder.setTitle("Confirm");
	    			builder.setMessage("Are you sure you want to delete all files in the directory?");
	    			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
    					
    					public void onClick(DialogInterface arg0, int arg1) {
    						clearFiles();
    					}});
	    			builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
	    					
	    					public void onClick(DialogInterface arg0, int arg1) {
	    						arg0.cancel();
	    					}});
	    			
	    			
	    			 alert = builder.create();
	    		        alert.show();
	    		}
	        });
	 }
	 
	 @Override
	 public void onStart(){
		 super.onStart();
		 currSchedule.setChecked(true);
		 showUpdates();
		 displayInfo();
		 
		 
	 }

		void clearFiles(){
			SharedPreferences settings = context.getSharedPreferences("settings",0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong("latestDate", 0);
			editor.commit();
			
			File[] dir = AlerterConfigActivity.schedule_file.listFiles();
			for(File file: dir){
				file.delete();
			}
		}
	 
		private void showUpdates() {
			SharedPreferences settings = getApplicationContext().getSharedPreferences("settings",0);
			DataSQLHelper eventsData = new DataSQLHelper(this);
			SQLiteDatabase db = eventsData.getReadableDatabase();
			Cursor cursor = db.query(DataSQLHelper.TABLE, null, null, null, null,
					null, null);
			startManagingCursor(cursor);
			String ret = "First 60 entries:\n\n";
			String splitStartTime, splitEndTime, temp;
			long startTime, endTime;
			String quizPath;
			int count = 0;
			boolean first = true;
		   while(cursor.moveToNext() && count < 60){
			   count++;
			   startTime = cursor.getLong(1); //yyyyMMddHHmm
			   temp = ""+startTime;
			   splitStartTime = Integer.parseInt(temp.substring(8,10))+":"+temp.substring(10)
					   + " " + Integer.parseInt(temp.substring(4,6))+"/"+Integer.parseInt(temp.substring(6,8))
					   + "/" + temp.substring(2,4);
			   endTime = cursor.getLong(2);
			   temp = "" + endTime;
			   splitEndTime = Integer.parseInt(temp.substring(8,10))+":"+temp.substring(10)
					   + " " + Integer.parseInt(temp.substring(4,6))+"/"+Integer.parseInt(temp.substring(6,8))
					   + "/" + Integer.parseInt(temp.substring(2,4)); 
			   quizPath = cursor.getString(3);
			   if(first){
				   boolean b = settings.getBoolean("alarmSet", false);
				   if(b == true){
					   ret += splitStartTime + " to " + splitEndTime + " " + "\n" + quizPath + " Alarm Set\n\n"; 
				   }
				   else{
					   ret += splitStartTime + " to " + splitEndTime + " " + "\n" + quizPath + " \n\n"; 
				   }
				//   
				   first = false;
			   }
			   else{
				   ret += splitStartTime + " to " + splitEndTime + "\n" + quizPath + "\n\n"; 
			   }
			   
			   
		   }
		   db.close();
		   eventsData.close();
		   output.setText(ret);
		}
	 
	 void showFiles(){
		 try{
			 File sdcard = Environment.getExternalStorageDirectory();
			 File schedule_file = new File(sdcard,SCHEDULE_LOCATION);
				File[] dir = schedule_file.listFiles();
				String ret = "Files in directory:\n\n";
				
				for(File file: dir){
					ret += file.getName() + "\n";
				}
				output.setText(ret);
		 }
			catch(Exception e){
				Toast.makeText(context, e.toString(), 10).show();
			}
	 }
	 
	 /*String printFirstStatus(){
		 try {
				BufferedReader br = new BufferedReader(new FileReader(AlerterConfigActivity.trigger_file));
				br.readLine(); br.readLine();
				String info = br.readLine().trim();	
				br.close();
				String status = info.split(" ")[1];
				return status;
				
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
	 }*/
	 
	//update the database, if there is a need to.
		File checkForNewFile(){
			SharedPreferences settings = context.getSharedPreferences("settings",0);
 	    	
			File[] dir = AlerterConfigActivity.schedule_file.listFiles();
			long latestDate = settings.getLong("latestDate", 0), date;
			File latestFile = null;
			for (File fileEntry: dir){
				String name = fileEntry.getName();
					
				//we will have the name of each file be like this: alerter_schedule_yyyyMMddHHmm
				date = Long.parseLong(name.split("_")[2]);
				if(date > latestDate){
					latestDate = date;
					latestFile = fileEntry;
				}
			}
			if(latestDate != settings.getLong("latestDate", 0)){
				SharedPreferences.Editor editor = settings.edit();
				editor.putLong("latestDate", latestDate);
				editor.commit();
				return latestFile;
			}
			else return null;
		}
		
		void displayInfo(){
			SharedPreferences settings = this.getSharedPreferences("settings",0);
			 long date = settings.getLong("latestDate", 0);
			 if(date == 0){
				 triggerFileOutput.setText("none");
			 }
			 else{
				 triggerFileOutput.setText("alerter_schedule_" + settings.getLong("latestDate", 0));
			 }
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
}
