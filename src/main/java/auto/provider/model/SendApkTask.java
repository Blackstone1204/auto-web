/**
 * 
 */
package auto.provider.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import auto.provider.service.IAppiumSyncService;
import com.alibaba.dubbo.config.ReferenceConfig;

/**
 * @author BlackStone
 *
 */
public class SendApkTask implements Runnable{

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	
	public static Logger log=Logger.getLogger(SendApkTask.class);
	ReferenceConfig<IAppiumSyncService> ref=null;
	//InputStream fin=null;
	byte[] bytes=null;
	String userName=null;
	public SendApkTask(	ReferenceConfig<IAppiumSyncService> ref,byte[] bytes,String userName){
		//this.fin=fin;
		this.bytes=bytes;
		this.userName=userName;
		this.ref=ref;

	}
	public void run() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年9月5日下午3:19:22
			 */
		try {
			//log.info("==fin "+fin);
			//log.info("==bytes "+bytes);
			ref.get().apkSave(bytes, userName);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
