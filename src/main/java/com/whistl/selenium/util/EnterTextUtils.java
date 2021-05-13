package com.whistl.selenium.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.test.BrowserLogger;

public class EnterTextUtils {
	public static void enterInput(final String inputValue,
			final String toStrMessage, final String inputEnteredMessage,
			final By inputFinder, WebDriver webDdriver) {
		final RenewableWebElement inputField = AbstractPage.waitForElement(
				inputFinder, AbstractPage.WAIT_TIME_LIMIT, webDdriver);
		inputField.clear();
		AbstractPage.waitForLoad(webDdriver, Long.valueOf(1000));
		AbstractPage.waitForCondition(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				inputField.clear();
				inputField.sendKeys(inputValue);
				String value = AbstractPage.fetchAttributeValue(inputFinder,
						"value", AbstractPage.WAIT_TIME_LIMIT / 3, driver);
				return inputValue.contentEquals(value);
			}

			public String toString() {
				return toStrMessage;
			}
		}, AbstractPage.WAIT_TIME_LIMIT, webDdriver);
		BrowserLogger.logStepResult(inputEnteredMessage, inputField,
				EnterTextUtils.class.toString());
	}

	public static String fetchInput(final By inputFinder, WebDriver driver) {
		return AbstractPage.fetchAttributeValue(inputFinder, "value",
				AbstractPage.WAIT_TIME_LIMIT / 3, driver);
	}

}
