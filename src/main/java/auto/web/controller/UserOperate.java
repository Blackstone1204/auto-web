/**
 * 
 */
package auto.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import auto.provider.model.Record;
import auto.provider.model.SpringContextHolder;
import auto.provider.model.Task;
import auto.provider.model.TestCase;
import auto.provider.service.IAppiumSyncService;
import auto.provider.service.IDeviceService;
import auto.provider.service.IProjectionService;
import auto.provider.service.IRecordService;
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
public class UserOperate {
	/** 
	 * @ClassName: UserConTroller 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年8月31日 上午11:29:08 
	 * 
	 */
	
	
	
	private static Logger log=Logger.getLogger(UserOperate.class);
	
	
	@ResponseBody
	@RequestMapping("/loadDevice")
	public Map<String,Object> loadDevice(HttpServletResponse response){
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map<String,Object> infoMap=new HashMap<String,Object>();
		List<Object> deviceInfoList=new ArrayList<Object>();
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		List<Device> list=deviceService.mGetOnlineDevice();
		if(list.size()<1){
			
			resultMap.put("code", 0);
			resultMap.put("msg","无在线设备 加载失败");
			return resultMap;
		}
		
		for(Device device:list){
			Map<String,Object> attrMap=new HashMap<String,Object>();
			attrMap.put("serial",device.getSerial());
			attrMap.put("model",device.getModel());
			attrMap.put("brand", device.getBrand());
			attrMap.put("version", device.getVersion());
			attrMap.put("resolution",device.getResolution());
			attrMap.put("url",device.getUrl());
			
			deviceInfoList.add(attrMap);
		}
		
		infoMap.put("deviceInfo",deviceInfoList);

		
		resultMap.put("code", 1);
	    resultMap.put("msg","加载设备信息成功");
		resultMap.put("data",infoMap);
		return resultMap;
		
		
	}
	
	@ResponseBody
	@RequestMapping("/loadHistory")
	public Map<String, Object> loadHistory(@RequestParam String userName) throws IOException{
		//log.info(String.format("==开始上传userName=%s serialStr=%s ", userName,serialStr));
		
		log.info("----loadHistory start---");
		ITaskService taskService=SpringContextHolder.getBean("taskService");
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> infoMap=new HashMap<String,Object>();
		

		infoMap.put("tasks",taskService.findTaskByUser(userName));
		map.put("data",infoMap);

       log.info("----loadHistory end---");
      return map;	
	}
	

	@ResponseBody
	@RequestMapping("/loadDetail")
	public Map<String, Object> loadDetail(@RequestParam String taskTag) throws IOException{
		//log.info(String.format("==开始上传userName=%s serialStr=%s ", userName,serialStr));
		
		log.info("----loadDetail start---");
		ISubTaskService subTaskService=SpringContextHolder.getBean("subTaskService");
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> infoMap=new HashMap<String,Object>();
	
		infoMap.put("subTasks",subTaskService.findByTaskTag(taskTag));
		map.put("data",infoMap);

       log.info("----loadDetail end---");
      return map;	
	}
	
	
	@ResponseBody
	@RequestMapping("/upload")
	public Map<String, Object> upload(@RequestParam("file") MultipartFile file,@RequestParam String userName,HttpServletRequest request) throws IOException{
		//log.info(String.format("==开始上传userName=%s serialStr=%s ", userName,serialStr));
		
		log.info("----上传脚本---");
		Map<String,Object> map=new HashMap<String,Object>();
		request.setCharacterEncoding("UTF-8");
		
// 		IAppiumTestService appiumTestService=SpringContextHolder.getBean("appiumTestService");
// 		appiumTestService.packageSave(file,userName);
// 		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
		commonService.scriptSave(file, userName);

		map.put("code", 1);
		map.put("msg","上传成功");
 	    log.info(String.format("--username %s--file %s", userName,file));


       log.info("----上传脚本结束---");
      return map;	
	}
	
	
	@ResponseBody
	@RequestMapping("/runTest")
	public Map<String, Object> runTest(@RequestParam String serialStr,@RequestParam String userName,@RequestParam String method){
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("code",0);
		map.put("msg", "测试任务提交失败");
		map.put("method",method);
		String[] serials=serialStr.split(";");
		
		if("appium".equals(method)){
			ICommonService commonService=SpringContextHolder.getBean("commonService");
			IAppiumTestService appiumTestService=SpringContextHolder.getBean("appiumTestService");
			IAppiumSyncService appiumSyncService=SpringContextHolder.getBean("appiumSyncService");
			//解压脚本
			//commonService.scriptUnpackage(userName, method);
			//初始化操作  生成serial目录保存生成的Driver.java	
			appiumTestService.init(userName, serialStr);
//			//搜寻用户最新上传产生的目录得到apk 将apk分发到远程provider目录
			appiumTestService.transferFile(serialStr, userName, map);
//			//编译Driver类&&用户上传的脚本
			appiumTestService.complie(userName,serials);
//			
//			//加入任务队列
//
			Map<String,String> scriptMap=appiumTestService.getLastClassesByUserSerial(userName);
//			
			File apk=appiumSyncService.getLastApk(userName);
			String apkPath=apk.getAbsolutePath();
//			
		    TestCase.addToTaskQueue(scriptMap,serials,userName,apkPath,method);
			
		}
		else if("robotium".equals(method)){
			
		}


		

		map.put("code",1);
		map.put("msg", "测试任务提交成功");
		return map;
	}
	

