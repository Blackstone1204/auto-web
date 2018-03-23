/**
 * 
 */
package auto.provider.model;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author BlackStone
 *
 */
public class GlobalHandler {
	/** 
	 * @ClassName: GlobalHandler 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年9月22日 下午1:02:01 
	 * 
	 */
	
	private static final Logger log=Logger.getLogger(GlobalHandler.class);
	public static Map<String,Device> deviceHandlers=new Hashtable<String,Device>();
	public static Map<String,Process> processHandlers=new Hashtable<String,Process>();
	
	
	public synchronized static Device getDevice(String serial){
		
		Device device=deviceHandlers.get(serial);
		if(null!=device)return device;
		
		Device d=new Device();
		addDevice(serial,d);
		return d;
		


	}
	public static void delDevice(String serial){
		deviceHandlers.remove(serial);
	}
	
	public static void addDevice(String serial,Device d){
		
		deviceHandlers.put(serial,d);
		log.info(String.format("-deviceHandler新增设备实例 %s ->%s -", serial,d));
	}
	
	public static void printDevice(){
		log.info("--当前deviceHandler对应关系--");
		Set<String> keys=deviceHandlers.keySet();
		for(String key:keys){
			log.info(String.format("--%s ->%s", key,deviceHandlers.get(key)));
		}
		
	}
	
}
