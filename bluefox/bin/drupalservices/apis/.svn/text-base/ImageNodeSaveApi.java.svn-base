package drupalservices.apis;


public class ImageNodeSaveApi extends NodeSaveApi{

	final String type = "image";
	private NodeLocation location=null;
	private String name="";
	private String imagedata="";
	private String sessionid="";
	private String title="";
	private String body="";
	private Boolean food=false;
	private String current="";
	
	public ImageNodeSaveApi(String sessid,String imagetitle,String imagecaption)
	{		
		if(sessid!=null)
			sessionid = sessid;
		if(imagetitle!=null)
			title = imagetitle;
		if(imagecaption!=null)
			body = imagecaption;	
		
	}

	public String getImagedata() {
		return imagedata;
	}

	public void setImagedata(String imagedata) {
		if(imagedata!=null)
			this.imagedata = imagedata;
	
	}
	
	public void setName(String name) {
		if(name!=null)
			this.name = name;
	
	}

	public String getName() {
		return name;
		
	}

	public void setLocation(NodeLocation location) {
		if(location.getLatitude()!=null && location.getLongitude()!=null)
			this.location = location;
	
	}

	public NodeLocation getLocation() {
		return location;
	}

	public String getXml()
	{
		
		String taxonomyXml = "";
		String locationXml = "";
		
		
		if(food==true)
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
														"<value><string>food</string></value>" +
													"</member>" +
												"</struct>" +
											"</value>" +
										"</member>" +
									"</struct>" +
								"</value>" +
							"</member>" ;
		}
		
		
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
		
		
		if(this.location!=null)
		{
			
			locationXml = "<member>" +
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
						"</member>";
			
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
							locationXml + 
							"<member>" +
								"<name>images</name>" +
								"<value>" +
									"<struct>" +
										"<member>" +
											"<name>preview</name>" +
											"<value><string>sites/default/files/"+imagedata.trim()+"</string></value>" +
										"</member>" +
										"<member>" +
											"<name>thumbnail</name>" +
											"<value><string>sites/default/files/"+imagedata.trim()+"</string></value>" +
										"</member>" +
										"<member>" +
											"<name>_original</name>" +
											"<value><string>sites/default/files/"+imagedata.trim()+"</string></value>" +
										"</member>" +
									"</struct>" +
								"</value>" +
							"</member>" + 
							taxonomyXml +
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
		
	}

	public void setFood(Boolean food) {
		this.food = food;
	}

	public Boolean getFood() {
		return food;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getCurrent() {
		return current;
	}
	

}
