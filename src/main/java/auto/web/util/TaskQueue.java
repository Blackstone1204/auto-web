/**
 * 
 */
package auto.web.util;

import java.util.Iterator;  
import java.util.LinkedList;  
import java.util.List;  

import org.apache.log4j.Logger;

import auto.provider.model.SpringContextHolder;
import auto.provider.model.SubTask;
import auto.provider.service.ISubTaskService;
import auto.provider.service.ITaskService;
public class TaskQueue {  
	private static Logger log=Logger.getLogger(TaskQueue.class);
    private static List<SubTask> queue = new LinkedList<SubTask>();  // 添加一项任务 
    

    public static synchronized void addSubTask(SubTask task) {  
    	ISubTaskService subTaskService=SpringContextHolder.getBean("subTaskService");
        if (task != null) { 
        	
        	task.setResult("waiting");
        	subTaskService.submit(task);
        	
            queue.add(task);  
            
            }  
        }  // 完成任务后将它从任务队列中删除  
     
    
    public static synchronized void finishSubTask(String subTaskTag,String result) {
		ISubTaskService subTaskService=SpringContextHolder.getBean("subTaskService");
        Iterator<SubTask> it = queue.iterator();
        SubTask subTask; 
        while (it.hasNext()){
        	  subTask = it.next();
        	  if(subTask.getSubTag().equals(subTaskTag)){
        		  
        		  subTask.setResult(result);
        		  subTaskService.setResult(subTask);
        		  
        		  queue.remove(subTask);
        		  
        		  UsedDevice.free(subTask.getSerial());
        		  
        	  }
	
        }
       
        }  // 取得一项待执行任务  
     

    public static synchronized SubTask getSubTask() {
		ISubTaskService subTaskService=SpringContextHolder.getBean("subTaskService");
        Iterator<SubTask> it = queue.iterator(); 
        SubTask subTask; 
        while (it.hasNext()) {  
        	
            subTask = it.next();
            if(subTask.getResult().equals("waiting")&UsedDevice.isUsed(subTask.getSerial())==false){
            	
            	subTask.setResult("running");
            	subTaskService.setResult(subTask);
            	
            	UsedDevice.addUsed(subTask.getSerial());
            	return subTask;
            }
            
            }  
        log.info("没拿到任务");
        return null;  
    }  
    
    public static int getSize(){
    	int size=queue.size();
    	log.info("任务队列大小 "+size);
    	return size;
    }
    
    public static  List<SubTask> getTaskList(){
    	return queue;
    	
    }
    public static void clear(){
    	queue.clear();
    }
} 