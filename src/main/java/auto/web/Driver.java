package auto.web;
/**
 * 
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author BlackStone
 *
 */
@ComponentScan(value = {"auto.web.controller"})  
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ImportResource("classpath:*.xml")
public class Driver extends SpringBootServletInitializer{
	/** 
	 * @ClassName: Driver 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @date 2017年9月6日 下午2:08:17 
	 * 
	 */
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Driver.class, args);
    }
}
