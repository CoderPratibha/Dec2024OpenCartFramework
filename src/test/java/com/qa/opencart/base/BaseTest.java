package com.qa.opencart.base;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.RegisterPage;
import com.qa.opencart.pages.SearchResultsPage;
import com.qa.opencart.utils.LogUtil;

import io.qameta.allure.Description;

//@Listeners(ChainTestListener.class)
public class BaseTest {
	
	
	/**
	 * Base test is only for precondition and post condition.
	 */
	
	WebDriver driver;
	DriverFactory df;
	protected Properties prop;
	protected LoginPage loginPage;
	protected AccountsPage accPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductInfoPage productInfoPage;
	protected RegisterPage registerPage;
	
	private static final Logger log= LogManager.getLogger(BaseTest.class);
	@Description("init the driver and properties")
	@Parameters({"browser", "browserversion","testname"})
	@BeforeTest
	public void setup(String browserName, String browserVersion, String testname) {
		
		df= new DriverFactory();
		prop= df.initProp();
		
		if(browserName!= null) {
			
			prop.setProperty("browser", browserName);
			prop.setProperty("browserversion", browserVersion);
			prop.setProperty("testname", testname);
		}
		
		driver =df.initDriver(prop);
		loginPage = new LoginPage(driver);
		
	}
	
	
	
	
	@AfterMethod //will be running after each method
	public void attachScreenshot(ITestResult result) {
		if(!result.isSuccess()){
			log.info("screenshot is taken..");
			ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
		}
		
		//ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
		
	}
	
	
	@Description("closing the browser")
	@AfterTest
	public void tearDown() {
		driver.quit();
		log.info("closing the browser....");
	}



}
