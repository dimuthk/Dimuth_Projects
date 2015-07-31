package drupalservices;

public class ServicesSettings {

	private Boolean cleanUrl = false;
	private String drupalUrl;
	private String drupalServicesUrl;
	private Boolean useSessionID;
	private Boolean useKey;
	private String key;
	private String domainName;
	private String username;
	private String password;
	private String sessionId;
	
	
	public String getDrupalUrl() {
		return drupalUrl;
	}

	public void setDrupalUrl(String drupalUrl) {
		this.drupalUrl = drupalUrl;
	}

	public String getDrupalServicesUrl() {
		return drupalServicesUrl;
	}

	public void setDrupalServicesUrl(String drupalServicesUrl) {
		this.drupalServicesUrl = drupalServicesUrl;
	}

	public Boolean getUseSessionID() {
		return useSessionID;
	}

	public void setUseSessionID(Boolean useSessionID) {
		this.useSessionID = useSessionID;
	}

	public Boolean getUseKey() {
		return useKey;
	}

	public void setUseKey(Boolean useKey) {
		this.useKey = useKey;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setCleanUrl(Boolean cleanUrl) {
		this.cleanUrl = cleanUrl;
	}

	public Boolean getCleanUrl() {
		return cleanUrl;
	}
	
	
	
	
	
}
