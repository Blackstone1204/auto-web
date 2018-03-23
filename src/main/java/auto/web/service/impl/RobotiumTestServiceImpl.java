/**
 * 
 */
package auto.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import auto.provider.model.FileUtil;
import auto.web.service.IRobotiumTestService;

/**
 * @author BlackStone
 *
 */
public class RobotiumTestServiceImpl implements IRobotiumTestService{

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#generateRJava()
	 */
	private static Logger log=Logger.getLogger(RobotiumTestServiceImpl.class);
	@Value("${user_data_dir}") private String userDataSave;
	@Value("${sdk_dir}") private String sdkDir;
	private String robotiumDataSave;

	@Override
	public void packageSave(MultipartFile file, String userName) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午3:40:20
			 */
		
		
		log.info(String.format("==开始脚本zip上传  %s", robotiumDataSave));
		this.robotiumDataSaveInit();
		
		try {
			
			File f=new File(robotiumDataSave);
			if(!f.exists())f.mkdir();
			f=new File(robotiumDataSave+File.separator+userName);
			if(!f.exists())f.mkdir();
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date now=new Date();
			String fileName=sdf.format(now)+".zip";
			
			f=new File(robotiumDataSave+File.separator+userName+File.separator+fileName);
			
			file.transferTo(f);
			log.info(String.format("==结束脚本zip上传 ", ""));
			log.info(String.format("==开始脚本解压 ",""));
			FileUtil.unZip(robotiumDataSave+File.separator+userName+File.separator+fileName);
			log.info(String.format("==结束脚本解压 ",""));
	
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void generateRJava(String userName) {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#compile()
	 */
	@Override
	public void compile() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#casePackage()
	 */
	@Override
	public void casePackage() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#signApk()
	 */
	@Override
	public void signApk() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#resignApk()
	 */
	@Override
	public void resignApk() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#installApk()
	 */
	@Override
	public void installApk() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}

	/* (non-Javadoc)
	 * @see auto.web.service.IRobotiumTestService#runTest()
	 */
	@Override
	public void runTest() {
			/**
			 * @Description:TODO
			 * @param 
			 * @author: BlackStone
			 * @time:2017年10月31日下午2:43:26
			 */
	}
	
	
	private void robotiumDataSaveInit(){
		robotiumDataSave=userDataSave+File.separator+"robotium";
		
		File dic=new File(robotiumDataSave);
		if(!dic.exists())dic.mkdir();
		
	}


	
	
}
