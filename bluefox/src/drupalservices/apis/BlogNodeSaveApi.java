package drupalservices.apis;


public class BlogNodeSaveApi extends NodeSaveApi {

	final String type = "blog";
	private NodeLocation location;
	private String name="";
	private String sessionid="";
	private String body="";
	private String title="";
	private String current="";
	
	public BlogNodeSaveApi(String sessid,String blogtitle,String blogbody,NodeLocation nloc)
	{		
		sessionid = sessid;
		title = blogtitle;
		body = blogbody;
		location = nloc;
		if(nloc.getLatitude()==null)nloc.setLatitude("");
		if(nloc.getLongitude()==null)nloc.setLongitude("");
	}

	public void setLocation(NodeLocation location) {
		
		if(location.getLatitude()!=null && location.getLongitude()!=null)
		this.location = location;
	}

	public NodeLocation getLocation() {
		
		return location;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		
		if(name!=null){
			this.name = name;
		}
		else{
			name = "";
		}
			
	}
	
	public String getXml()
	{
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
							"<name>nid</name>" +
							"<value><string>"+"3533"+"</string></value>" +
							"</member>" +
							"<member>" +
								"<name>changed</name>" +
								"<value><string>"+"1300688296"+"</string></value>" +
							"</member>" +
							"<member>" +
								"<name>body</name>" +
								"<value><string>"+body.trim()+"</string></value>" +
							"</member>" +
						"</struct>" +
					"</value>" +
					"</param>" + 
					"</params>" +
					"</methodCall>";
		return xml;
			
	}

	/*public String getXml()
	{
		
		String taxonomyXml = "";
	
		if(current!="")
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
		}
		
		
		
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
								"<name>body</name>" +
								"<value><string>"+body.trim()+"</string></value>" +
							"</member>" +
							taxonomyXml +
							"<member>" +
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
							"</member>" +
							"<member>" +
								"<name>name</name>" +
								"<value><string>"+name.trim()+"</string></value>" +
							"</member>" +
							"<member>" +
								"<name>title</name>" +
								"<value><string>"+title.trim()+"</string></value>" +
							"</member>" +
							"<member>" +
								"<name>type</name>" +
								"<value><string>"+type.trim()+"</string></value>" +
							"</member>" +
						"</struct>" +
					"</value>" +
				"</param>" +
			"</params>" +
		"</methodCall>";	
		
		return xml;
	}*/

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getCurrent() {
		return current;
	}
}
