package drupalservices.apis;

public class UserLoginApi extends ServicesApis{
	
	public UserLoginApi(String user,String password,String sessionid)
	{
		
		apiName = "user.login";
		xml = 
			"<?xml version=\"1.0\"?>" +
					"<methodCall><methodName>user.login</methodName>" +
						"<params>" +
							"<param>" +
								"<value><string>"+sessionid+"</string></value>" +
							"</param>" +
							"<param>" +
								"<value><string>"+user+"</string></value>" +
							"</param>" +
							"<param>" +
								"<value><string>"+password+"</string></value>" +
							"</param>" +
						"</params>" +
					"</methodCall>";		
		
	}
	
	
}
