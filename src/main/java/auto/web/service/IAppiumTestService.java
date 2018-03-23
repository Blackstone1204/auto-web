/**
 * 
 */
package auto.web.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author BlackStone
 *
 */
public interface IAppiumTestService {
	public void packageSave(MultipartFile file,String userName);
	public File getLastApk(String userName);
	public String getLastUserDicPath(String userName);
	public void init(String userName,String serialStr);
	public Map<String,String> getLastClassesByUserSerial(String userName);
	public void transferFile(String  serialStr,String userName,Map<String,Object>map);
	public void complie(String userName,String[] serials);
	public String executeClass(String subTaskTag,String userName,String serial,String scriptPath);
	public void runTest(String taskTag,String subTaskTag, String userName,String scriptName,String serial);
	public void gather();

}
