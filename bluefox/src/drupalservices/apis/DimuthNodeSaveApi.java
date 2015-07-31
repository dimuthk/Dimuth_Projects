package drupalservices.apis;

public class DimuthNodeSaveApi extends NodeSaveApi
{
	final String type = "akshen_message";

	public DimuthNodeSaveApi(String sessionid, String nid, String from)
	{
		xml = 
			"<?xml version=\"1.0\" ?>" +
				"<methodCall><methodName>"+apiName.trim()+"</methodName>" +
					"<params>" +
						"<param><value><string>"+sessionid.trim()+"</string></value></param>" +
						"<param><value>" +
							"<struct>" +
								"<member>" +
									"<name>nid</name>" +
									"<value><string>"+nid.trim()+"</string></value>" +
								"</member>" +
								"<member>" +
								"<name>body</name>" +
								"<value><string>"+"AAA"+"</string></value>" +
							"</member>" +
								"<member>" +
									"<name>changed</name>" +
									"<value><string>1300742856</string></value>" +
								"</member>" +
								/*"<member>" +
								"<name>title</name>" +
								"<value><string>hello</string></value>" +
								"</member>" +*/
								"<member>" +
									"<name>field_destination_value</name>" +
									"<value>" +
									"<array>" +
										"<data>" +
											"<value>" +
												"<struct>" +
													"<member>" +
														"<name>0</name>" +
														"<value>" +
														"<array>" +
															"<data>" +
																"<value>" +
																	"<struct>" +
																		"<member>" +
																			"<name>value</name>" +
																			"<value><string>"+from+"</string></value>" +
																		"</member>" +
																	"</struct>" +
																"</value>" +
															"</data>" +
														"</array>" +
														"</value>" +
													"</member>" +
												"</struct>" +
											"</value>" +
										"</data>" +
									"</array>" +
								"</value>" +
								"</member>" +
							"</struct>" +
						"</value></param>" +
					"</params>" +
				"</methodCall>";
						
			

	}
}
