package auto.web.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import auto.provider.model.DeviceRunnable;
import auto.provider.model.SpringContextHolder;
import auto.provider.model.SubTask;
import auto.provider.model.Task;
import auto.provider.service.IDeviceService;
import auto.provider.service.ITaskService;
import auto.web.service.IAppiumTestService;
import auto.web.service.ICommonService;
import auto.web.service.ITimedTask;
import auto.web.util.TaskQueue;

@Component  //import org.springframework.stereotype.Component;  
public class TimedTask  implements ITimedTask {  
  
    private Logger log=Logger.getLogger(TimedTask.class);
    @Scheduled(cron="0/2 * *  * * ? ")
    public void globalHandlerSync(){
		try {
			ApplicationContext ctx=SpringContextHolder.getApplicationContext();
	        ICommonService commonService=(ICommonService) ctx.getBean("commonService");
			commonService.globalHandlerSync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Scheduled(cron="0/5 * *  * * ? ")
    public void checkTask(){
    	IAppiumTestService appiumTestService=SpringContextHolder.getBean("appiumTestService");
    
    	SubTask subTask=TaskQueue.getSubTask();
    
    	TaskQueue.getSize();
    	if(null==subTask){
    		log.info("--没拿到任务");
    		return;
    	}
    	String subTaskTag=subTask.getSubTag();
    	String  taskTag=subTask.getTaskTag();
    	log.info("拿到子任务 subTaskTag="+subTaskTag);
 		ITaskService taskService=SpringContextHolder.getBean("taskService");
 		Task task=taskService.findTaskByTaskTag(taskTag);
 		
 		if(task==null){
 			log.info("--没找到父任务");
 			return;
 		}
 		
    	String userName=task.getUserName();
    	Map<String,String> scriptMap=appiumTestService.getLastClassesByUserSerial(userName);

    	String  serial=subTask.getSerial();
    	String scriptPath=subTask.getScriptName();
    	String apkPath="";
    	String method=task.getMethod();
        if(null!=subTask)new Thread(new DeviceRunnable(taskTag, subTaskTag,userName,scriptPath,serial,method)).start();
    }
}  