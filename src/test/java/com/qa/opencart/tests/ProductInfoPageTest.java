package com.qa.opencart.tests;

import java.util.Map;
import static com.qa.opencart.constants.AppConstants.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.CSVUtil;
import com.qa.opencart.utils.ExcelUtil;

public class ProductInfoPageTest extends BaseTest {

	@BeforeClass
	public void productInfoSetup() {

		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));

	}

	@DataProvider
	public Object[][] getProductTestData() {

		return new Object[][] { { "macbook", "MacBook Pro" }, { "macbook", "MacBook Air" }, { "imac", "iMac" },
				{ "samsung", "Samsung SyncMaster 941BW" }, { "samsung", "Samsung Galaxy Tab 10.1" } };
	}

	@Test(dataProvider = "getProductTestData")
	public void productHeaderTest(String searchKey, String productName) {

		searchResultsPage = accPage.doSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(productName);
		String actHeader = productInfoPage.getProductHeader();
		Assert.assertEquals(actHeader, productName);
	}

	// @DataProvider
	public Object[][] getProductImagesData() {

		return new Object[][] { { "macbook", "MacBook Pro", 4 }, { "macbook", "MacBook Air", 4 }, { "imac", "iMac", 3 },
				{ "samsung", "Samsung SyncMaster 941BW", 1 }, { "samsung", "Samsung Galaxy Tab 10.1", 7 } };
	}

	@DataProvider
	public Object[][] getProductData() {

		return ExcelUtil.getTestData(PRODUCT_SHEET_NAME);
}

	@DataProvider
	public Object[][] getCSVData() {

		return CSVUtil.csvData("product");
}

	@Test(dataProvider = "getCSVData")
	public void productImageCountTest(String searchKey, String productName, String expectedImageCount) {

		searchResultsPage = accPage.doSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(productName);
		int actImageCount = productInfoPage.getProductImagesCount();
		Assert.assertEquals(String.valueOf(actImageCount), expectedImageCount);
	}

	@Test
	public void prodcutInfoTest() {

		searchResultsPage = accPage.doSearch("MacBook Pro");
		productInfoPage = searchResultsPage.selectProduct("MacBook Pro");
		Map<String, String> actualProductDetailsMap = productInfoPage.getProductDetailsMap();

		SoftAssert softassert = new SoftAssert();
		softassert.assertEquals(actualProductDetailsMap.get("productheader"), "MacBook Pro");
		softassert.assertEquals(actualProductDetailsMap.get("productimages"), "4");

		softassert.assertEquals(actualProductDetailsMap.get("Brand"), "Apple");
		softassert.assertEquals(actualProductDetailsMap.get("Product Code"), "Product 18");
		softassert.assertEquals(actualProductDetailsMap.get("Reward Points"), "800");
		softassert.assertEquals(actualProductDetailsMap.get("productprice"), "$2,000.00");
		softassert.assertEquals(actualProductDetailsMap.get("extaxprice"), "$2,000.00");

		softassert.assertAll();

	}

}
