package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.qa.opencart.constants.AppConstants.*;
import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.ExcelUtil;

public class RegisterPageTest extends BaseTest{
	
	
	@BeforeClass
	public void registerSetup() {
		
		registerPage= loginPage.navigateToRegisterPage();
}
	
	//@DataProvider
	public Object[][] getUserRegTestData() {
		
		return new Object[][] {
			{"Visu", "Pratap","vishaler@open.com","8765432123", "yes" },
			{"Ritu", "Khanna","rituqa@open.com","8734232654", "no" },
			{"Shyam", "Sundar","shyamqaz@open.com","1234567876","yes" }
		};
		
	}
	
	@DataProvider
	public Object[][] getUserRegData() {
		
		Object regData[][]=ExcelUtil.getTestData(REGISTER_SHEET_NAME);
		
		return regData;
	}
	
	@Test(dataProvider="getUserRegData")
	public void userRegisterTest(String firstName, String lastName,String telephone,
			String password , String subscribe) {
		
		Assert.assertTrue(
				registerPage.
				   	userRegistration(firstName, lastName,telephone, password, subscribe));
	}
	
	
	
	

}
