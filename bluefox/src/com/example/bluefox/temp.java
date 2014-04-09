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
import drupalservices.apis.SystemConnectApi;
import drupalservices.apis.UserLoginApi;



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



public class temp extends Activity {
private String sessionid;	
ServicesSettings settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
    }
    
  //searches the akshen website and will update the message table as necessary
    private void updateMessages(String input)
    {
    	  try {	     
    		    
    		    
    		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		    Document doc = dBuilder.parse(input);
    		    String nidList="";
    		    
    		     
    		     doc.getDocumentElement().normalize();
    		     NodeList structList = doc.getElementsByTagName("struct");
    		     //each 
    		     //nid,destination,node_type,vid,message,start,end
    		     
    		     for (int i=0;i<structList.getLength();i++) 
    		     {
    		        Node struct = structList.item(i);
    		        if (struct.getNodeType() == Node.ELEMENT_NODE) 
    		        {
    		           Element el;
    		           
    		           
    		         
    		        }
    		     }
    
}
    	  catch(Exception e)
    	  {
    		  
    	  }
    
    }
}