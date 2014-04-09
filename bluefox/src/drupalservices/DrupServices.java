package drupalservices;

import drupalservices.apis.FileGetApi;
import drupalservices.apis.FileSaveApi;
import drupalservices.apis.NodeGetApi;
import drupalservices.apis.NodeSaveApi;
import drupalservices.apis.ServicesApis;
import drupalservices.apis.UserLoginApi;
import drupalservices.apis.UserLogoutApi;
import drupalservices.apis.ViewGetApi;


public interface DrupServices {

	public String GetUnixTimeStamp();
	public String GetSystemConnect(ServicesApis api);	
	public String Login(UserLoginApi api);
	public Boolean Logout(UserLogoutApi logout);
	public String NodeSave(NodeSaveApi n);
	public String NodeGet(NodeGetApi n);
	public String FileSave(FileSaveApi file);
	public String FileGet(FileGetApi fileget);
	public String ViewGet(ViewGetApi view);
}
