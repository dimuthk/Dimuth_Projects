package drupalservices.apis;

import java.util.Date;

public abstract class ServicesApis {

	public String xml;
	public String apiName;
	
	public String GetUnixTimeStamp() {

		Long currentTime = new Date().getTime()/100;
		return currentTime.toString();
	}
	
	public String getXml()
	{
		return xml;
		
	}
	
}
