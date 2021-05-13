package com.whistl.selenium.pages.smartwyre;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.util.ExpectedConditionFactory;
import com.whistl.selenium.util.RenewableWebElement;

public class DemoHomePage extends AbstractPage {
	
	private DemoMenuList demoMenuList ;
	private final static By YES_PLEASE_BUTTON = By
			.xpath(".//*[text()='Yes please!']");
	private final static By NO_THANKS_BUTTON = By
			.xpath(".//*[text()='No, thanks!']");

	public DemoHomePage(AbstractPage parentPage) throws Exception {
		super(parentPage);
		demoMenuList = new DemoMenuList(parentPage);
	}

	
	public DemoHomePage(WebDriver driver) throws Exception {
		super(driver);
		demoMenuList = new DemoMenuList(driver);
	}

	public void clickkNoThanks() {
	
		RenewableWebElement noThanksButton = waitForElement(NO_THANKS_BUTTON, WAIT_TIME_LIMIT);
		
		WebDriverWait wait = new WebDriverWait(getDriver(), WAIT_TIME_LIMIT);
		wait.until(ExpectedConditions.elementToBeClickable(NO_THANKS_BUTTON));
		waitForCondition(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver input) {
				if(isElementPresent(NO_THANKS_BUTTON)) {
				noThanksButton.click();
				waitForLoad(3000L);
				}
				return !isElementPresent(NO_THANKS_BUTTON);
			}
		}, WAIT_TIME_LIMIT);
	}
	
    public void clickYesPlease() {
    	RenewableWebElement yesPleaseButton = waitForElement(YES_PLEASE_BUTTON, WAIT_TIME_LIMIT);
    	yesPleaseButton.click();
	}
	
	@Override
	public void resetActiveIFrame() {

	}

	@Override
	public void waitForLoad() throws Exception {

	}


	public DemoMenuList getDemoMenuList() {
		return demoMenuList;
	}

}
