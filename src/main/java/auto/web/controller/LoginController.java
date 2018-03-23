/**
 * 
 */
package auto.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;















import auto.provider.model.User;
import auto.provider.service.IUserService;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author BlackStone
 *
 */
 
@Controller
public class LoginController {
	/** 
	 * @ClassName: UserController 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年8月30日 上午10:59:19 
	 * 
	 */
	
	private  final Logger log =Logger.getLogger(this.getClass().getName());
	


	@RequestMapping("/")
		public String index(){
			return "index";
			
		}
    @RequestMapping("/login")
	public String login(){
    	
		return "login";
		
	}
    

	@ResponseBody
	@RequestMapping(value="/auth")
	
	public Map<String,Object> auth(HttpServletRequest request,@RequestParam  String userName,@RequestParam String password) throws JsonProcessingException{
		IUserService userService=(IUserService) RequestContextUtils.getWebApplicationContext(request).getBean("userService"); 
		User user=userService.findUserByUserNamePassword(userName, password);
		log.info(userName+" "+password);
		
		Map<String,Object> map=new HashMap<String,Object>();
		
		if(null!=user){
		map.put("code",1);
			map.put("msg","登录成功");
			map.put("username",userName);

		}else{
			map.put("code",0);
		    map.put("msg","登录失败");
		
			
		}
		return map;
		

	}
	
	
	@RequestMapping(value="/main")
	public String main(){
		return "main";
		
	}


}
