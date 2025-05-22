package com.qa.opencart.pages;

import static com.qa.opencart.constants.AppConstants.DEFAULT_TIMEOUT;
import static com.qa.opencart.constants.AppConstants.MEDIUM_DEFAULT_TIMEOUT;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.utils.ElementUtil;

public class ProductInfoPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1-Private locators

	private final By productHeader = By.tagName("h1");
	private final By productImages = By.cssSelector("ul.thumbnails img");
	private final By productMetaData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[1]/li");
	private final By pricingData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[2]/li");
	private Map<String, String> productMap;

	// 2-Public constructor
	public ProductInfoPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(driver);

	}

	public String getProductHeader() {
		String header = eleUtil.waitForElementVisible(productHeader, MEDIUM_DEFAULT_TIMEOUT).getText();
		System.out.println("product header :" + header);
		return header;
	}

	public int getProductImagesCount() {
		int imageCount = eleUtil.waitForAllElementsVisible(productImages, MEDIUM_DEFAULT_TIMEOUT).size();
		System.out.println("Total number of images :" + imageCount);
		return imageCount;

	}

	public Map<String, String> getProductDetailsMap() {
		//HashMap - non order based collection
		//LinkedHashMap -maintain insertion order--the order you have inserted the values
		//TreeMap- maintain sorted order only for keys
		//productMap = new HashMap<String, String>();
		//productMap = new LinkedHashMap<String, String>();
		productMap = new TreeMap<String, String>();
		
		productMap.put("productheader", getProductHeader());
		productMap.put("productimages", String.valueOf(getProductImagesCount()));
		getProductMetaData();
		System.out.println("Full product details :" +productMap);
		return productMap;
	}

	private void getProductMetaData() {
		List<WebElement> metaList = eleUtil.waitForAllElementsVisible(productMetaData, DEFAULT_TIMEOUT);
		for (WebElement e : metaList) {
			String metaData = e.getText();
			String meta[] = metaData.split(":");
			String metaKey = meta[0].trim();
			String metaValue = meta[1].trim();
			productMap.put(metaKey, metaValue);
			getProductPriceData();

		}

	}

	private void getProductPriceData() {
		List<WebElement> priceList = eleUtil.waitForAllElementsVisible(pricingData, DEFAULT_TIMEOUT);
		String productPrice = priceList.get(0).getText();
		String exTaxPrice = priceList.get(1).getText().split(":")[1].trim();
		productMap.put("productprice", productPrice);
		productMap.put("extaxprice", exTaxPrice);

	}

}
