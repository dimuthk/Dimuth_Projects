package drupalservices.apis;


public class StatusNodeSaveApi extends NodeSaveApi {

	final String type = "status";
	private String status="";
	private String nid = "";
	//private String name="";
	private String sessionid="";

	
	public StatusNodeSaveApi(String sessid,String status, String nid)
	{		
		this.status=status;
		this.nid=nid;
		sessionid = sessid;
		
	}

	public void setStatus(String mystatus) {
		
		if(mystatus!=null)
		status=mystatus;
	}

	public String getStatus()
	{
		return status;
	}
	
	/*public String getName() {
		return name;
	}
	


	public void setName(String name) {
		
		if(name!=null){
			this.name = name;
		}
		else{
			name = "";
		}
			
	}*/
	
	public void setNid(String nid) {
		
		if(nid!=null)
		this.nid=nid;
	}

	public String getNid()
	{
		return nid;
	}

	public String getXml()
	{
		
		String taxonomyXml = "";
		//current is a blog tag
		/*if(current!="")
		{	taxonomyXml = 	"<member>" +
								"<name>taxonomy</name>" +
								"<value>" +
									"<struct>" +
										"<member>" +
											"<name>tags</name>" +
											"<value>" +
												"<struct>" +
													"<member>" +
														"<name>4</name>" +
														"<value><string>"+current+"</string></value>" +
													"</member>" +
												"</struct>" +
											"</value>" +
										"</member>" +
									"</struct>" +
								"</value>" +
							"</member>" ;
		}*/
		
		
		
		xml = 
			"<?xml version=\"1.0\"?>" +
				"<methodCall>" +
					"<methodName>"+apiName.trim()+"</methodName>" +
					"<params>" +
					"<param><value><string>"+sessionid.trim()+"</string></value></param>" +
					"<param>" +
					"<value>" +
						"<struct>" +
							"<member>" +
								"<name>nid_list</name>" +
								"<value><string>"+nid.trim()+"</string></value>" +
							"</member>" +
							taxonomyXml +
							/*"<member>" +
								"<name>locations</name>" +
								"<value>" +
									"<array>" +
										"<data>" +
											"<value>" +
												"<struct>" +
													"<member>" +
														"<name>latitude</name>" +
														"<value><string>"+location.getLatitude().trim()+"</string></value>" +
													"</member>" +
													"<member>" +
														"<name>longitude</name>" +
														"<value><string>"+location.getLongitude().trim()+"</string></value>" +
													"</member>" +
												"</struct>" +
											"</value>" +
										"</data>" +
									"</array>" +
								"</value>" +
							"</member>"*/
							/*"<member>" +
								"<name>name</name>" +
								"<value><string>"+name.trim()+"</string></value>" +
							"</member>" +*/
							"<member>" +
								"<name>new_status</name>" +
								"<value><string>"+status.trim()+"</string></value>" +
							"</member>" +
							/*"<member>" +
								"<name>type</name>" +
								"<value><string>"+type.trim()+"</string></value>" +
							"</member>" +*/
						"</struct>" +
					"</value>" +
				"</param>" +
			"</params>" +
		"</methodCall>";	
		
		return xml;
	}

	
}
