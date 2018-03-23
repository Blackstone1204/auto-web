/**
 * 
 */
package auto.provider.model;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import auto.provider.model.Device;
import auto.web.service.IAppiumTestService;
import auto.web.service.ICommonService;

/**
 * @author Administrator
 *
 */
public class DeviceRunnable implements Runnable{
	
	String taskTag;
	String subTaskTag;
	String userName;
	String scriptPath;
	String serial;
	String apkPath;
	String method;

	private static Logger log=Logger.getLogger(DeviceRunnable.class);

	public DeviceRunnable(String taskTag,String subTaskTag,String userName,String scriptPath, String serial,String method){
	
		this.taskTag=taskTag;
		this.subTaskTag=subTaskTag;
		this.userName=userName;
		this.scriptPath=scriptPath;
		this.serial=serial;
		//this.apkPath=apkPath;
		this.method=method;

	
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
			/**
			* @Description:TODO
			* @param 
			* @author: BlackStone
			* @time:2016年8月11日上午10:13:33
			*/
		
		log.info(String.format("--开始运行%s任务 subTaskTag=%s",method,subTaskTag));
    	if(method.equals("appium")){

    		IAppiumTestService appiumTestService=SpringContextHolder.getBean("appiumTestService");
    		appiumTestService.runTest(taskTag,subTaskTag,userName, scriptPath, serial);
    		
    		
    		
    	}else if(method.equals("robotium")){
    		
    	}else if(method.equals("monkey")){
    		
    	}
        //ICommonService commonService=(ICommonService) ctx.getBean("commonService");
        

//    	Device device=GlobalHandler.getDevice(serial);
    	
 //   	log.info("--获取到的设备运行实例="+device);
	
//		synchronized(device){
//
//			//device.runSingleCase(taskTag,userName, scriptPath, serial, apkPath, method);
//	
//		}
	}

}
