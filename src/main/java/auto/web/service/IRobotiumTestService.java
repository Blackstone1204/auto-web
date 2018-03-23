/**
 * 
 */
package auto.web.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author BlackStone
 *
 */
public interface IRobotiumTestService {
	public void packageSave(MultipartFile file,String userName);
	//生成 R.java
	public void generateRJava(String userName);
	//编译.java文件
	public void compile();
    //apk打包
	public void casePackage();
	//签名apk打包
	public void signApk();
    //重签名apk
	public void resignApk();
	//安装apk
	public void installApk();
	//运行robotium测试
	public void runTest();

}
