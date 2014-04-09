package drupalservices;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



import android.content.SharedPreferences;


import drupalservices.apis.FileGetApi;
import drupalservices.apis.FileSaveApi;
import drupalservices.apis.NodeGetApi;
import drupalservices.apis.NodeSaveApi;
import drupalservices.apis.ServicesApis;
import drupalservices.apis.SystemConnectApi;
import drupalservices.apis.UserLoginApi;
import drupalservices.apis.UserLogoutApi;
import drupalservices.apis.ViewGetApi;


public class DrupXmlServices implements DrupServices {

	public ServicesSettings settings;	
	public String uid="0";
	
	
	public DrupXmlServices(ServicesSettings s)
	{
		settings = s;	
		
		if(settings.getCleanUrl() == true)
		{
			settings.setDrupalServicesUrl(settings.getDrupalUrl()+"services/xmlrpc");			
		}
		else
		{
			settings.setDrupalServicesUrl(settings.getDrupalUrl()+"?q=services/xmlrpc");			
		}		
		
	}
	
	
	public String GetUnixTimeStamp() {

		Long currentTime = new Date().getTime()/100;
		return currentTime.toString();
	}

	
	//posts the data
	public String PostData(String data)
	{		
		  try {
	     
			  	String out="";
	            URL url = new URL(settings.getDrupalServicesUrl());
	            //URLConnection conn = url.openConnection();
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	            conn.setConnectTimeout(1000 * 15); 
	            conn.setDoOutput(true);
	            
	            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	            	            
	            wr.write(data);
	            wr.flush();
	            
	            // Get the response
	            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = rd.readLine()) != null) {
	                // Process line...
	            	out = out + line;
	            }
	            wr.close();
	            rd.close();
	            
	            return out;
	            
	        }catch (MalformedURLException  e) {
	        	
	        	return null;
	        	
	        }catch (IOException e) {
	        	
		        	return null;
		        	
	        }catch (Exception e) {
		        	
		        	return null;	        	
		    }	        
		
	}
	
	
	public String GetSystemConnect(ServicesApis api) {
		
		try {
			String sessionid="";
			String out =  PostData(api.getXml());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();	            
			Reader reader=new CharArrayReader(out.toCharArray());
			Document doc = builder.parse(new org.xml.sax.InputSource(reader));			
			
			if(out.contains("faultCode"))
			{
				return null;				
			}

			Element e = doc.getDocumentElement();
			sessionid = getTagValue("string",e,0);
			
			return sessionid;
			
		}catch(Exception e){
			return null;
			
		}		
		
	}	

	public String Login(UserLoginApi login) {

		try
		{
			String sessid="";
			String out = PostData(login.getXml());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();	            
			Reader reader=new CharArrayReader(out.toCharArray());
			Document doc = builder.parse(new org.xml.sax.InputSource(reader));			
			
			if(out.contains("faultCode"))
			{
				return null;				
			}
			
			Element e = doc.getDocumentElement();
			sessid = getTagValue("string",e,0);
			uid = getTagValue("string",e,1);
		
			return sessid;		
			
		}catch(Exception e)
		{
			return null;
			
		}

	}

	public Boolean Logout(UserLogoutApi n) {
	
		try
		{
			String out = PostData(n.getXml());
			
			if(out.contains("faultCode"))
			{
				return false;				
			}
			
			return true;		
			
		}catch(Exception e)
		{
			return false;
			
		}
	}

	private String getTagValue(String sTag, Element eElement,int index){
	    
		NodeList nlList= eElement.getElementsByTagName(sTag).item(index).getChildNodes();
		org.w3c.dom.Node nValue = (org.w3c.dom.Node) nlList.item(0);
	    return  nValue.getNodeValue();    
	 }
	
	public String NodeSave(NodeSaveApi node) {

		try
		{
			String nid="";
			String out = PostData(node.getXml());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();	            
			Reader reader=new CharArrayReader(out.toCharArray());
			Document doc = builder.parse(new org.xml.sax.InputSource(reader));
		
			if(out.contains("faultCode"))
			{
				return null;				
			}
			
			Element e = doc.getDocumentElement();
			nid = getTagValue("string",e,0);			
			
			return nid;				
			
		}catch(Exception e)
		{
			return null;
			
		}
		
	}

	public String FileSave(FileSaveApi file) {

		try
		{
			String fid="";
			String out = PostData(file.getXml());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();	            
			Reader reader=new CharArrayReader(out.toCharArray());
			Document doc = builder.parse(new org.xml.sax.InputSource(reader));
		
			if(out.contains("faultCode"))
			{
				return null;				
			}
			
			Element e = doc.getDocumentElement();
			fid = getTagValue("string",e,0);			
			
			return fid;		
			
		}catch(Exception e)
		{
			return null;
			
		}
		
	}
	
	public String ViewGet(ViewGetApi view) {

		try
		{
			
			String viewXml = PostData(view.getXml());
					
			if(viewXml.contains("faultCode"))
			{
				return null;				
			}
			
			return viewXml;		
			
		}catch(Exception e)
		{
			return null;
			
		}
		
	}

	public String FileGet(FileGetApi fileget) {

		try
		{
			String filedata = "";
			String out = PostData(fileget.getXml());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();	            
			Reader reader=new CharArrayReader(out.toCharArray());
			Document doc = builder.parse(new org.xml.sax.InputSource(reader));
		
			if(out.contains("faultCode"))
			{
				return null;				
			}
			
			Element e = doc.getDocumentElement();
			filedata = getTagValue("string",e,2);
			return filedata;	
			
		}catch(Exception e)
		{
			return null;
			
		}
		
	}


	public String NodeGet(NodeGetApi node) {
		
		try
		{
			String out = PostData(node.getXml());
			
			if(out.contains("faultCode"))
			{
				return null;				
			}
			
			return out;	
			
		}catch(Exception e)
		{
			return null;
			
		}
	}

}
