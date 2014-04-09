package com.example.bluefox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import drupalservices.DrupXmlServices;
import drupalservices.ServicesSettings;
import drupalservices.apis.SystemConnectApi;
import drupalservices.apis.UserLoginApi;
import drupalservices.apis.ViewGetApi;


import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;



//At x frequency, access the message feed and grab n messages from the feed that
//haven't expired. Simutaneously write to the message that you have already selected
//that message. Do not take messages that have been noted as such. 

public class UpdateMessages extends Service
{
	private String DATE_FORMAT = "yyyy-MM-dd HH:MM";
	private Timer updateTimer;
	private String loginId;
	private SharedPreferences settings;
	DataSQLHelper eventsData;
	ServicesSettings settings2;
	@Override
	   public IBinder onBind(Intent arg0) {
	        return null;
	    }
	  
	
	    @Override
  public void onCreate() {
	    	super.onCreate();
	    	updateTimer = new Timer();
	    	settings = this.getSharedPreferences("settings",MODE_WORLD_READABLE);
	    	eventsData = new DataSQLHelper(this);
	    	TimerTask update = new TimerTask()
	    	{
				@Override
				public void run() 
				{
						updateMessages(getMessagesFromAkshen());
				}	
	    	};

	    	updateTimer.scheduleAtFixedRate(update,1000*5,settings.getInt("updateMsgRate", 60*1000));
	   }

	    @Override
	  public void onDestroy() {
	      super.onDestroy();
	      updateTimer.cancel();
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
		    		        	  		case 1: /*if(destination.trim().length()!=7&&destination.trim().length()!=10) 
		    		        	  				{
		    		        	  					destination=null;
		    		        	  					postData(nid,"DestinationNotWellFormed");
		    		        	  				}*/
		    		        	  				destination = data; break;
		    		        	  		case 4: message = data; break; //can be anything except null
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
