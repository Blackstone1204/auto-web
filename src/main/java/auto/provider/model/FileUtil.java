package auto.provider.model;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ArgumentConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.sun.jna.platform.FileUtils;

import auto.provider.service.IAppiumSyncService;
import auto.provider.service.IDeviceService;

/**
 * 
 */

/**
 * @author BlackStone
 *
 */
public class FileUtil {
	/** 
	 * @ClassName: FileUtil 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年9月5日 上午10:00:41 
	 * 
	 */
    private static final int buffer = 2048;  
    private static Logger log=Logger.getLogger(FileUtil.class);
    
    
    
  public static String  analyseResult(File dest,String str){

	  log.info(String.format("--文件 %s开始匹配 字符串 %s",dest.getAbsolutePath(),str));
	  BufferedReader bd=null;
	  try {
		  if(!dest.exists()){
			  log.info("结构分析文件不存在 !");
			  return "error_结果分析文件不存在";
		  }
		bd=new BufferedReader(new InputStreamReader(new FileInputStream(dest)));
		
		String line="";
		//String content="";
		while((line=bd.readLine())!=null){
			
			log.info("line:"+line);
			//content+=line;
	
			if(line.contains(str)){
				log.info(String.format("--文件 %s匹配到字符串 %s",dest.getAbsolutePath(),str));
				
				if(null!=bd)bd.close();
				return "pass";
			}
			
		}
		
		if(null!=bd)bd.close();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  return "fail";
	  
  }
    
  /** 
   * 解压Zip文件 
   * @param path 文件目录 
   */  

  public  static void unZip(String path)  
      {  
	  

       int count = -1;  
       String savepath = "";  

       File file = null;  
       InputStream is = null;  
       FileOutputStream fos = null;  
       BufferedOutputStream bos = null;  

       savepath = path.substring(0, path.lastIndexOf(".")) + File.separator; //保存解压文件目录  
       new File(savepath).mkdir(); //创建保存目录  
       ZipFile zipFile = null;  
       try  
       {  
           zipFile = new ZipFile(path); //解决中文乱码问题  
           Enumeration<?> entries = zipFile.entries();  

           while(entries.hasMoreElements())  
           {  
               byte buf[] = new byte[buffer];  

               ZipEntry entry = (ZipEntry)entries.nextElement();  

               String filename = entry.getName();  
               boolean ismkdir = false;  
               if(filename.lastIndexOf("/") != -1){ //检查此文件是否带有文件夹  
                  ismkdir = true;  
               }  
               filename = savepath + filename;  

               if(entry.isDirectory()){ //如果是文件夹先创建  
                  file = new File(filename);  
                  file.mkdirs();  
                   continue;  
               }  
               file = new File(filename);  
               if(!file.exists()){ //如果是目录先创建  
                  if(ismkdir){  
                  new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建  
                  }  
               }  
               file.createNewFile(); //创建文件  

               is = zipFile.getInputStream(entry);  
               fos = new FileOutputStream(file);  
               bos = new BufferedOutputStream(fos, buffer);  

               while((count = is.read(buf)) > -1)  
               {  
                   bos.write(buf, 0, count);  
               }  
               bos.flush();  
               bos.close();  
               fos.close();  

               is.close();  
           }  

           zipFile.close();  

       }catch(IOException ioe){  
           ioe.printStackTrace();  
       }finally{  
              try{  
              if(bos != null){  
                  bos.close();  
              }  
              if(fos != null) {  
                  fos.close();  
              }  
              if(is != null){  
                  is.close();  
              }  
              if(zipFile != null){  
                  zipFile.close();  
              }  
              }catch(Exception e) {  
                  e.printStackTrace();  
              } 
              
       }
       
      }
  
  public static String getLastUserDicPath(String userNamePath){
	  //log.info("--dicpath= "+dicPath);
	  File dic=new File(userNamePath);
	  File[] files=dic.listFiles();
	  String last="";
	  for(File f:files){
		 
		  //log.info(f.getName());
		  if(f.getName().contains("appium"))continue;
		  if(f.getName().contains("robotium"))continue;
		  if(f.isDirectory()){
			  if(last.compareTo(f.getName())<0)last=f.getName();

			  
		  }
	
	  }

	  return userNamePath+File.separator+last;
  }
  
  //usernName目录下获取最新上传zip位置
//  public static String getLastZipLoaction(String userNamePath){
//	  File dic=new File(userNamePath);
//	  File[] files=dic.listFiles();
//	  String last="";
//	  for(File f:files){
//		 
//		  //log.info(f.getName());
//		  if(f.getName().contains(".zip")){
//			  if(last.compareTo(f.getName())<0)last=f.getName();
//
//			  
//		  }
//	
//	  }
//
//	  return userNamePath+File.separator+last;
//	  
//  }
  public static String getLastName(String dicPath){
	  log.info("get last name");
	  File dic=new File(dicPath);
	  if(!dic.exists())log.info(String.format("文件不存在 %s",dicPath));
	  File[] files=dic.listFiles();
	  String last="";
	  for(File f:files){
		 
		  log.info(f.getName());
		  if(!f.getName().contains(".zip")){
			  if(last.compareTo(f.getName())<0)last=f.getName();
			  
			  
		  }
	
	  }

	  log.info("return "+last);
	  return last;
  }
 
	/**
	 * 获取用户上传的最新的apk文件(保存在web端的)
	 */
	public static File getLastApk(String apkLocParent){
		
		File apk=null;
		File dic=new File(apkLocParent);
		if(dic.isDirectory()){
			File[] files=dic.listFiles();
			for(File target:files){
				if(target.getName().contains(".apk")){
					apk=new File(apkLocParent+File.separator+target.getName());
					break;
				}
				
			}
			
		}
		return apk;
		
		
		

	}

public static byte[] getContent(String filePath) throws IOException {
		 
	    InputStream in = new FileInputStream(filePath);
	    byte[] data = toByteArray(in);
	    in.close();
	 
	    return data;
	}
	
private static byte[] toByteArray(InputStream in) throws IOException {
	 
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024 * 4];
	    int n = 0;
	    while ((n = in.read(buffer)) != -1) {
	        out.write(buffer, 0, n);
	    }
	    return out.toByteArray();
	}
  public static byte[] getContent2(String filePath) throws IOException {  
      File file = new File(filePath);  
      long fileSize = file.length();  
      if (fileSize > Integer.MAX_VALUE) {  
          System.out.println("file too big...");  
          return null;  
      }  
      FileInputStream fi = new FileInputStream(file);  
      byte[] buffer = new byte[(int) fileSize];  
      int offset = 0;  
      int numRead = 0;  
      while (offset < buffer.length  
      && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
          offset += numRead;  
      }  
      // 确保所有数据均被读取  
      if (offset != buffer.length) {  
      throw new IOException("Could not completely read file "  
                  + file.getName());  
      }  
      fi.close();  
      return buffer;  
  } 
  
  
  public static void generateDriverFiles(String userDataSave,String sdkDir,String userName,String[] serials) throws Exception{
		//目录结构  /user-data/971406187/appium/2017-10-1201/serial/test/script/Driver.java
	  log.info("--开始生成Driver.java--");
	  //生成serial/test/script/Driver.java文件
		File f=new File(userDataSave);
		if(!f.exists())f.mkdir();
		f=new File(userDataSave+File.separator+userName);
		if(!f.exists())f.mkdir();
		//log.info("f00");
		f=new File(userDataSave+File.separator+userName+File.separator+"appium");
		if(!f.exists())f.mkdir();

		//拷贝用户最新的解压后的脚本到 appium目录下
		
		String unzipDic=FileUtil.getLastUserDicPath(userDataSave+File.separator+userName);
		String name=new File(unzipDic).getName();
	    String destDic=userDataSave+File.separator+userName+File.separator+"appium"+File.separator+name;
	    
	    log.info(String.format("copy directory %s %s", unzipDic,destDic));
		FileUtil.copyDirectory(unzipDic, destDic);
//		//String zipNameLoc=FileUtil.getLastZipLoaction(userDataSave+File.separator+userName);
//		File srcFile=new File(zipNameLoc);
//		String zipName=new File(zipNameLoc).getName();
//		File dstFile=new File(userDataSave+File.separator+userName+File.separator+"appium"+File.separator+zipName);
//		FileUtil.copyFile(srcFile,dstFile);
//		
//		FileUtil.unZip(userDataSave+File.separator+userName+File.separator+"appium"+File.separator+zipName);

		//
		
		String lastDateParent=userDataSave+File.separator+userName+File.separator+"appium";
		String lastDicPath=FileUtil.getLastUserDicPath(lastDateParent);
		File apk=FileUtil.getLastApk(lastDicPath);
		String apkPath=apk.getAbsolutePath();

		for(String serial:serials){
			if(serial.equals(""))continue;
			f=new File(getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium")+File.separator+serial);
			if(!f.exists())f.mkdir();
			f=new File(getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium")+File.separator+serial+File.separator+"test");
			if(!f.exists())f.mkdir();
			f=new File(getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium")+File.separator+serial+File.separator+"test"+File.separator+"script");
			if(!f.exists())f.mkdir();
			f=new File(getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium")+File.separator+serial+File.separator+"test"+File.separator+"script"+File.separator+"Driver.java");
			if(!f.exists())f.createNewFile();
		}
		
		
	  //获取模板内容
	  InputStream is=null;
	  BufferedReader bd=null;

	  
	  
	  //替换模板内容写入 Driver.java
	  
	  Map<String,String> map=apkAnalysis(apkPath,sdkDir);
	  String activityName=map.get("activityName");
	  String packageName=map.get("packageName");

      IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
	  for(String serial:serials){
		  if(serial.equals(""))continue;
		  Device device=deviceService.findDeviceBySerial(serial);
		  if(null==device){
			  log.info("--设备没连接 或者表中数据没同步 serial="+serial);
			  return;
		  }
		  
		  //
		  is=FileUtil.class.getResourceAsStream("/Driver.mb");
		  bd=new BufferedReader(new InputStreamReader(is));
		  String content="";
		  String line="";
		  while((line=bd.readLine())!=null){
			  content+=line+"\n";
		  }
		  
		  log.info("--获取的模板内容-+"+content);
		  

		  //
		  
		  String port=String.valueOf(device.getPort());
		  String ip="";
		  String node=deviceService.findDeviceBySerial(serial).getNode();
		  Pattern p=Pattern.compile("dubbo://(.*?):");
		  Matcher m=p.matcher(node);
		  while(m.find()){
			  ip=m.group(1);
		  }
		  log.info(String.format("ip %s port %s",ip,port));
		  String remoteApkPath=getRemoteApkPath(userName,serial).replaceAll("\\\\","\\\\\\\\");
			
		  log.info(String.format("--serial=%s removeApkPath=%s startAcvity=%s packageName=%s ip=%s port=%s",serial,remoteApkPath,activityName,packageName,ip,port));
		  boolean flag=content.contains("#ip#");
		  log.info("flag="+flag);
		  content=content.replace("#startActivity#", activityName)
				  .replace("#packageName#", packageName)
		          .replace("#ip#", ip)
		          .replace("#port#",port)
		          .replace("#apkPath#",remoteApkPath);
		  
		  log.info("--模板替换后内容--\n"+content);
		  
		  File cpFile=new File(getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium")+File.separator+serial+File.separator+"test"+File.separator+"script"+File.separator+"Driver.java");
		  
		  PrintWriter pw=new PrintWriter(cpFile);
		  pw.write(content);
		  pw.flush();
		  pw.close();
		  
		  
	  }
	  
		log.info("--Driver.java生成结束--");
	  
	  
	  
  
	  
  }

  //返回用户最新的脚本文件.class和所在serial目录的名称
  public static Map<String,String> getScriptsByUserSerial(String userDataSave,String userName){
	  //List<String>scriptPaths=new ArrayList<String>();
	  Map<String,String> scriptMap=new HashMap<String,String>();
	  String path=getLastUserDicPath(userDataSave+File.separator+userName+File.separator+"appium");
	  File userLastDir=new File(path);
	  if(userLastDir.exists()){
		  File[] files=userLastDir.listFiles();
		  for(File serialDir:files){
			  if(serialDir.isDirectory()){
				 
				
				  File[] runFileDir=new File(serialDir.getAbsolutePath()+File.separator+"test"+File.separator+"script").listFiles();
				  for(File runFile:runFileDir){
					  if(runFile.getName().endsWith(".class")&&!runFile.getName().contains("Driver")){
						  int len=runFile.getAbsolutePath().length();
						  String p=runFile.getAbsolutePath();
						  //scriptPaths.add(p.substring(0,len-6));
						  scriptMap.put(p.substring(0,len-6),serialDir.getName());
					  }
						 
					  
				  }
		
				  
			  }
			  
			  
		  }

		  
		  
	  }
	  
	  return scriptMap;
	  
  }
  
  public static Map<String,String> apkAnalysis(String apkPath,String sdkPath) throws IOException, InterruptedException{

	  log.info("--开始解析apk--");
	  Map<String,String> map=new HashMap<String,String>();
	  String activityName="";
	  String packageName="";
	  
	  File sdkDic=new File(sdkPath);
	  String aaptPath="";
	  if(!sdkDic.exists())
		  log.info(String.format("--%s不存在", sdkPath));
	  
	  File buildTools=new File(sdkPath+File.separator+"build-tools");
	  if(!buildTools.exists())
		  log.info(String.format("--%s不存在", buildTools));
	  
	  if(buildTools.listFiles().length>0){
		  
		  aaptPath=buildTools.getAbsolutePath()+File.separator+buildTools.listFiles()[0].getName()+File.separator+"aapt.exe";
	  }
	  
	  log.info(String.format("--apkPath %s", apkPath));
	  log.info(String.format("--aaptPath %s", aaptPath));

	  String apkAnalysisCommand=String.format("%s dump badging %s", aaptPath,apkPath);
	  
	  log.info(String.format("--apk解析命令  %s",apkAnalysisCommand ));
	  
	  String str=AdbConnector.run(apkAnalysisCommand);

      String regex1="launchable-activity: name='(.*?)'  label";
      String regex2="package: name='(.*?)' versionCode";
	
	  Pattern p1=Pattern.compile(regex1);
	  Pattern p2=Pattern.compile(regex2);
	  Matcher m1=p1.matcher(str);
	  Matcher m2=p2.matcher(str);
	  
		
	  while(m1.find()){
		  activityName=m1.group(1);
		 
		}
	  
		
	  while(m2.find()){
		  packageName=m2.group(1);
		 
		}
	  
	  log.info(String.format("--apk解析结束  packageName=%s activityName=%s--", packageName,activityName));
	  
	  map.put("packageName", packageName);
	  map.put("activityName",activityName);
	  return map;
	  
	  
	  
  }
  //获取上传脚本所有路径 
  public static List<String> getUploadJavaPaths(String uploadJavaPath){
	  log.info("==获取上传脚本路径==");
	  List<String> paths=new ArrayList<String>();
	  File uploadJavaDir=new File(uploadJavaPath);
	  
	  for(File javaFile:uploadJavaDir.listFiles()){
		  if(javaFile.getName().endsWith(".java")){
			  paths.add(javaFile.getAbsolutePath());
			  log.info("--path="+javaFile.getAbsolutePath());
		  }

		  
	  }
	  
	  return paths;

	  
  }
  
  public static void copyDirectory(String srcDic,String destDic){
	  File dst=new File(destDic);
	  if(!dst.exists())dst.mkdir();
	  
	  File srcD=new File(srcDic);
	  
	  File[] files=srcD.listFiles();
	  for(File file:files){
		  File srcFile=file;
		  String name=srcFile.getName();
		  File dstFile=new File(destDic+File.separator+name);
		  try {
			FileUtil.copyFile(srcFile,dstFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  }
	  
	  
  }
  public static void copyFile(File f1,File f2) throws Exception{
	  log.info("==[开始拷贝文件]");
	  
	  //用字节流形式进行拷贝
	  String srcPath=f1.getAbsolutePath();
	  byte[] data=FileUtil.getContent(srcPath);
	  
	  FileOutputStream fos=new FileOutputStream(f2);
	  
	  fos.write(data);
	  fos.flush();
	  fos.close();
	  
//	  BufferedReader bd=null;
//	  PrintWriter pw=new PrintWriter(f2);
//	  
//	  try{
//		  bd=new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
//		  String content="";
//		  String line="";
//		  while((line=bd.readLine())!=null){
//			  content+=line+"\n";
//		  }
//		  
//		 // log.info("--content="+content);
//		  
//		  pw.write(content);
//		  
//	  }catch(Exception ex){
//		  ex.printStackTrace();
//	  }finally{
//		  if(bd!=null)bd.close();
//		  if(pw!=null)pw.close();
//		  log.info("==[拷贝结束]");
//	  }
	  
  }
  //拷贝文件 返回所需时间
  public  static void  copyJavaFileToSerialDir(File f1,File f2) throws Exception{
	  log.info("==[开始拷贝文件]");
	  BufferedReader bd=null;
	  PrintWriter pw=new PrintWriter(f2);
	  
	  try{
		  bd=new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
		  String content="";
		  String line="";
		  while((line=bd.readLine())!=null){
			  content+=line+"\n";
		  }
		  
		 // log.info("--content="+content);
		  
		  pw.write(content);
		  
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }finally{
		  if(bd!=null)bd.close();
		  if(pw!=null)pw.close();
		  log.info("==[拷贝结束]");
	  }
	  
	 

  }
  
  private static String getRemoteApkPath(String userName,String serial){
		IDeviceService deviceService=SpringContextHolder.getBean("deviceService");
		Device device=deviceService.findDeviceBySerial(serial);
		String url=device.getNode()+"/"+"auto.provider.service.IAppiumSyncService";
		ApplicationConfig ac=new ApplicationConfig();
		ac.setName("DubboProvider");
		ReferenceConfig<IAppiumSyncService> ref=new ReferenceConfig<IAppiumSyncService>();
		ref.setInterface(IAppiumSyncService.class);
		ref.setUrl(url);
		ref.setApplication(ac);
		MethodConfig mc=new MethodConfig();
		mc.setAsync(false);
		mc.setName("getLastApk");
		ref.setMethods(Arrays.asList(new MethodConfig[]{mc}));
		String remotePath=ref.get().getLastApk(userName).getAbsolutePath();

		return remotePath;
	  
  }
  
  
  class CopyTask implements Runnable{
	  File src;
	  File dest;
	  public CopyTask(File src,File dest){
		  this.src=src;
		  this.dest=dest;
	  }
	  public void run(){
		  
		  try{
			  long time=new Date().getTime();
		      int length=2097152;
		      FileInputStream in=new FileInputStream(this.src);
		      FileOutputStream out=new FileOutputStream(this.dest);
		      FileChannel inC=in.getChannel();
		      FileChannel outC=out.getChannel();
		      ByteBuffer b=null;
		      while(true){
		          if(inC.position()==inC.size()){
		              inC.close();
		              outC.close();
		              
		          }
		          if((inC.size()-inC.position())<length){
		              length=(int)(inC.size()-inC.position());
		          }else
		              length=2097152;
		          b=ByteBuffer.allocateDirect(length);
		          inC.read(b);
		          b.flip();
		          outC.write(b);
		          outC.force(false);
		      }
			  
		  }catch(Exception ex){
			  ex.printStackTrace();
		  }
	

	  }
  }
  
 
      
          
}
