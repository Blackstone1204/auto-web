/**
 * 
 */
package auto.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;

import auto.provider.model.Device;
import auto.provider.model.FileUtil;
import auto.provider.model.GlobalHandler;
import auto.provider.model.JedisUtil;
import auto.provider.model.Mode;
import auto.provider.model.SpringContextHolder;
import auto.provider.service.IAppiumSyncService;
import auto.provider.service.IDeviceService;
import auto.provider.service.IProjectionService;
import auto.provider.service.IRecordService;
import auto.web.service.ICommonService;
import auto.web.util.ImageUtil;

/**
 * @author BlackStone
 *
 */


public class CommonServiceImpl implements ICommonService{
	
	@Autowired JedisUtil ju;
	@Value("${user_data_dir}") private String userDataSave;
	private Logger log=Logger.getLogger(this.getClass().getName());
	public Device getSyncDevice(String serial){
		
		return null;
		//log.info(String.format("--获取运行设备实例%s ->%s", serial,device));
		
		
		
	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#globalHandlerSync()
	 */
	@Override
	public void globalHandlerSync() {
		
		//log.info("==全局句柄信息同步==");
		Set<String> keys=ju.getKeys("device_*");
		for(String key:keys){
			
			String serial=key.split("_")[1];
			GlobalHandler.getDevice(serial);
			//GlobalHandler.printDevice();

				
		}


	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#scriptSave(org.springframework.web.multipart.MultipartFile, java.lang.String)
	 */
	@Override
	public void scriptSave(MultipartFile file, String userName) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午5:48:42
			 */
		
		/**
		 * @Description:TODO
		 * @param 
		 * @author: BlackStone
		 * @time:2017年9月5日上午9:22:14
		 */
	
	log.info(String.format("==开始脚本保存 %s", userDataSave));
	try {
		
		File f=new File(userDataSave);
		if(!f.exists())f.mkdir();
		f=new File(userDataSave+File.separator+userName);
		if(!f.exists())f.mkdir();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date now=new Date();
		String fileName=sdf.format(now)+".zip";
		
		f=new File(userDataSave+File.separator+userName+File.separator+fileName);
		
		file.transferTo(f);
		log.info(String.format("==结束脚本zip保存 ", ""));
     	log.info(String.format("==开始脚本解压 ",""));
		FileUtil.unZip(userDataSave+File.separator+userName+File.separator+fileName);
		log.info(String.format("==结束脚本解压 ",""));

	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		
	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#scriptUnpackage()
	 */
	@Override
	public void scriptUnpackageDirCopyTo(String userName,String method,String fileName) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午5:48:42
			 */
		

		

	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#projectionInit(java.lang.String)
	 */
	@Override
	public String projectionInit(String serial) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年11月14日下午2:18:26
			 */
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IProjectionService> ref=new ReferenceConfig<IProjectionService>();
		ref.setInterface(IProjectionService.class);
		
		
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		String url=device.getNode()+"/"+"auto.provider.service.IProjectionService";
		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(false);
		mc.setTimeout(10000);
		mc.setName("projectionInit");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
		
		return ref.get().projectionInit(serial);

		

	}
	
	
	@Override
	public void actionExecution(String serial,String userName,String actionName,String args,String sw,boolean isRecording,String mode,String time) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年11月14日下午2:18:26
			 */
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IProjectionService> ref=new ReferenceConfig<IProjectionService>();
		ref.setInterface(IProjectionService.class);
		
		
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		String url=device.getNode()+"/"+"auto.provider.service.IProjectionService";
		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(true);
		mc.setName("actionExecution");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
		
		ref.get().actionExecution(serial,userName, actionName, args,sw,false,isRecording,new Mode(mode),time);

	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#playback(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String,Object> playback(String[] serials,String userName,String recordId,String sw,String time) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年12月1日下午6:07:19
			 */		
		
		//log.info("jjjfdaf");
		Map<String,Object> resultMap=new HashMap<String,Object>();
		for(String serial:serials){
			
			log.info("serial"+serial);
			ApplicationConfig ac=new ApplicationConfig();
			ac.setName("DubboProvider");
			ReferenceConfig<IProjectionService> ref=new ReferenceConfig<IProjectionService>();
			ref.setInterface(IProjectionService.class);
			
			
			IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
			Device device=deviceService.findDeviceBySerial(serial);
			String url=device.getNode()+"/"+"auto.provider.service.IProjectionService";
			ref.setUrl(url);
			ref.setApplication(ac);
			MethodConfig mc=new MethodConfig();
			mc.setAsync(false);
			mc.setName("playback");
			mc.setTimeout(500000);
	
			ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
			
		    Map<String,Object> map=ref.get().playback(serial,userName,recordId,sw,time);
		    resultMap.put(serial, map);
			
			
		}
		
		return resultMap;

				//log.info("testuuu");
	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#actionRecord(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void actionRecord(String userName,String serial, String actionName, String args) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年12月4日下午1:48:52
			 */
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IProjectionService> ref=new ReferenceConfig<IProjectionService>();
		ref.setInterface(IProjectionService.class);
		
		
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		String url=device.getNode()+"/"+"auto.provider.service.IProjectionService";
		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(true);
		mc.setName("actionRecord");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
		
		String command =actionName+args;
		ref.get().actionRecord(serial,userName, command);
		
		
		
	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#recordStop(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void recordStop(String userName,String serial,String recordName, String tip,int method,String time) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年12月4日下午2:30:09
			 */
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IProjectionService> ref=new ReferenceConfig<IProjectionService>();
		ref.setInterface(IProjectionService.class);
		
		
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		String url=device.getNode()+"/"+"auto.provider.service.IProjectionService";
		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(true);
		mc.setName("stopRecord");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
		
	
		ref.get().stopRecord(userName, serial,recordName, tip,method,time);

	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#showScreenshot(java.lang.String)
	 */
	@Override
	public byte[] showPage(String serial) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年12月5日下午2:23:07
			 */
		
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		
		String url=device.getNode()+"/"+"auto.provider.service.IDeviceService";
		
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IDeviceService> ref=new ReferenceConfig<IDeviceService>();
		ref.setInterface(IDeviceService.class);

		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(false);

		mc.setName("mGetScreenShot");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));

		
		return ref.get().mGetScreenShot(serial);
		
		
	}
	/* (non-Javadoc)
	 * @see auto.web.service.ICommonService#screenshotByHand(java.lang.String, java.lang.String, boolean, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public void screenshotByHand(String serial, String userName,
			boolean isPlayBack, boolean isRecording, String mode, String time) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2018年3月20日下午3:46:18
			 */
		
		
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IProjectionService> ref=new ReferenceConfig<IProjectionService>();
		ref.setInterface(IProjectionService.class);
		
		
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		String url=device.getNode()+"/"+"auto.provider.service.IProjectionService";
		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(true);
		mc.setName("screenshotByHand");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
	
		ref.get().screenshotByHand(serial, userName, isPlayBack, isRecording, new Mode(mode), time);
	

	}

	
	

}
