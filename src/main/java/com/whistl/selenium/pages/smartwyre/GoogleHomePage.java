package com.whistl.selenium.pages.smartwyre;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.util.EnterTextUtils;
import com.whistl.selenium.util.RenewableWebElement;

public class GoogleHomePage extends AbstractPage {

	public static final By SEARCH_INPUT_TEXT_FINDER = By.xpath(".//input[@name='q']");
	public static final String SEARCH_LIST = ".//ul[@role='listbox']/li[%s]//div[@class='aypzV']";
	

	public GoogleHomePage(AbstractPage parentPage) throws Exception {
		super(parentPage);
	}

	public void enterTextInSearchInput(String searchText) {

		EnterTextUtils.enterInput(searchText, "Entered search text is ", "Input Entered Message",
				SEARCH_INPUT_TEXT_FINDER, getDriver());
	}

	
	public List<String> getSearchResultsList(){
		List<String> searchResultText = new ArrayList<String>();
		List<WebElement> searchResults=   getDriver().findElements(By.xpath(String.format(SEARCH_LIST, "*")));;
       
		int length = searchResults.size();
		for (int i =0 ; i< length;i++) {
        	 searchResultText.add(searchResults.get(i).getText());
        	 
         }	
	  return searchResultText;
	}
	
	
	public void clickOnNthSearchResult(int i) {
		RenewableWebElement nthSearchResult =waitForElement(By.xpath(String.format(SEARCH_LIST, i)));
		nthSearchResult.click();
	}
	
	
	public GoogleHomePage(WebDriver driver) throws Exception {
		super(driver);
	}

	@Override
	public void resetActiveIFrame() {
		// TODO Auto-generated method stub

	}

	
	
	@Override
	public void waitForLoad() throws Exception {
     waitForElement(SEARCH_INPUT_TEXT_FINDER);
	}

}
