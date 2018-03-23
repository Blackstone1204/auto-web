package test.script;


import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
public class DriverT {

	private static DesiredCapabilities getCapabilities(){
		DesiredCapabilities cp=new DesiredCapabilities();

		cp.setCapability("platformName", "android");
		cp.setCapability("deviceName", "testDevice");
		cp.setCapability("platformVersion", "4.4");


		cp.setCapability("appPackage", "me.demo.demotest");
		cp.setCapability("appActivity","me.demo.demotest.MainActivity");

		cp.setCapability("unicodeKeyboard", "True"); //
		// Chinese
		cp.setCapability("resetKeyboard", "True");
		cp.setCapability("newCommandTimeout", "240");
		//cp.setCapability("noReset","false" );
		return cp;
		
	}
	
	public static AppiumDriver<WebElement> getDriver() {
		AppiumDriver<WebElement> driver=null;

		try {
			DesiredCapabilities capabilities=getCapabilities();
			driver=new AndroidDriver<WebElement>(new URL(getUrl()), capabilities);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			driver=getDriver();
			
		}
		
		return driver;
	}
	
	private static String getUrl(){
		//String url="http://192.168.2.109:4724/wd/hub";
		String url="http://127.0.0.1:4724/wd/hub";
		return url;
	}
}
