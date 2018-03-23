/**
 * 
 */
package auto.web.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author BlackStone
 *
 */
public class UsedDevice {
	/** 
	 * @ClassName: DeviceMap 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年10月25日 上午11:19:14 
	 * 
	 */
	private static Map<String,String> map=new Hashtable<String,String>();
	
	public   synchronized static boolean isUsed(String serial){
		
		return map.get(serial)==null?false:true;
		
	}
	
	public synchronized static void addUsed(String serial){
		
		map.put(serial,"used");
	
	}
	
	public synchronized static void free(String serial){
		map.remove(serial);
		
	}
	
	public synchronized static Map<String,String> getUsed(){
		return map;
		
	}
	public synchronized static void  clear(){
		map.clear();
		
	}
	
}
