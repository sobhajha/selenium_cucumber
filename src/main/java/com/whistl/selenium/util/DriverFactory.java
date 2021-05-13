package com.whistl.selenium.util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.whistl.selenium.test.BrowserLogger;
import com.whistl.selenium.test.Logger;


public class DriverFactory {
	//private WebDriver fDriver = null;

//private String testTarget;
	public static ThreadLocal<WebDriver> tlDriver= new ThreadLocal<>();
	
	public static synchronized WebDriver getDriver() {
		return tlDriver.get();
	}
	
	public WebDriver init_driver(String browser, String testTarget) {
		if(browser.equals("chrome")) {
			
			DesiredCapabilities capabilities=DesiredCapabilities.chrome();
			capabilities.setPlatform(Platform.valueOf("MAC"));
			ChromeOptions options = new ChromeOptions();
			options.merge(capabilities);
			applyDriver("webdriver.chrome.driver", "drivers/ChromeDriver/chromedriver");

			 tlDriver.set(new ChromeDriver(options));

			
		}
		else if(browser.equals("firefox")){
			
			DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			capabilities.setPlatform(Platform.valueOf("MAC"));
			FirefoxOptions options = new FirefoxOptions();
			options.merge(capabilities);
			applyDriver("webdriver.chrome.driver", "drivers/gekoDriver/chromedriver");

			 tlDriver.set(new FirefoxDriver(options));
		}
		else if(browser.equals("safari")) {
			DesiredCapabilities capabilities=DesiredCapabilities.safari();
			capabilities.setPlatform(Platform.valueOf("MAC"));
			SafariOptions options = new SafariOptions();
			options.merge(capabilities);
			applyDriver("webdriver.chrome.driver", "drivers/gekoDriver/chromedriver");

			 tlDriver.set(new SafariDriver(options));
		}
		else {
			Logger.logStepResult("Please pass the correct browser value", false);
		}
		
		BrowserLogger.INSTANCE.setDriver(getDriver());	
		ElementHighlighter.INSTANCE.setDriver(getDriver());
		Logger.reset();
		// make the driver known to the CaptureScreenshots
		CaptureScreenshots.INSTANCE.setDriver(getDriver());

			// delete cookies
         getDriver().manage().deleteAllCookies();
         getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// now open the web site
			getDriver().get(testTarget);

		return getDriver();
	}
	
	

	
	/**
	 * Verifies that the driver is available under the defined path and sets the
	 * system property. Used for local execution of tests.
	 * 
	 * @param driverProperty
	 *            property as required by the driver
	 * @param driverPath
	 *            path to the driver executable
	 */
	private static void applyDriver(final String driverProperty,
			final String driverPath) {
		if (driverPath != null && driverPath.trim().length() > 0) {
			File serverDriverFile = new File(driverPath);
			// the parameter was set
			if (serverDriverFile.exists()) {
				// can access the file
				System.setProperty(driverProperty, driverPath);
			} else {
				throw new RuntimeException("Cannot access driver '"
						+ driverProperty + "' with path ["
						+ serverDriverFile.getAbsolutePath() + "]");
			}
		}
	}


	
}
