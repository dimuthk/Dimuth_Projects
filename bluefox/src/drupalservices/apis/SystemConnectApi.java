package drupalservices.apis;

public class SystemConnectApi extends ServicesApis{

	public SystemConnectApi()
	{
		apiName = "system.connect";
		xml = 
			"<?xml version=\"1.0\"?>" +
			"<methodCall>" +
				"<methodName>system.connect</methodName>" +
			"</methodCall>";
		
	}	
	
}
