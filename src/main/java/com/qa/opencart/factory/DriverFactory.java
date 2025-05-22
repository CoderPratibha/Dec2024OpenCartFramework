package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.exceptions.BrowserException;
import com.qa.opencart.exceptions.FrameworkException;

import io.qameta.allure.Step;

public class DriverFactory {
	
	WebDriver driver;
	Properties prop;
	OptionsManager optionsManager; 
	
	public static ThreadLocal<WebDriver> tlDriver= new ThreadLocal<WebDriver>();
	
	public static String highlight;
	private static final Logger log= LogManager.getLogger(DriverFactory.class);
	
	/**
	 * This method is used to initialize the driver on the basis of given browser name.
	 * @param browserName
	 * @return 
	 */
	@Step("init  driver with properties :{0}")
	public WebDriver initDriver(Properties prop) {
		log.info("properties : "+prop);
		String browserName =prop.getProperty("browser");
		//System.out.println("browser name : "+ browserName);
		
		log.info("browser name : "+ browserName);
		ChainTestListener.log("browser name : "+browserName);
		optionsManager = new OptionsManager(prop);
		
		highlight= prop.getProperty("highlight");
		
		switch(browserName.toLowerCase().trim()) {
		case "chrome":
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			break;
			
		case "edge":
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			break;
			
		case "firefox":
			tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			break;
			
		case "safari":
			tlDriver.set(new SafariDriver());
			break;
			
			default:
				//System.out.println("plz pass the valid browser name... " +browserName);
				log.error("plz pass the valid browser name... \" +browserName");
				throw new BrowserException("=====Invalid Browser=====");
				
				
		}
		
		getDriver().get(prop.getProperty("url"));//login page url
		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();
		return getDriver();
		
	}
	
	
	/**
	 * get the local thread copy of driver
	 * @return 
	 */
	
	public static WebDriver getDriver() {
		return tlDriver.get();
	}
		/**
		 * This is used to initialize the config properties		
		 * @return
		 */
		
	
	//mvn clean install -Denv
		public Properties initProp() {
			
			String envName = System.getProperty("env");
			FileInputStream ip= null;
			prop= new Properties();
			
			
			try {
			if(envName ==null) {
				//System.out.println("env is null , hence running the tests on QA env");
				log.warn("env is null , hence running the tests on QA env");
				ip= new FileInputStream("./src/test/resources/config/config.properties");
			}else {
				
				log.info("Running tests on env: " +envName);
				switch (envName.toLowerCase().trim()) {
				case "qa": {
					ip= new FileInputStream("./src/test/resources/config/qa.config.properties");
					break;
			}
				case "dev": {
					ip= new FileInputStream("./src/test/resources/config/dev.config.properties");
					break;
			}
				case "stage": {
					ip= new FileInputStream("./src/test/resources/config/stage.config.properties");
					break;
			}
				case "uat": {
					ip= new FileInputStream("./src/test/resources/config/uat.config.properties");
					break;
			}
				default:
					log.error("----invalid environment name----"+ envName);
					throw new FrameworkException("===Invalid ENV Name=== : "+envName);
				}
			}
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				prop.load(ip);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			return prop;
		}
		
		
		/**
		 * takesscreenshot
		 * 
		 */
		
		public static File getScreenshotFile() {
			File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// temp dir
			return srcFile;
		}

		public static byte[] getScreenshotByte() {
			return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);// temp dir

		}

		public static String getScreenshotBase64() {
			return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);// temp dir

		}
}
