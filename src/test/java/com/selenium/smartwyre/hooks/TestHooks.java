package com.selenium.smartwyre.hooks;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.test.Logger;
import com.whistl.selenium.util.CaptureScreenshots;
import com.whistl.selenium.util.ConfigReader;
import com.whistl.selenium.util.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class TestHooks {
	
	//private WebDriver driver;
	private ConfigReader configReader;
	private Properties prop;
	/** Screenshot store folder. */
	private String fScreenshotStoreFolder;
	private final static By I_AGREE_COOKIED_FINDER = By.xpath(".//*[text()='I agree']");
	
	@Before(order = 0)
	public void getProperty() {
	  	configReader = new ConfigReader();
	  	prop = configReader.init_prop();
	  	AbstractPage.setCaptureMode(true);
	}
	
	
	@Before(order = 1, value = "@First")
	public void launchBrowser() {		
		String browserName = prop.getProperty("browser");
		String testTarget = prop.getProperty("testTarget");
		Logger.logStepResult("Browser name is "+ browserName, false);
		Logger.logStepResult("testTarget url is "+ testTarget, false);

		new DriverFactory().init_driver(browserName,testTarget);
		acceptCookiesPolicy();
	}

	
	@After(order = 0, value = "@Last")
		public void quitBrowser() {
		DriverFactory.getDriver().quit();
	}
	
	@After(order = 1, value = "@Last")
	public void tearDown() {
		this.fScreenshotStoreFolder = prop.getProperty("screenshot");

		// screenshots functionality must be present even if everything failed
				initCaptureScreenshot();
				// hook to execute something before closing everything
				Logger.logStepResult("Closing session.", false);
				CaptureScreenshots.INSTANCE.resetCounter();
				if (DriverFactory.getDriver() != null && DriverFactory.getDriver().getWindowHandles() != null) {
					// driver has been successfully initialised and something is still
					// open
					for (String windowHandle : DriverFactory.getDriver().getWindowHandles()) {
						try {
							DriverFactory.getDriver().switchTo().window(windowHandle);
							Logger.logStepResult("Closing window " + windowHandle,
									Boolean.FALSE);
							DriverFactory.getDriver().close();
							Logger.logStepResult("Closed window " + windowHandle,
									Boolean.FALSE);
						} catch (NoSuchWindowException e) {
							// do nothing
							Logger.logStepResult("failed to close the window",
									Boolean.FALSE);
						}
					}
					try {
						// waiting for close to take effect before quitting
						// (https://bugzilla.mozilla.org/show_bug.cgi?id=1027222)
						Logger.logStepResult(
								"waiting for 3 secs before terminating the session.",
								Boolean.FALSE);
						Thread.sleep(3000L);
					} catch (InterruptedException e) {
						// do nothing
						Logger.logStepResult("interrupted while waiting", Boolean.FALSE);
					}
					Logger.logStepResult("Quitting session.", Boolean.FALSE);
					try {
						DriverFactory.getDriver().quit();
					} catch (WebDriverException e) {
						// driver seems to be dead already, but I don't care
						Logger.logStepResult("driver.quit was not successfully sent",
								Boolean.FALSE);
					}
				}
				Logger.logStepResult("Session closed.", Boolean.FALSE);
	}
	
	private void acceptCookiesPolicy() {
         Set<String> windowHandles = DriverFactory.getDriver().getWindowHandles();
         
         if(windowHandles.size()>1) {
        	 DriverFactory.getDriver().switchTo().frame(0);
        	 DriverFactory.getDriver().switchTo().alert().accept();
         }else if(AbstractPage.isElementPresent(I_AGREE_COOKIED_FINDER, DriverFactory.getDriver())) {
             DriverFactory.getDriver().findElement(I_AGREE_COOKIED_FINDER).click();

         }
		DriverFactory.getDriver().switchTo().defaultContent();
	}
	
	/**
	 * checks for the screenshot functionality to be ready for usage.
	 * Initialises the folder and set driver fo screenshot capture.
	 */
	private void initCaptureScreenshot() {
		Method thisMethod = null;
		try {
			thisMethod = this.getClass().getMethod("cleanAfterTests");
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		initScreenshotFolder(this.fScreenshotStoreFolder, thisMethod);
		CaptureScreenshots.INSTANCE.setDriver(DriverFactory.getDriver());
		// }

	}
	
	/**
	 * Initialise the screenshot store folder. Be aware that the
	 * {@link CaptureScreenshot} is not ready for usage after this. The driver
	 * still needs to be set via {@link CaptureScreenshots#setDriver(WebDriver)}
	 * .
	 * 
	 * @param screenshotStoreFolder
	 *            folder to store the screenshots
	 * @param method
	 *            calling method (might be a test method or cleanAfterTests)
	 */
	private static void initScreenshotFolder(
			final String screenshotStoreFolder,
			final java.lang.reflect.Method method) {
		// store the folder for screenshots for further reference
		String callingClassName = method.getDeclaringClass().getName();
		String callingMethodName = method.getName();

		CaptureScreenshots.INSTANCE
				.initScreenshotStoreFolder(screenshotStoreFolder
						+ File.separator
						+ callingClassName.substring(callingClassName
								.lastIndexOf(".") + 1) + File.separator
						+ callingMethodName);

	}
	
	/**
	 * To execute something after tests. Default implementation checks does
	 * nothing. Override to do something.
	 */
	public void cleanAfterTests() {
	}
}
