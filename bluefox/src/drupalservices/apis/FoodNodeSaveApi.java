package drupalservices.apis;


public class FoodNodeSaveApi extends NodeSaveApi{

	final String type = "recall";
	private NodeLocation location;
	private String name;
	private String field_recall;
	private String cellid;
	
	public void setLocation(NodeLocation location) {
		this.location = location;
	}

	public NodeLocation getLocation() {
		return location;
	}

	public FoodNodeSaveApi(String sessionid,String title,String body)
	{		
			xml = 
				"<?xml version=\"1.0\" ?>" +
					"<methodCall><methodName>"+apiName.trim()+"</methodName>" +
						"<params>" +
							"<param><value><string>"+sessionid.trim()+"</string></value></param>" +
							"<param><value>" +
								"<struct>" +
									"<member>" +
										"<name>body</name>" +
										"<value><string>"+body.trim()+"</string></value>" +
									"</member>" +
								/*	"<member>" +
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
									"</member>" + */
									"<member>" +
										"<name>field_recall</name>" +
										"<value>" +
											"<array>" +
												"<data>" +
													"<value>"+field_recall.trim()+"</value>" +
												"</data>" +
											"</array>" +
										"</value>" +
									"</member>" +
								/*	"<member>" +
										"<name>field_cellid</name>" +
										"<value>" +
											"<array>" +
												"<data>" +
													"<value>" +
														"<struct>" +
															"<member>" +
																"<name>value</name>" +
																"<value>" +
																	"<string>"+cellid.trim()+"</string>" +
																"</value>" +
															"</member>" +
															"<member>" +
																"<name>safe</name>" +
																"<value>" +
																	"<string>"+cellid.trim()+"</string>" +
																"</value>" +
															"</member>" +
															"<member>" +
																"<name>view</name>" +
																"<value>" +
																	"<string>"+cellid.trim()+"</string>" +
																"</value>" +
															"</member>" +
														"</struct>" +
													"</value>" +
												"</data>" +
											"</array>" +
										"</value>" +
									"</member>" +
									"<member>" +
										"<name>field_locationsrc</name>" +
										"<value>" +
											"<array>" +
												"<data>" +
													"<value>" +
														"<struct>" +
															"<member>" +
																"<name>value</name>" +
																"<value>" +
																	"<string>google</string>" +
																"</value>" +
															"</member>" +
															"<member>" +
																"<name>safe</name>" +
																"<value>" +
																	"<string>google</string>" +
																"</value>" +
															"</member>" +
															"<member>" +
																"<name>view</name>" +
																"<value>" +
																	"<string>google</string>" +
																"</value>" +
															"</member>" +
														"</struct>" +
													"</value>" +
												"</data>" +
											"</array>" +
										"</value>" +
									"</member>" +  */
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
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setField_recall(String field_recall) {
		this.field_recall = field_recall;
	}

	public String getField_recall() {
		return field_recall;
	}

	public void setCellid(String cellid) {
		this.cellid = cellid;
	}

	public String getCellid() {
		return cellid;
	}
	
}
