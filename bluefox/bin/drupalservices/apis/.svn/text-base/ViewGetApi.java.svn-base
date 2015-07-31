package drupalservices.apis;

public class ViewGetApi extends ServicesApis {

	
	private String view;
	
		

	public ViewGetApi(String sessionid, String viewName)
	{
		apiName = "views.get";
		xml = 
			"<?xml version=\"1.0\"?>" +
					"<methodCall>" +
						"<methodName>"+apiName.trim()+"</methodName>" +
						"<params>" +
							"<param>" +
								"<value><string>"+sessionid.trim()+"</string></value>" +
							"</param>" +
							"<param>" +
								"<value><string>"+viewName.trim()+"</string></value>" +
							"</param>" +
						"</params>" +
					"</methodCall>";		
		
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getView() {
		return view;
	}
	
	
}
