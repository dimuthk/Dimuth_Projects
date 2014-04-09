package drupalservices.apis;

public class FileGetApi extends ServicesApis {

	
	private String fid;
	
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	

	public FileGetApi(String sessionid, String fid)
	{
		apiName = "file.get";
		xml = 
			"<?xml version=\"1.0\"?>" +
					"<methodCall>" +
						"<methodName>"+apiName.trim()+"</methodName>" +
						"<params>" +
							"<param>" +
								"<value><string>"+sessionid.trim()+"</string></value>" +
							"</param>" +
							"<param>" +
								"<value><i4>"+fid.trim()+"</i4></value>" +
							"</param>" +
						"</params>" +
					"</methodCall>";		
		
	}
	
	
}
