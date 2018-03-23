/**
 * 
 */
package auto.provider.service;

import java.io.File;
import java.io.IOException;

/**
 * @author BlackStone
 *
 */
public interface IAppiumSyncService {
	public String apkSave(byte[] bytes,String userName) throws IllegalStateException, IOException;
	public File getLastApk(String userName);
	public void installApk(String userName,String serial);
	public void gather();

}
