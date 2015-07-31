package com.example.bluefox;


import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import org.apache.http.HttpConnection;
import org.w3c.dom.*;


import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;



import drupalservices.DrupXmlServices;
import drupalservices.ServicesSettings;
import drupalservices.apis.BlogNodeSaveApi;
import drupalservices.apis.DimuthNodeSaveApi;
import drupalservices.apis.NodeLocation;
import drupalservices.apis.SystemConnectApi;
import drupalservices.apis.UserLoginApi;
import drupalservices.apis.ViewGetApi;



import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//MAIN CLASS OF PROJECT
//The parsers and all such cannot be implemented yet, but we can create the database for the information now.
//you don't need to build a table for the messages anymore. still have a table for the update log. 

//you still have a message table on the phone. what range of messages do you scan?
//scan no messages that are past their send time. Scan x messages, starting from the most recent that
//can still be sent. Place the message in a table. This is one service and runs say every minute. 
//The messages are sent through another service. This service should run at a relatively
//fast rate (every 15 seconds?). The service runs as such; take a message from the table (it is now removed
//from the table), check to see if it can be sent, and send it immediately to the target. 
//This is virtually all the program does. Still have an update log. 
public class bluefox extends Activity {
	public static final String PREFS_NAME = "akshen";
	private Button showMessages,showUpdates,goToSettings, refresh;
	private Context appContext;
	private SharedPreferences settings;
	private TextView output;
	private String DATE_FORMAT = "yyyy-MM-dd HH:MM";
	private Intent distributeMessages, updateMessages;
	Timer timer;
	String loginId;
	ServicesSettings settings2;
	DataSQLHelper eventsData;

	
	//you need some saved variables. first is
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        settings = this.getSharedPreferences("settings",MODE_WORLD_READABLE);
        //dates in this format: 2011-01-01 18:11
        distributeMessages = new Intent(this,DistributeMessages.class);
        updateMessages = new Intent(this,UpdateMessages.class);
        output = (TextView)findViewById(R.id.output2);
        showMessages = (Button)findViewById(R.id.show_messages);
        refresh = (Button)findViewById(R.id.refresh);
        showUpdates = (Button)findViewById(R.id.view_update_log);
        goToSettings = (Button)findViewById(R.id.settings);
        appContext=this;
        //addMessage("ss","","","");
        //updateMessages();
        eventsData = new DataSQLHelper(this);
        //String x= getMessagesFromAkshen();
        //updateMessages(x);
        //distributeMessage();
        //convertDate("2000-11-12 07:12");
        //configure initial settings
        if(!settings.contains("message_url"))	   
        {
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("drupal_url", "http://ccp.arl.arizona.edu/daveakshen/");
        	editor.commit();
        	editor.putString("message_url", settings.getString("drupal_url", "") + "messages/dimuth");
           	editor.putString("mon", "Y");
        	editor.putString("tue", "Y");
        	editor.putString("wed", "Y");
        	editor.putString("thu", "Y");
        	editor.putString("fri", "Y");
        	editor.putString("sat", "Y");
        	editor.putString("sun", "Y");
        	
        	editor.putString("logError", "Y");
        	editor.putString("logMessageStatus", "Y");
        	editor.putString("last20", "N");
        	
        	editor.putInt("messageCountHour", 0); //message statistics
        	editor.putInt("messageCountToday", 0);
        	editor.putInt("messageCountTotal", 0);
        	
        	editor.putInt("messageInterrupt", 0); //interrupt as a way to track when 100 msgs have been sent
        	editor.putInt("hourInterrupt", 0); 
        	editor.putInt("dayInterrupt", 0); 
        	
        	editor.putInt("messageDistrRate", 5*1000); //messages from the table will be sent every x ms.
        	editor.putInt("updateMsgRate", 60*1000); //the app will obtain new messages from the feed every y ms.
        	
        	editor.commit();
        }
        getSessionId();
        attempt();
        stopService(distributeMessages);
        stopService(updateMessages);
        
        showMessages.setOnClickListener(new OnClickListener()
        {
     		@Override
     		public void onClick(View arg0) {	

     			Intent i = new Intent(appContext, ShowMessages.class);
     			startActivity(i);
     		 
     		}
        });
        
        refresh.setOnClickListener(new OnClickListener()
        {
     		@Override
     		public void onClick(View arg0) {	

     			setStatistics();
     		 
     		}
        });
        
        showUpdates.setOnClickListener(new OnClickListener()
        {
     		@Override
     		public void onClick(View arg0) {	
     			Intent i = new Intent(appContext, ShowUpdates.class);
     			startActivity(i);
     		 
     		}
        });
        
