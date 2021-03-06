package drupalservices.apis;

import java.io.BufferedInputStream;

import java.io.FileInputStream;

//import org.apache.commons.codec.binary.Base64;

import android.util.Log;

public class FileSaveApi extends ServicesApis {

	
	private String filedata="";
	private String filepath="sites/default/files/";
	private String filesize="";
	private String filename="";
	private String file="";
		
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


	private String timestamp=GetUnixTimeStamp();
	private String uid="";
	private String sessionid="";
	
	public FileSaveApi(String userid,String sessid)
	{
		
		apiName = "file.save";
		uid = userid;
		sessionid = sessid;
		
	}

	/*public String getXml()
	{				
	
	    FileInputStream in;
        BufferedInputStream buf;
        try {
	   	      in = new FileInputStream(file);
	            buf = new BufferedInputStream(in);            
	        
	            byte[] bMapArray= new byte[buf.available()];
	            buf.read(bMapArray);            
	            
	            //filedata = new String(Base64.encodeBase64(bMapArray));	    	                         
	            filedata = new String(Base64.encodeBase64(bMapArray, true));
	            filesize = (new Integer(filedata.length())).toString();
	            
	            Log.d("encoded",filedata);
	            if (in != null) {
	           	 in.close();
	            }
	            if (buf != null) {
	           	 buf.close();
	            }
	    } catch (Exception e) {
	        Log.e("Error reading file", e.toString());
	    } 
	    
	xml = 
			"<?xml version=\"1.0\"?>" +
				"<methodCall>" +
					"<methodName>"+apiName.trim()+"</methodName>" +
					"<params>" +
						"<param>" +
							"<value><string>"+sessionid.trim()+"</string></value>" +
						"</param>" +
						"<param>" +
							"<value>" +
								"<struct>" +
									"<member>" +
										"<name>filepath</name>" +
										"<value><string>"+filepath.trim()+filename.trim()+"</string></value>" +
									"</member>" +
									"<member>" +
										"<name>uid</name>" +
										"<value><string>"+uid.trim()+"</string></value>" +
									"</member>" +
									"<member>" +
										"<name>filesize</name>" +
										"<value><string>"+filesize.trim()+"</string></value>" +
									"</member>" +
									"<member>" +
										"<name>filename</name>" +
										"<value><string>"+filename.trim()+"</string></value>" +
									"</member>" +
									"<member>" +
										"<name>timestamp</name>" +
										"<value><string>"+timestamp.trim()+"</string></value>" +
									"</member>" +
									"<member>" +
										"<name>file</name>" +
										"<value><string><![CDATA["+filedata.trim()+"]]></string></value>" +
									"</member>" +
								"</struct>" +
							"</value>" +
						"</param>" +
					"</params>" +
				"</methodCall>";	
		return xml;
	}*/
	
	

	public String getTimestamp() {
		return timestamp;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}
	
	
}
