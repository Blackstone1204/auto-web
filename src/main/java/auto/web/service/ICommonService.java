/**
 * 
 */
package auto.web.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import auto.provider.model.Device;

/**
 * @author BlackStone
 *
 */
public interface ICommonService {
	public Device getSyncDevice(String serial);
	public void globalHandlerSync();
	
	public void scriptSave(MultipartFile file,String userName);
	public void scriptUnpackageDirCopyTo(String userName,String method,String fileName);
	
	public String projectionInit(String serial);
	
	public void actionExecution(String serial,String userName,String actionName,String args,String sw,boolean isRecording,String mode,String time);
	
	public void actionRecord(String userName,String serial,String actionName,String args);
	
	public void recordStop(String userName,String serial,String recordName,String tip,int method,String time);
	
	public Map<String,Object> playback(String[] serials,String userName,String recordId,String sw,String time);
	
	public byte[] showPage(String serial);
	
	public void screenshotByHand(String serial,String userName,boolean isPlayBack,boolean isRecording,String mode,String time);

	
	


}
