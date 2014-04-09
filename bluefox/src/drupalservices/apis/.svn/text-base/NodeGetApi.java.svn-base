package drupalservices.apis;

public class NodeGetApi extends ServicesApis {
	
	private String sessid;
	private String nodeid;
	
	
	public NodeGetApi(String sessionid,String nid)
	{
		sessid = sessionid;
		nodeid = nid;
		apiName= "node.get";
		
	}

	
	public String getXml()
	{
		xml = 
			"<?xml version=\"1.0\"?>" +
			"<methodCall>" +
			"<methodName>"+apiName.trim()+"</methodName>" +
			"<params>" +
				"<param>" +
					"<value><string>"+sessid.trim()+"</string></value>" +
				"</param>" +
				"<param>" +
					"<value><i4>"+nodeid.trim()+"</i4></value>" +
				"</param>" +
			"</params>" +
			"</methodCall>";	
		return xml;
		
	}

	public void setSessid(String sessid) {
		this.sessid = sessid;
	}


	public String getSessid() {
		return sessid;
	}


	public void setNid(String nid) {
		this.nodeid = nid;
	}


	public String getNid() {
		return nodeid;
	}

}
