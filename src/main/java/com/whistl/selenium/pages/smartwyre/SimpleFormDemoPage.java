package com.whistl.selenium.pages.smartwyre;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.util.EnterTextUtils;

public class SimpleFormDemoPage extends AbstractPage {

	private DemoMenuList demoMenuList;
	public static final By SINGLE_INPUT_FIELD_FILNDER = By.xpath(".//*[text()='Single Input Field']");
	public static final By INPUT_FIELD_FINDER = By.xpath(".//input[@id='user-message']");
	public static final By SHOW_MESSAGE_FINDER = By.xpath(".//button[text()='Show Message']");
	public static final By YOUR_MESSAGE_FINDER= By.xpath(".//*[@id='display']");
	public static final By SUM1_FINDER = By.xpath(".//*[@id='sum1']");
	public static final By SUM2_FINDER = By.xpath(".//*[@id='sum2']");
	public static final By GET_TOTAL_FINDER = By.xpath(".//*[text()='Get Total']");
	public static final By GET_TOTAL_SUM_FINDER = By.xpath(".//*[@id='displayvalue']");
	
	
	
	
	
	public void enterInputMessage(String inputMessage) {
		EnterTextUtils.enterInput(inputMessage, "Entered Message is "+ inputMessage, "Input Message is "+ inputMessage, INPUT_FIELD_FINDER, getDriver());
	}
	
	
	public String clickShowMessage() {
	    waitForElement(SHOW_MESSAGE_FINDER,WAIT_TIME_LIMIT).click();
	   return fetchText(YOUR_MESSAGE_FINDER, WAIT_TIME_LIMIT);
	    
	}
	
	
	public void enterValueInBothInputBox(int a, int b) {
		
		EnterTextUtils.enterInput(Integer.toString(a), "Entered String Value is "+Integer.toString(a) , "Entered Value", SUM1_FINDER, getDriver());
	
		EnterTextUtils.enterInput(Integer.toString(b), "Entered String Value is "+Integer.toString(b) , "Entered Value", SUM2_FINDER, getDriver());

		waitForElement(GET_TOTAL_FINDER, WAIT_TIME_LIMIT).click();
		
	}
	
	
	public void clickOnGetTotalButton() {
		
		waitForElement(GET_TOTAL_FINDER, WAIT_TIME_LIMIT).click();

	}
	
	
	
	public int getTheTotalSUM() {
		
		
		return Integer.valueOf(fetchText(GET_TOTAL_SUM_FINDER, WAIT_TIME_LIMIT));
	
	}
	
	
	public SimpleFormDemoPage(AbstractPage parentPage) throws Exception {
		super(parentPage);
		this.demoMenuList = new DemoMenuList(parentPage);
	}

	
	public SimpleFormDemoPage(WebDriver driver) throws Exception {
		super(driver);
		this.demoMenuList = new DemoMenuList(driver);
	}
	
	
	@Override
	public void resetActiveIFrame() {
	}

	@Override
	public void waitForLoad() throws Exception {
		
		waitForElement(SINGLE_INPUT_FIELD_FILNDER, WAIT_TIME_LIMIT);
	}


	public DemoMenuList getDemoMenuList() {
		return demoMenuList;
	}

}
