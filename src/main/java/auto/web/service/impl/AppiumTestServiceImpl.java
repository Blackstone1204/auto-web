/**
 * 
 */
package auto.web.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;

import auto.provider.model.AdbConnector;
import auto.provider.model.Device;
import auto.provider.model.FileUtil;
import auto.provider.model.SendApkTask;
import auto.provider.model.SpringContextHolder;
import auto.provider.model.SubTask;
import auto.provider.service.IAppiumAsynService;
import auto.provider.service.IAppiumSyncService;
import auto.provider.service.IDeviceService;
import auto.provider.service.ISubTaskService;
import auto.web.service.IAppiumTestService;
import auto.web.util.TaskQueue;

/**
 * @author BlackStone
 *
 */
public class AppiumTestServiceImpl implements IAppiumTestService{

	/* (non-Javadoc)
	 * @see auto.web.service.IAppiumTestService#packageSaveTo()
	 */

	private static Logger log=Logger.getLogger(AppiumTestServiceImpl.class);
	
	@Value("${user_data_dir}") private String userDataSave;
	@Value("${classes_dir}") private String classesDir;
	@Value("${jar_dir}") private String jarDir;
	@Value("${sdk_dir}") private String sdkDir;
	
	private String appiumDataSave="";
	
	
	private void appiumDataSaveInit(){
		
		appiumDataSave=userDataSave+File.separator+"appium";
		
	}

	/**
	 * 保存用户上传的zip脚本文件
	 */
	public void packageSave(MultipartFile file,String userName) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年9月5日上午9:22:14
			 */
		
		log.info(String.format("==开始脚本zip上传 %s", userDataSave));
		
		
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
			log.info(String.format("==结束脚本zip上传 ", ""));
//			log.info(String.format("==开始脚本解压 ",""));
//			FileUtil.unZip(userDataSave+File.separator+userName+File.separator+fileName);
//			log.info(String.format("==结束脚本解压 ",""));
	
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<String,String> getLastClassesByUserSerial(String userName){
		
		
		return FileUtil.getScriptsByUserSerial(userDataSave, userName);
		
	}
	