        goToSettings.setOnClickListener(new OnClickListener()
        {
     		@Override
     		public void onClick(View arg0) {	

     			Intent i = new Intent(appContext, Settings.class);
     			startActivity(i);
     		 
     		}
        });
    }

	   
	    private void setStatistics()
	    {
	    	 output.setText("Messages sent this hour: " + settings.getInt("messageCountHour", 0) + 
		        		"\n" + "Total messages today: " + settings.getInt("messageCountToday", 0) +
		        		"\n" + "Total messages sent: " + settings.getInt("messageCountTotal", 0));
	    
	    }
	           
		  //send an SMS 
		    private void sendSMS(String phoneNumber, String text)
		    {        
		    	SmsManager sm = SmsManager.getDefault();
		    	sm.sendTextMessage(phoneNumber, null, text, null, null);
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
			    
			    

			  //searches the akshen website and will update the message table as necessary
			    private void updateMessages(String input)
			    {
			    	  try {	     
			    		  InputSource is = new InputSource();
			    		  is.setCharacterStream(new StringReader(input));
			    		  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			    		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			    		    Document doc = dBuilder.parse(is);
			    		    String nidList="";
			    		    String nid="",destination="",message="",startTime="",endTime="";
			    		     doc.getDocumentElement().normalize();
			    		     NodeList structList = doc.getElementsByTagName("struct");

			    		     for (int i=0;i<structList.getLength();i++) 
			    		     {
			    		        Node struct = structList.item(i);
			    		        if (struct.getNodeType() == Node.ELEMENT_NODE) 
			    		        {
			    		           Element elStruct= (Element) struct;
			    		           NodeList memberList = elStruct.getElementsByTagName("member");
			    		           
			    		          for(int j=0;j<memberList.getLength();j++)
			    		          {
			    		        	  	Node member = memberList.item(j);
			    		        	  	if(member.getNodeType() == Node.ELEMENT_NODE) 
			    		        	  	{
			    		        	  		Element elMember = (Element) member;
			    		        	  		Node value =  elMember.getLastChild();
			    		        	  		Element elValue = (Element) value;
			    		        	  		NodeList temp = elValue.getElementsByTagName("string");
			    		        	  		Node n = (Node) temp.item(0);
			    		        	  		String data=null;
			    		        	  		try
			    		        	  		{
			    		        	  			data = n.getFirstChild().getNodeValue();
			    		        	  			switch(j)
				    		        	  		{
				    		        	  		case 0: nid = data; break; //should always work!
				    		        	  		case 1: message = data; break; //can be anything except null
				    		        	  		case 2: /*if(destination.trim().length()!=7&&destination.trim().length()!=10) 
				    		        	  				{
				    		        	  					destination=null;
				    		        	  					postData(nid,"DestinationNotWellFormed");
				    		        	  				}*/
				    		        	  				destination = data; break;
				    		        	  		case 5: /*if(startTime.trim().length()!=16) 
				    		        	  				{
				    		        	  					startTime=null;
				    		        	  					postData(nid,"StartTimeNotWellFormed");
				    		        	  				}*/
				    		        	  				startTime = data; break; 
				    		        	  		case 6: /*if(endTime.trim().length()!=16)
				    		        	  				{
				    		        	  					endTime=null;
				    		        	  					postData(nid,"EndTimeNotWellFormed");
				    		        	  				}*/
				    		        	  				endTime = data; break; 		
				    		        	  		}	
			    		        	  		}
			    		        	  		catch(Exception e)
			    		        	  		{
			    		        	  			//assume nid is always legitimate
			    		        	  			switch (j)
			    		        	  			{
			    		        	  				case 1: postData(nid,"DestinationNotWellFormed");break;
			    		        	  				case 4: postData(nid,"MessageNotWellFormed"); break;
			    		        	  				case 5: postData(nid,"StartTimeNotWellFormed"); break;
			    		        	  				case 6: postData(nid,"EndTimeNotWellFormed"); break;
			    		        	  			}
			    		        	  		}
			    		        	  	}
			    		          }
			    		          	addMessage(startTime,endTime,message,destination,nid,1);
			    		          	nidList+=nid+",";
			    		        }
			    		     }          
			    		           postData(nidList,"Received");
			    		     }     
			    		    catch (Exception e) {
			    		     addUpdate("Failed to update messages",0);
			    		   }
			    }
			    
			    private void postData(String nidList, String status) throws Exception
			    {
			    	try{
			    		 URL url = new URL(settings.getString("drupal_url", ""));
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
			    
			    public void attempt()
			    {
			    	DimuthNodeSaveApi myNode = new DimuthNodeSaveApi(loginId,"3534","4801234567");
			    	DrupXmlServices services = new DrupXmlServices(settings2);
			    	services.NodeSave(myNode);
			    }
			    
				  //adds an entry into the message table
			    //CHANGE TO ADD AT PROPER INDEX. 
			    private boolean addMessage(String startTime, String endTime, String message, String destination, String nid,int tableNum) 
			    {
			    	if(startTime==null||endTime==null||message==null||destination==null||nid==null) return false;
			    	DataSQLHelper eventsData = new DataSQLHelper(this);
			  	    SQLiteDatabase db = eventsData.getWritableDatabase();
			  	    ContentValues values = new ContentValues();
			  	    values.put(DataSQLHelper.START_TIME, startTime);
			  	    values.put(DataSQLHelper.END_TIME, endTime);
			  	    values.put(DataSQLHelper.MESSAGE, message);
			  	    values.put(DataSQLHelper.DESTINATION, destination);
			  	    values.put(DataSQLHelper.NID, nid);
			  	    //values.put(DataSQLHelper.READY, ready);
			  	    if(tableNum==1)db.insert(DataSQLHelper.TABLE1, null, values); 
			  	    else db.insert(DataSQLHelper.TABLE3, null, values); 
			  	    db.close();
			  	    return true;
			  	  }
			    
				  //receive messages from Akshen as a string output
			    private String getMessagesFromAkshen()
			    {
			    	String sessionId = getSessionId();
			    	if(loginId==null) return null;
			    	DrupXmlServices drupserv = new DrupXmlServices(settings2);
			    	String viewName = "sms_messages_dimuth";
			    	ViewGetApi myView = new ViewGetApi(loginId,viewName);
			    	String response = drupserv.ViewGet(myView);
			    	return response;
			    }

			    
			 
				    
				    public String getSessionId() {
				    	String user = "dimuthk";
				    	String pass = "phoenixsuns22";
				    	//Logout();
				    	//return true;
				    	/*Code to do authentication*/
				    	Context context = this;
				    	String PREFS_NAME = "akshen";
				    	settings2 = new ServicesSettings();
				    	
				    	settings2.setCleanUrl(true);
				    	settings2.setDomainName("localhost");
				    	//settings.setDrupalUrl("http://akshendemo.arl.arizona.edu/");
				    	
				    	SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
						
						try{
							settings2.setDrupalUrl(prefs.getString("server", settings.getString("drupal_url","")));
							//settings2.setDrupalUrl(prefs.getString("server", "http://akshenweb.org/"));			
							//Toast.makeText(getApplicationContext(), "Server-"+prefs.getString("server", "http://akshenweb.org/"), 10).show();
							
						}catch(ClassCastException e){
							settings2.setDrupalUrl(settings.getString("drupal_url",""));
							//settings2.setDrupalUrl("http://akshenweb.org/");
						}
						settings2.setDrupalUrl(settings.getString("drupal_url",""));
				    	//settings2.setDrupalUrl("http://akshenweb.org/");
				    	settings2.setUsername(user.trim());
				    	settings2.setPassword(pass.trim());
				    	DrupXmlServices drupserv = new DrupXmlServices(settings2);
				    	
				    	SystemConnectApi systemConnect = new SystemConnectApi();
				    	String sessionid = drupserv.GetSystemConnect(systemConnect);
				    	
				    	if(sessionid!=null){
				    		
				    		UserLoginApi login = new UserLoginApi(settings2.getUsername(),settings2.getPassword(),sessionid);
				        	
				        	String loginsessid = drupserv.Login(login); 	
				        	
				        	if(loginsessid != null){
				        		
				        		loginId = loginsessid;
				                SharedPreferences.Editor editor = prefs.edit();
				    	        editor.putString("username", user.trim());
				    	        editor.putString("password", pass.trim());
				    	        editor.putString("uid", drupserv.uid.trim());
				    	        editor.putString("sessionid", loginsessid);
				    	        editor.commit();
				    	        
				        		//Toast.makeText(getApplicationContext(), "Logged in successfully", 10).show();
				        		return loginsessid;
				        	}
				        	else{
				        		
				        		Toast.makeText(getApplicationContext(), "Login failed. Check your username and password", 10).show();
				        		return null;
				        	}	
				    	}
				    	Toast.makeText(getApplicationContext(), "Unable to connect. Check your internet connection", 10).show();
						return null;
				    }

	 
}