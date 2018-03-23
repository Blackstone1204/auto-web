/**
 * 
 */
package auto.provider.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import auto.provider.service.IDeviceService;
import auto.provider.service.ISubTaskService;
import auto.provider.service.ITaskService;
import auto.web.controller.UserOperate;
import auto.web.service.IAppiumTestService;
import auto.web.service.ICommonService;
import auto.web.util.TaskQueue;

/**
 * @author BlackStone
 *
 */
public class TestCase {
	/** 
	 * @ClassName: TestCase 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年9月22日 下午4:40:24 
	 * 
	 */
	
	private static Logger log=Logger.getLogger(TestCase.class);
	
	public static void addToTaskQueue(Map<String,String>map,String[] serials,String userName,String apkPath,String method){

		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		ITaskService taskService=SpringContextHolder.getBean("taskService");

		IAppiumTestService appiumTestService=SpringContextHolder.getBean("appiumTestService");

		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date now=new Date();
		String taskTag=sdf.format(now);
		String serialStr=getSerialStr(serials);
        String scriptStr=getScriptStr(map);
	       //db写入一条任务
			Task task=new Task();
			task.setApkName(new File(apkPath).getName());
			task.setApkName2(appiumTestService.getLastApk(userName).getName());
			task.setUploadName("unknown");
			task.setTaskDir(appiumTestService.getLastUserDicPath(userName));
			
			task.setDeviceSerial(serialStr);
			task.setMethod(method);
			task.setResult("running");
			task.setScriptName(scriptStr);
			task.setTaskName(String.format("%s测试", method));
			task.setTaskTag(taskTag);
			task.setUserName(userName);
			task.setPkgName("unknown");
			task.setModel(getModelStr(serials));
			taskService.submit(task);
			
			Set<String> keys=map.keySet();
			for(String key:keys){
				

				String subTaskTag=getTimeStr();
				
				SubTask subTask=new SubTask();
				subTask.setSubTag(subTaskTag);
				subTask.setTaskTag(taskTag);
				subTask.setSerial(map.get(key));
				
				Device device=deviceService.findDeviceBySerial(map.get(key));
				if(null!=device)
					subTask.setModel(device.getModel());
				
				subTask.setResult("waiting");
				subTask.setScriptName(new File(key+".class").getAbsolutePath());
			
				TaskQueue.addSubTask(subTask);
				
			}
	}
	
	public static String getScriptStr(Map<String,String> scriptMap){
		
		String str="";
		Set<String> keys=scriptMap.keySet();
		
		for(String key:keys){
			log.info(String.format("--脚本映射关系 key=%s value=%s ", key,scriptMap.get(key)));
			
			str+=";"+key+".class";
		}
		
		return str;

		
	}
	public static String getSerialStr(String[] serials){
		String serialStr ="";
		for(String serial:serials){
			if(serial.length()<1)continue;
			serialStr+=";"+serial;
		}
		
		return serialStr;
	}
	
	public static String getModelStr(String[] serials){
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		String modelStr="";
		for(String serial:serials){
			if(serial.length()<1)continue;
			Device device=deviceService.findDeviceBySerial(serial);
			if(null==device)continue;
			String model=device.getModel();
			modelStr+=";"+model;
			
		}
		
		return modelStr;
		
	}
	
	public static void delay(int time){
		try{
			Thread.sleep(time);
		}catch(Exception ex){
			
		}
		
	}
	public static String getTimeStr(){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date now=new Date();
		
		return sdf.format(now)+"_"+new Random().nextInt(100);
	}
	
	
}