	public List<String> getLastJavaPath(String userName){
		
		
		String dicPath=FileUtil.getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium");
		List<String> paths=new ArrayList<String>();  
		File f=new File(dicPath);
		
		for(File ff:f.listFiles()){
			if(ff.isDirectory()){
				File serialDir=new File(ff.getAbsolutePath());

					File javaDir=new File(serialDir.getAbsolutePath()+File.separator+"test"+File.separator+"script");
					for(File javaFile:javaDir.listFiles()){
					
						if(javaFile.getName().endsWith(".java")&&!javaFile.getName().contains("Driver")){
							log.info(String.format("--javaFile path =%s", javaFile.getAbsolutePath()));
							paths.add(javaFile.getAbsolutePath());
							
						}
				
						
					}
	

				
			}

		}
		return paths;
		
	}
	/**
	 * 获取用户上传的最新的apk文件(保存在web端的)
	 */
	public File getLastApk(String userName){
		
		
		
		String lastName=FileUtil.getLastName(userDataSave+File.separator+userName+File.separator+"appium");
		
		log.info("lastname="+lastName);
		
		String searchUrl=userDataSave+File.separator+userName+File.separator+"appium"+File.separator+lastName;
		
		log.info("searchUlr="+searchUrl);
		File file=new File(searchUrl);
		File[] files=file.listFiles();
		
		String apkName="";
		for(File f:files){
			if(f.getName().endsWith(".apk")){
				apkName=f.getName();
				break;
			}
			
		}
		
		File apk=new File(userDataSave+File.separator+userName+File.separator+"appium"+File.separator+lastName+File.separator+apkName);
		
		return apk;
	}

	
	public void init(String userName,String serialStr){

		String[] serials=serialStr.split(";");
		//String apkPath=this.getLastApk(userName).getAbsolutePath();
		try {
			FileUtil.generateDriverFiles(userDataSave,sdkDir,userName,serials);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/* (non-Javadoc)
	 * @see auto.web.service.IAppiumTestService#complie()
	 */
	

	public void transferFile(String  serialStr,String userName,Map<String,Object>map){
		log.info("==开始发送远端apk==");

		File apk=this.getLastApk(userName);
		String apkWebPath=apk.getAbsolutePath();
		log.info("--web端apk path="+apkWebPath);
		
		String[] serials=serialStr.split(";");
		
		for(String ss:serials){
			if(ss.length()==0)continue;
		
			IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
			Device device=deviceService.findDeviceBySerial(ss);
			String url=device.getNode()+"/"+"auto.provider.service.IAppiumAsynService";
			log.info("url="+url);
			log.info("--map---="+map);
			if(null!=map.get(url))continue;
			
			ApplicationConfig ac=new ApplicationConfig();
			ac.setName("DubboProvider");
			ReferenceConfig<IAppiumSyncService> ref=new ReferenceConfig<IAppiumSyncService>();
			ref.setInterface(IAppiumSyncService.class);
			ref.setUrl(url);
			ref.setApplication(ac);
			MethodConfig mc=new MethodConfig();
			mc.setAsync(false);
			mc.setName("apkSave");
			ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
			InputStream fin=null;
			try {

				if(null!=apk){
					//fin=new FileInputStream(apk);
					byte[] bytes=FileUtil.getContent(apkWebPath);
					
					log.info("byte send size="+bytes.length);
					log.info("byte send content "+new String(bytes));
			
					new Thread(new SendApkTask(ref,bytes,userName)).start();
					map.put(url,true);

				}
				else log.error("==没有获取到apk");
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}	
		
		log.info("==发送远端apk结束==");
		
	}
	public void complie(String userName,String[] serials) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年9月5日上午9:22:14
			 */
		log.info("==开始编译脚本==");
	
		String osName=System.getProperty("os.name");
		log.info(String.format("-操作系统 %s",osName));
		log.info(String.format("-jarDir %s", jarDir));
		log.info(String.format("-classesDir %s", classesDir));
		String split=";";
		if(osName.startsWith("win")||osName.startsWith("Win"))split=";";
		else split=":";
		
		String denpendJarStr="";
		File jarDic=new File(jarDir);
		File classDic=new File(classesDir);
		
		if(!jarDic.exists()){
			log.info("--依赖jar包目录不存在");
			return;
		}
		if(!classDic.exists()){
			log.info("--classes目录不存在");
			return;
		}
		

		for(File f:jarDic.listFiles()){
			denpendJarStr+=f.getAbsolutePath()+split;
			
		}

		String userLastDirPath=FileUtil.getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium");
		//编译Driver.java
		for(String serial:serials){
			if(serial.equals(""))continue;
			String javacx=String.format("javac -encoding utf-8 -cp %s%s%s %s",classesDir,split,denpendJarStr,userLastDirPath+File.separator+serial+File.separator+"test"+File.separator+"script"+File.separator+"Driver.java");
			log.info(String.format("==编译Driver.java %s", javacx));
			Process  process=null;
			try {
				process=Runtime.getRuntime().exec(javacx);
				String path=FileUtil.getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium");
				String serialDirPath=path+File.separator+serial;
				
				//generateAnalyseFile(process,serialDirPath,".compile");
				//AdbConnector.run(javacx);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//编译上传脚本
		  //先拷贝所有的java脚本到serial目录 考虑包名 test.script
		
		List<String> uploadJavaPaths=FileUtil.getUploadJavaPaths(userLastDirPath);
		int size=uploadJavaPaths.size();
		log.info(String.format("==上传脚本个数  %s", size));
		for(String javaPath:uploadJavaPaths){
			for(String serial:serials){
				
				if(serial.equals(""))continue;
				File srcFile=new File(javaPath);
				String srcFileName=srcFile.getName();
				File destFile=new File(userLastDirPath+File.separator+serial+File.separator+"test"+File.separator+"script"+File.separator+srcFileName);
				
				try {
					FileUtil.copyJavaFileToSerialDir(srcFile, destFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		//
		List<String> scriptPaths=this.getLastJavaPath(userName);
		log.info(String.format("--用户上传脚本个数  =%s", scriptPaths.size()));
		
		for(String scriptPath:scriptPaths){
			File javaFile =new File(scriptPath);
			String driverClassPath=javaFile.getParentFile().getParentFile().getParent();
			String javac=String.format("javac -encoding utf-8 -cp %s%s%s%s%s %s",classesDir,split,driverClassPath,split,denpendJarStr,scriptPath);
			log.info(String.format("==编译上传脚本 %s", javac));



			try {
				AdbConnector.run(javac);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		
		log.info("--编译操作结束---");
		

		
	}




	/* (non-Javadoc)
	 * @see auto.web.service.IAppiumTestService#runTest(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void runTest(String taskTag,String subTaskTag,String userName,String scriptPath, String serial) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年9月16日上午11:25:52
			 */
		log.info("==[开始测试过程]==");

		IAppiumTestService appiumTestService=SpringContextHolder.getBean("appiumTestService");
		IAppiumSyncService appiumSyncService=SpringContextHolder.getBean("appiumSyncService");
		IAppiumAsynService appiumAsynService=SpringContextHolder.getBean("appiumAsynService");

		//解压脚本

		//远端设备端开启appiumserver
		appiumAsynService.startServer(serial);
		//本地执行junit测试
		String result=appiumTestService.executeClass(subTaskTag,userName, serial,scriptPath);
		//更新结果
	
		//测试完成关闭appiumserver
		appiumAsynService.stopServer(serial);
		//任务队列中移除任务释放设备
		TaskQueue.finishSubTask(subTaskTag, result);
		//远端设备卸载apk
		log.info("==[结束测试过程]==");
	}



	/* (non-Javadoc)
	 * @see auto.web.service.IAppiumTestService#executeClass(java.lang.String, java.lang.String)
	 */
	public String executeClass(String subTaskTag,String userName, String serial,String classFullPath) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年9月19日上午9:57:55
			 */
		
		log.info(String.format("--开始执行junit测试",""));
		
		String result="未定义";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String osName=System.getProperty("os.name");
		log.info(String.format("-操作系统 %s",osName));
		log.info(String.format("-jarDir %s", jarDir));
		log.info(String.format("-classesDir %s", classesDir));
		String split=";";
		if(osName.startsWith("win")||osName.startsWith("Win"))split=";";
		else split=":";
		
		String denpendJarStr="";
		File jarDic=new File(jarDir);
		File classDic=new File(classesDir);
		log.info("f1");
		if(!jarDic.exists()){
			log.info("--依赖jar包目录不存在");
			return "";
		}
		log.info("f2");
		if(!classDic.exists()){
			log.info("--classes目录不存在");
			return "";
		}
		log.info("f3");

		for(File f:jarDic.listFiles()){
			denpendJarStr+=f.getAbsolutePath()+split;
			
		}

		log.info("f4");
		File runClassFile=new File(classFullPath);
		if(!runClassFile.exists())log.info(classFullPath+"路径不存在");
		String runClasShortName=runClassFile.getName().substring(0,runClassFile.getName().length()-6);
		String runClassFullName=String.format("test.script.%s",runClasShortName);
        String driverClassPath=runClassFile.getParentFile().getParentFile().getParent();
        
	    String java=String.format("java -cp %s%s%s%s%s org.junit.runner.JUnitCore %s",classesDir,split,driverClassPath,split,denpendJarStr,runClassFullName);
	    log.info(String.format("--junit命令--%s", java));
	    Process process=null;
	    try {
			process=Runtime.getRuntime().exec(java);
			
			//生成一个run.rst用于后面结果分析
		
			String path=FileUtil.getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium");
			String serialDirPath=path+File.separator+serial;
			generateAnalyseFile(subTaskTag,process, serialDirPath,".run");
			
			//
			process.waitFor();
			process.destroy();
			//
			Thread.sleep(200);
			//延时5s进行 结果分析
			String dataPath=FileUtil.getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium");
			String resutPath=dataPath+File.separator+serial+File.separator+subTaskTag+".run";
			File resultFile=new File(resutPath);
			log.info("--准备分析的结果文件  "+resultFile.getAbsolutePath());
			result=FileUtil.analyseResult(resultFile, "OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return result;

	
	}
	
	/* (non-Javadoc)
	 * @see auto.web.service.IAppiumTestService#gather()
	 */
	public void gather() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年9月5日上午9:22:14
			 */
	}
	
	//生成分析文件 compile /run
	private void generateAnalyseFile(String subTaskTag,final Process p,final String parentPath,String fileNamePattern){
		log.info("--开始生成结果分析文件--");
		
		final String rightFilePath=String.format(parentPath+File.separator+"%s%s",subTaskTag,fileNamePattern);
		log.info("--生成位置 "+rightFilePath);
		final String errorFilePath=String.format(parentPath+File.separator+"%s%s.err",subTaskTag,fileNamePattern);
		//输出run.err.rst
		new Thread(){
			public void run(){
				String rs="";	
				try {
				BufferedReader bd=new BufferedReader(new InputStreamReader(p.getErrorStream(),"utf-8"));
				File runErr=(new File(errorFilePath));
				if(!runErr.exists())runErr.createNewFile();
				PrintWriter pw=new PrintWriter(runErr);
				String line="";
				
					while((line=bd.readLine())!=null)
					  rs+=line+"\n<br/>";
					
					bd.close();
										
					if(rs!=""){
						//log.error(rs);
						pw.write(rs);
						pw.flush();
						if(null!=pw)pw.close();
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					
				}
	
			
				
			}
		}.start();
		
		//输出run.rst
		
		new Thread(){
			public void run(){
				String rs="";
				try {	
				BufferedReader bd=new BufferedReader(new InputStreamReader(p.getInputStream(),"utf-8"));
				String line="";
				File run=(new File(rightFilePath));
				if(!run.exists())run.createNewFile();
				PrintWriter pw=new PrintWriter(run);
				
			
					while((line=bd.readLine())!=null)
					  rs+=line+"\n<br/>";
					
					bd.close();
					
					if(rs.length()>0){
						pw.write(rs);
						pw.flush();
						
						if(pw!=null)pw.close();
					}
					//log.info(rs);
				   
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				
				}
	
				
				
			}
		}.start();
		
	}
	
	
	//
	

	public String getLastUserDicPath(String userName){
		
		
		return FileUtil.getLastUserDicPath(userDataSave+File.separator+userName);
		
	}
	
	public String getTimeStr(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date now=new Date();
		String timeStr=sdf.format(now);
		return timeStr;
	}







}
