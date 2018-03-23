/**
 * 
 */
package auto.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
















import auto.provider.model.Device;
import auto.provider.model.FileUtil;
import auto.provider.model.SpringContextHolder;
import auto.provider.model.SubTask;
import auto.provider.model.Task;
import auto.provider.model.TestCase;
import auto.provider.service.IAppiumSyncService;
import auto.provider.service.IDeviceService;
import auto.provider.service.ISubTaskService;
import auto.provider.service.ITaskService;
import auto.web.service.IAppiumTestService;
import auto.web.service.ICommonService;
import auto.web.util.TaskQueue;
import auto.web.util.UsedDevice;

/**
 * @author BlackStone
 *
 */
@Controller
public class TestOperate {
	/** 
	 * @ClassName: UserConTroller 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年8月31日 上午11:29:08 
	 * 
	 */
	
	
	
	private static Logger log=Logger.getLogger(TestOperate.class);
	
	
	@ResponseBody
	@RequestMapping("/free")
	public Map<String,Object> free(){
 		//ITaskService taskService=SpringContextHolder.getBean("taskService");
 		//Task task=taskService.findTaskByTaskTag("2017-10-26_09-59-35");
 		//log.info("test task="+task);
 		
		TaskQueue.clear();
		UsedDevice.clear();
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		resultMap.put("code", 0);


		
		return resultMap;
	}
	@ResponseBody
	@RequestMapping("/query")
	public Map<String,Object> test(String userName){
 		ITaskService taskService=SpringContextHolder.getBean("taskService");
 		ISubTaskService subTaskService=SpringContextHolder.getBean("subTaskService");
 		
 		List<Task> list=taskService.findTaskByUser(userName);
 		List<SubTask> list2=new ArrayList<SubTask>();
 		
 		for(Task task:list){
 			List<SubTask> ls=subTaskService.findByTaskTag(task.getTaskTag());
 			log.info("ls size="+ls.size());
 			for(SubTask subTask:ls){
 				list2.add(subTask);
 				
 			}
 		}
 		
		Map<String,Object> resultMap=new HashMap<String,Object>();

		resultMap.put("usedDevice",UsedDevice.getUsed());
		resultMap.put("tasklist", TaskQueue.getTaskList());
		resultMap.put("history",list);
		resultMap.put("history2",list2);
		
		
		return resultMap;
	}
	
	
	
	
}