	@ResponseBody
	@RequestMapping("/projectionInit")
	public Map<String, Object> projectionInit(String serial,HttpServletRequest request,HttpServletResponse response){
		//log.info(String.format("==开始上传userName=%s serialStr=%s ", userName,serialStr));
		
		//response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("----projectionInit start---");
		log.info("serial="+serial);
		Map<String,Object> map=new HashMap<String,Object>();

		String webUrl="";
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
		webUrl=commonService.projectionInit(serial);

		map.put("code","1");
		map.put("url", webUrl);
       log.info("----projectionInit end---");
      return map;	
	}
	
	

	@ResponseBody
	@RequestMapping("/actionControl")
	public Map<String, Object> actionControl(String serial,String userName,String actionName,String args,String sw,String isRecording,String mode,String time,HttpServletResponse response){

	    response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("----actionControl start---");
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
		commonService.actionExecution(serial,userName,actionName, args,sw,Boolean.valueOf(isRecording),mode,time);
		
		Map<String,Object> map=new HashMap<String,Object>();
		

		map.put("code","1");
       log.info("----actionControl end---");
      return map;	
	}
	
	@ResponseBody
	@RequestMapping("/actionRecord")
	public Map<String, Object> record(String userName,String serial,String actionName,String args,HttpServletResponse response){

	    response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("----record start---");
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
		commonService.actionRecord(userName,serial, actionName, args);
		
		Map<String,Object> map=new HashMap<String,Object>();
		

		map.put("code","1");
       log.info("----record end---");
      return map;	
	}

	
	@ResponseBody
	@RequestMapping("/recordStop")
	public Map<String, Object> recordStop(String serial,String userName,String recordName,String tip,int method,String time,HttpServletResponse response){

	    response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("----record start---");
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
	    commonService.recordStop(userName,serial, recordName, tip,method,time);
		
		Map<String,Object> map=new HashMap<String,Object>();
		

		map.put("code","1");
       log.info("----record end---");
      return map;	
	}
	
	
	@ResponseBody
	@RequestMapping("/mGetRecordHistory")
	public Map<String, Object> getRecordHistory(String userName,HttpServletResponse response){

	    response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("----mGetRecordHistory start---");

		Map<String,Object> map=new HashMap<String,Object>();
		
		IRecordService recordService=SpringContextHolder.getBean("recordService");
		List<Record> records=recordService.mGetRecordHistory(userName);

		map.put("code","1");
		map.put("data",records);
		
       log.info("---mGetRecordHistory end---");
      return map;	
	}
	
	

	
	@ResponseBody
	@RequestMapping("/playback")
	public Map<String, Object> playback(String serialStr,String userName,String recordId,String sw,String time,HttpServletResponse response){

	    response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("----playback start---");
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
		
		String[] as=serialStr.split(";");
		String[] serials=new String[as.length-1];
		int j=0;
		for(int i=0;i<as.length;i++){
			if(i==0)continue;
			serials[j++]=as[i];
			
		}
		
		Map<String,Object> data=commonService.playback(serials,userName,recordId,sw,time);
		Map<String,Object> map=new HashMap<String,Object>();
        
		map.put("code","1");
		map.put("data",data);
		
       log.info("----playback end---");
      return map;	
	}


	
	
	//设备图片显示

	@RequestMapping("/showScreenshot")
	public void showScreenshot(String serial,HttpServletResponse response){

	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setContentType("image/jpeg");
		//log.info("----showScreenshot start---");
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
		byte[] data=commonService.showPage(serial);
//		
	     OutputStream stream;
	     


		try {
//	     File file =new File("E:\\auto-provider\\basic-data\\test.jpg");
//		     FileInputStream inputStream = new FileInputStream(file);
//		     byte[] data = new byte[(int)file.length()];
//		     int length = inputStream.read(data);
//		     inputStream.close();
		     
			stream = response.getOutputStream();
		    stream.write(data);
		    stream.flush();
		    stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
      	
	}
	
	//easy模式手动截屏
	@ResponseBody
	@RequestMapping("/screenshotByHand")
	public Map<String,String> screenshotByHand(String serial,String userName,boolean isPlayBack,boolean isRecording,String mode,String time,HttpServletResponse response){
	    response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("---- 触发Easy模式手动截屏---");
		
		ICommonService commonService=SpringContextHolder.getBean("commonService");
	    commonService.screenshotByHand(serial, userName, isPlayBack, isRecording, mode, time);
		
		Map<String, String> map=new HashMap<String,String>();
	
		map.put("code","1");
      
       return map;
		
		
	}
	
	
}
