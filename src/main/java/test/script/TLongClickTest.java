package test.script;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
   
public class TLongClickTest{  
    private static Logger log=Logger.getLogger(TLongClickTest.class);
    private AppiumDriver<WebElement> driver;    

    @Before  
    public void setUp(){  
        // set up appium 
       	log.info("--set up--");
        driver =DriverT.getDriver();
     
        log.info(String.format("[*]driver=%s",driver.toString()));
        
    }  
   
    @After  
    public void tearDown() throws Exception { 
       	log.info("--tearDown--");
        if(null!=driver)driver.quit();  
    }  
   
    @Test  
    public void test(){ 
    	log.info("--test--");
    	WebElement longClickButton=driver.findElementById("me.demo.demotest:id/longclick");
    	new TouchAction(driver).longPress(longClickButton).waitAction(1000).perform();
    }
    
}