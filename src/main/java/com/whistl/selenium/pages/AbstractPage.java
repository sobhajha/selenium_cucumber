package com.whistl.selenium.pages;

import java.util.Set;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.whistl.selenium.test.Logger;
import com.whistl.selenium.util.RenewableWebElement;
import com.whistl.selenium.util.TimeUtils;

/**
 * Abstract page class, which contains general functionality. Especially waiting
 * for a specific element.
 */
public abstract class AbstractPage implements IFramedPage {
	/** Time to sleep between checks for an element. In milliseconds. */
	private static final Long TIME_TO_SLEEP = Long.valueOf(500);
	/** Time limit to wait for an element. 30000 ms. */
	public static final Long WAIT_TIME_LIMIT = Long.valueOf(30000);
	
	
	/** WebDriver for the page. */
	private final WebDriver fDriver;
	/** Internal window identifier. Used to switch to the desired window. */
	private String windowHandle;

	public boolean isAlertPresent() {
		try {
			getDriver().switchTo().alert();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		} // catch
	}

	/**
	 * Constructor for abstract page. Is called by all classes, which extend it.
	 * 
	 * @param driver
	 *            WebDriver displaying the page
	 * @throws Exception
	 */
	public AbstractPage(final WebDriver driver) throws Exception {
		this(driver, true);
	}

	/**
	 * Constructor with an option to disable the initialisation.
	 * 
	 * @param driver
	 *            WebDriver displaying the page
	 * @param initialise
	 *            {@code true} to execute the initialisation project (see
	 *            {@link #init()})
	 * @throws Exception
	 */
	protected AbstractPage(final WebDriver driver, final boolean initialise)
			throws Exception {
		this.fDriver = driver;
		if (initialise) {
			init();
		}
	}

	/**
	 * Constructor. Merges the settings of the parameter page into the new
	 * object.
	 * 
	 * @param parentPage
	 *            {@link AbstractPage} the calling page
	 * @throws Exception
	 */
	public AbstractPage(final AbstractPage parentPage) throws Exception {
		this(parentPage, true);
	}

	/**
	 * Constructor. Merges the settings of the parameter page into the new
	 * object.
	 * 
	 * @param parentPage
	 *            {@link AbstractPage} the calling page
	 * @param initialise
	 *            {@code true} to execute the initialisation project (see
	 *            {@link #init()})
	 * @throws Exception
	 */
	public AbstractPage(final AbstractPage parentPage, final boolean initialise)
			throws Exception {
		this(parentPage.getDriver(), initialise);
		mergeSettingsFrom(parentPage);
	}

	/**
	 * Initialises this page object. It waits until page is loaded and then
	 * initialises the annotated instance variables.
	 * 
	 * @throws Exception
	 */
	protected final void init() throws Exception {
		preInit();
		waitForLoad();
		getDriver().getWindowHandle();
		this.windowHandle = getDriver().getWindowHandle();
		PageFactory.initElements(new AjaxElementLocatorFactory(getDriver(),
				TimeUtils.getTimeInSeconds(WAIT_TIME_LIMIT).intValue()), this);
	}

	/**
	 * To be overridden when needed. Called as part of the initialisation
	 * process. Should something be created/initialised before the call of
	 * {@link #waitForLoad()}, it should be done in this method.
	 */
	protected void preInit() {
	}

	/**
	 * Get the driver.
	 * 
	 * @return {@link WebDriver} presenting the page
	 */
	public final WebDriver getDriver() {
		return this.fDriver;
	}

	/**
	 * The implementer of this method has to make sure that all the necessary
	 * attributes of the underlying page are loaded after the call is finished.
	 * It doesn't necessarily mean that the page is loaded completely. <br>
	 * This method is called by the constructor {@link #AbstractPage(WebDriver)}
	 * before the elements are initialised.
	 * 
	 * @throws Exception
	 */
	public abstract void waitForLoad() throws Exception;

	/**
	 * Use this method to wait for a specified time. The timeout exception is
	 * ignored. The method {@link #waitForLoad(WebDriver, Long)} is called
	 * internally.
	 * 
	 * @param time
	 *            to wait (in milliseconds)
	 */
	public final void waitForLoad(final Long time) {
		waitForLoad(getDriver(), time);
	}

	/**
	 * Use this method to navigate to Url.
	 * 
	 * @param url
	 *            where to navigate
	 */
	protected final void navigateToUrl(final String url) {
		getDriver().navigate().to(url);
	}

	/**
	 * Use this method to wait for a specified time. The timeout exception is
	 * ignored.
	 * 
	 * @param driver
	 *            {@link WebDriver}
	 * @param time
	 *            to wait (in milliseconds)
	 */
	public static void waitForLoad(final WebDriver driver, final Long time) {
		ExpectedCondition<Boolean> eCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver innerDriver) {
				// do nothing
				return Boolean.FALSE;
			}

			@Override
			public String toString() {
				return "Waiting for specific time, not verifying anything.";
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, TimeUtils
				.getTimeInSeconds(time).longValue(), TimeUtils
				.getTimeInSeconds(time).longValue());
		try {
			wait.until(eCondition);
		} catch (org.openqa.selenium.TimeoutException e) {
			// expected. do nothing
			Logger.logStepResult("Done waiting for " + time + " ms.", false);
		}
	}

	/**
	 * Waits for the element to be loaded/created and displayed. Lets the
	 * {@link Thread} sleep as long as the element is not found or the timeout
	 * has been reached.
	 * 
	 * @param finder
	 *            a {@link By} object, which defines how to find the element
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @return the {@link RenewableWebElement} found by the {@code finder}
	 *         request parameter
	 */
	public final RenewableWebElement waitForElement(final By finder,
			final Long timeout) {
		return waitForElement(finder, timeout, getDriver());
	}

	/**
	 * Shortcut for {@link #waitForElement(By, Long)} using
	 * {@link #WAIT_TIME_LIMIT}.
	 * 
	 * @param finder
	 *            a {@link By} object, which defines how to find the element
	 * @return see {@link AbstractPage#waitForElement(By, Long)}
	 */
	public final RenewableWebElement waitForElement(final By finder) {
		return waitForElement(finder, WAIT_TIME_LIMIT);
	}

	

	/**
	 * Static variant of the method {@link #waitForElement(By, Long)}.
	 * 
	 * @param finder
	 *            a {@link By} object, which defines how to find the element
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @param driver
	 *            WebDriver, which finds the element
	 * @return the {@link RenewableWebElement} found by the {@code finder}
	 *         request parameter
	 */
	public static RenewableWebElement waitForElement(final By finder,
			final Long timeout, final WebDriver driver) {
		ExpectedCondition<RenewableWebElement> eCondition = new ExpectedCondition<RenewableWebElement>() {
			@Override
			public RenewableWebElement apply(final WebDriver innerDriver) {
				RenewableWebElement reElement = null;
				try {
					// try to find the element
					reElement = new RenewableWebElement(innerDriver, finder);
					if (!AbstractPage.isElementPresent(finder, driver)) {
						// this was not successful
						reElement = null;
					}
					// reElement = reElement.findElement(finder);
				} catch (StaleElementReferenceException e) {
					// do nothing
					Logger.logStepResult(
							"Element has been modified. Renewing, if there's time left.",
							false);
					reElement = null;
				} catch (WebDriverException e) {
					// do nothing
					Logger.logStepResult(
							"WebDriver has encountered an error. Retrying, if there's time left.",
							false);
					reElement = null;
				}
				return reElement;
			}

			@Override
			public String toString() {
				return String.format(
						"Waiting for WebElement using [%s] limited to [%d]ms.",
						finder, timeout);
			}
		};
		return waitForCondition(eCondition, timeout, driver);
	}

	/**
	 * Waits a certain time until the specified condition is fulfilled.
	 * 
	 * @param eCondition
	 *            the expected condition to wait for
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @param driver
	 *            WebDriver, which displays the element
	 * @param <T>
	 *            return object is the one returned by eCondition on successful
	 *            execution.
	 * 
	 * @return condition return value
	 */
	public static <T> T waitForCondition(final ExpectedCondition<T> eCondition,
			final Long timeout, final WebDriver driver) {
		FluentWait<WebDriver> webDriverWait = new WebDriverWait(driver,
				TimeUtils.getTimeInSeconds(timeout).longValue(),
				TIME_TO_SLEEP.longValue()).ignoring(TimeoutException.class,
				WebDriverException.class);

		return webDriverWait.until(eCondition);
	}

	

	/**
	 * Wait for the element to have an expected text value.
	 * 
	 * @param element
	 *            {@link WebElement} to look into
	 * @param expectedValue
	 *            the value, which has to be present on exit from the method
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 */
	protected final void waitForText(final WebElement element,
			final String expectedValue, final Long timeout) {
		ExpectedCondition<Boolean> eCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver driver) {
				Logger.logStepResult(
						"element text:[" + element.getText() + "]", false);
				return Boolean.valueOf(expectedValue.equals(element.getText()));
			}

			@Override
			public String toString() {
				return String
						.format("Waiting for element [%s] to have [%s] as its text. Waiting for maximum of [%d]ms.",
								element, expectedValue, timeout);
			}
		};
		waitForCondition(eCondition, timeout);
	}

	/**
	 * see {@link #waitForCondition(ExpectedCondition, Long, WebDriver)}.
	 * 
	 * @param timeout
	 *            how long to wait at maximum
	 * @param eCondition
	 *            condition to wait for
	 * @param <T>
	 *            return object is the one returned by eCondition on successful
	 *            execution.
	 * 
	 * @return condition return value
	 */
	protected final <T> T waitForCondition(
			final ExpectedCondition<T> eCondition, final Long timeout) {
		return waitForCondition(eCondition, timeout, getDriver());
	}

	/**
	 * Return the text of the element given by the search term.
	 * 
	 * @param elementSearchTerm
	 *            the search term to find the WebElement
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @return the text of the given element or <code>null</code> if the timeout
	 *         occurs earlier or the given element does not exist.
	 */
	public final String fetchText(final By elementSearchTerm, final Long timeout) {
		return fetchText(elementSearchTerm, timeout, getDriver());
	}

	/**
	 * Use this method to avoid StaleElementReferenceException in a web page
	 * using asynchronous java script code. Return the text of the element given
	 * by the search term
	 * 
	 * @param elementSearchTerm
	 *            the search term to find the WebElement
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @param driver
	 *            WebDriver displaying the page
	 * @return the text of the given element or <code>null</code> if the timeout
	 *         occurs earlier or the given element does not exist.
	 */
	public static String fetchText(final By elementSearchTerm,
			final Long timeout, final WebDriver driver) {

		ExpectedCondition<String> textFetched = new ExpectedCondition<String>() {
			@Override
			public String apply(final WebDriver aDriver) {
				RenewableWebElement element = new RenewableWebElement(aDriver,
						elementSearchTerm);
				try {
					if (element.isElementPresent()) {
						return element.getText();
					}
				} catch (StaleElementReferenceException ignore) {
					Logger.logStepResult(
							"WebElement has been modified. Renewing, if there's time left.",
							false);
				}
				return null;
			}

			@Override
			public String toString() {
				return String.format(
						"Fetching text from WebElement found via [%s]",
						elementSearchTerm);
			}
		};

		return waitForCondition(textFetched, timeout, driver);
	}

	/**
	 * Return the specified attribute value of the element given by the search
	 * term. Use this method to avoid StaleElementReferenceException in a web
	 * page using asynchronous java script code.
	 * 
	 * @param elementSearchTerm
	 *            the search term to find the WebElement
	 * @param attributeName
	 *            the name of the attribute
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @return the text of the given element or <code>null</code> if the timeout
	 *         occurs earlier or the given element does not exist.
	 */
	public final String fetchAttributeValue(final By elementSearchTerm,
			final String attributeName, final Long timeout) {
		return fetchAttributeValue(elementSearchTerm, attributeName, timeout,
				getDriver());
	}

	/**
	 * Return the specified attribute value of the element given by the search
	 * term. Use this method to avoid StaleElementReferenceException in a web
	 * page using asynchronous java script code.
	 * 
	 * @param elementSearchTerm
	 *            the search term to find the WebElement
	 * @param attributeName
	 *            the name of the attribute
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @param driver
	 *            WebDriver displaying the page
	 * @return the text of the given element or <code>null</code> if the timeout
	 *         occurs earlier or the given element does not exist.
	 */
	public static String fetchAttributeValue(final By elementSearchTerm,
			final String attributeName, final Long timeout,
			final WebDriver driver) {
		ExpectedCondition<String> attributeValueFetched = new ExpectedCondition<String>() {
			@Override
			public String apply(final WebDriver input) {
				RenewableWebElement element = waitForElement(elementSearchTerm,
						timeout, input);
				if (element != null) {
					try {
						return element.getAttribute(attributeName);
					} catch (StaleElementReferenceException ignore) {
						Logger.logStepResult(
								"WebElement has been modified. Renewing, if there's time left.",
								false);
					}
				}
				return null;
			}

			@Override
			public String toString() {
				return String
						.format("Waiting for element foud with [%s] to then fetch the value of its attribute [%s].",
								elementSearchTerm, attributeName);
			}
		};
		return waitForCondition(attributeValueFetched, timeout, driver);
	}

	/**
	 * Wait for the element to have an expected text as attribute.
	 * 
	 * @param findElementBy
	 *            {@link By} how to find the element
	 * @param attributeName
	 *            the name of the attribute to look into
	 * @param expectedValue
	 *            the value (regular expression), which has to be present on
	 *            exit from the method
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 */
	protected final void waitForAttribute(final By findElementBy,
			final String attributeName, final String expectedValue,
			final Long timeout) {
		waitForAttribute(
				findElementBy,
				attributeName,
				Pattern.compile(expectedValue, Pattern.CASE_INSENSITIVE
						| Pattern.UNICODE_CASE | Pattern.CANON_EQ), timeout,
				getDriver());
	}

	/**
	 * Wait for the element to have an expected text as attribute.
	 * 
	 * @param findElementBy
	 *            {@link By} how to find the element
	 * @param attributeName
	 *            the name of the attribute to look into
	 * @param expectedValue
	 *            the pattern, which has to be present on exit from the method
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @return attribute value after successful wait
	 * @see Pattern
	 */
	protected final String waitForAttribute(final By findElementBy,
			final String attributeName, final Pattern expectedValue,
			final Long timeout) {
		return waitForAttribute(findElementBy, attributeName, expectedValue,
				timeout, getDriver());
	}

	/**
	 * Wait for the element to have an expected text as attribute.
	 * 
	 * @param findElementBy
	 *            {@link By} how to find the element
	 * @param attributeName
	 *            the name of the attribute to look into
	 * @param expectedValue
	 *            the value, which has to be present on exit from the method
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @param driver
	 *            WebDriver to be used
	 * @return the attribute found (reasonable since a regular expression can be
	 *         used to define)
	 */
	public static String waitForAttribute(final By findElementBy,
			final String attributeName, final Pattern expectedValue,
			final Long timeout, final WebDriver driver) {
		ExpectedCondition<Boolean> eCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver innerDriver) {
				// used this method to avoid StaleElementReferenceException
				String fetchedValue = fetchAttributeValue(findElementBy,
						attributeName, WAIT_TIME_LIMIT, innerDriver);
				Logger.logStepResult("element " + attributeName + ":["
						+ fetchedValue + "] expected value pattern:["
						+ expectedValue + "]", false);
				// avoid matching a 'null'
				if (fetchedValue == null) {
					fetchedValue = "";
				}
				return Boolean.valueOf(expectedValue.matcher(fetchedValue)
						.matches());
			}

			@Override
			public String toString() {
				return String
						.format("Waiting for attribute '%s' value to have expected pattern [%s]. Element is found by [%s].",
								attributeName, expectedValue.pattern(),
								findElementBy);
			}
		};
		WebDriverWait webDriverWait = new WebDriverWait(driver, TimeUtils
				.getTimeInSeconds(timeout).longValue(),
				TIME_TO_SLEEP.longValue());
		webDriverWait.until(eCondition);
		return driver.findElement(findElementBy).getAttribute(attributeName);
	}

	/**
	 * Wait for the element to have an expected text as attribute.
	 * 
	 * @param findElementBy
	 *            {@link By} how to find the element
	 * @param attributeName
	 *            the name of the attribute to look into
	 * @param expectedValue
	 *            the value, which has to be present on exit from the method
	 * @param timeout
	 *            how much to wait till give up (in milliseconds).
	 * @param driver
	 *            WebDriver to be used
	 */
	public static void waitForAttribute(final By findElementBy,
			final String attributeName, final String expectedValue,
			final Long timeout, final WebDriver driver) {
		ExpectedCondition<Boolean> eCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver innerDriver) {
				// used this method to avoid StaleElementReferenceException
				String fetchedValue = fetchAttributeValue(findElementBy,
						attributeName, WAIT_TIME_LIMIT, innerDriver);
				Logger.logStepResult("element " + attributeName + ":["
						+ fetchedValue + "] expected:[" + expectedValue + "]",
						false);
				return Boolean.valueOf(expectedValue.equals(fetchedValue));
			}

			@Override
			public String toString() {
				return String
						.format("Waiting for attribute %s value to be [%s]. Element is found by [%s].",
								attributeName, expectedValue, findElementBy);
			}
		};
		WebDriverWait webDriverWait = new WebDriverWait(driver, TimeUtils
				.getTimeInSeconds(timeout).longValue(),
				TIME_TO_SLEEP.longValue());
		webDriverWait.until(eCondition);
	}

	/**
	 * Navigate the {@link #fDriver} to the inner frame defined by all
	 * parameters.
	 * 
	 * @param findersForFrames
	 *            {@link By} finders for subframes to reach the desired frame.
	 *            Each finder is relative to the previous one.
	 */
	public final void switchToFrame(final By... findersForFrames) {
		switchToFrame(getDriver(), findersForFrames);
	}

	/**
	 * Switch to New window by clicking to WebElement found by <b>finder</b>.
	 * 
	 * @param finder
	 *            how to find the element to click on.
	 */
	public final void switchToNewWindowByClick(final By finder) {
		// get current window handles
		final Set<String> oldWindowHandles = getDriver().getWindowHandles();
		waitForElement(finder, WAIT_TIME_LIMIT).click();
		// get current window handles
		// compare handles

		Set<String> newWindowHandles = waitForCondition(
				new ExpectedCondition<Set<String>>() {
					public Set<String> apply(final WebDriver driver) {
						Set<String> innerWindowHandles = getDriver()
								.getWindowHandles();
						if (innerWindowHandles.size() > oldWindowHandles.size()) {
							return innerWindowHandles;
						}
						return null;
					}

					@Override
					public String toString() {
						return "Waiting for a new window to open.";
					}
				}, WAIT_TIME_LIMIT);

		for (String windowHandles : newWindowHandles) {
			if (oldWindowHandles.contains(windowHandles)) {
				continue;
			}
			// switch to the new window handle
			Logger.logStepResult("Switching to window handle [" + windowHandles
					+ "]", false);
			getDriver().switchTo().window(windowHandles);
		}
	}

	/**
	 * Static variant to navigate the {@link #fDriver} to the inner frame
	 * defined by all parameters.
	 * 
	 * @param driver
	 *            {@link WebDriver}
	 * @param xPathsToFrame
	 *            xpaths for subframes to reach the desired frame. Each xpath is
	 *            relative to the previous one.
	 * @deprecated
	 */
	public static void switchToFrame(final WebDriver driver,
			final String... xPathsToFrame) {
		// navigate to the right frame
		for (int i = 0; i < xPathsToFrame.length; i++) {
			WebElement innerFrame = driver.findElement(By
					.xpath(xPathsToFrame[i]));
			driver.switchTo().frame(innerFrame);
		}
	}

	/**
	 * Static variant to navigate the {@link #fDriver} to the inner frame
	 * defined by all parameters. First try to find the frame, then switch to
	 * it.
	 * 
	 * @param driver
	 *            {@link WebDriver}
	 * @param findersForFrames
	 *            {@link By} finders for subframes to reach the desired frame.
	 *            Each finder is relative to the previous one.
	 */
	public static void switchToFrame(final WebDriver driver,
			final By... findersForFrames) {
		for (By finder : findersForFrames) {
			waitForElement(finder, WAIT_TIME_LIMIT, driver);
			driver.switchTo().frame(driver.findElement(finder));
		}
	}

	/**
	 * @param captureMode
	 *            the captureMode to set
	 */
	public static void setCaptureMode(final Boolean captureMode) {
		Logger.INSTANCE.setCaptureMode(captureMode);
	}

	/**
	 * Merge settings of one abstract page to another one. Make sure that the
	 * settings are identical. After the call {@code this} page has the same
	 * settings as the parameter page.
	 * 
	 * @param parentPage
	 *            page with parameters
	 */
	public void mergeSettingsFrom(final AbstractPage parentPage) {
		// currently do nothing
	}

	/**
	 * This function is used to check if the element is present on the page.
	 * 
	 * @param finder
	 *            how to find the element
	 * @return {@code true} if the element was immediately found on the page.
	 */
	public final boolean isElementPresent(final By finder) {
		return isElementPresent(finder, getDriver());
	}

	/**
	 * This function is used to check if the element is present on the page.
	 * 
	 * @param finder
	 *            how to find the element
	 * @return {@code true} if the element was immediately found on the page.
	 */
	public final boolean isElementPresent(final By finder, final Long timeout) {
		return isElementPresent(finder, timeout, getDriver());
	}

	/**
	 * This function is used to check if the element is present on the page.
	 * 
	 * @param finder
	 *            how to find the element
	 * @param driver
	 *            WebDriver displaying the page
	 * @return {@code true} if the element was immediately found on the page.
	 */
	public static boolean isElementPresent(final By finder,
			final WebDriver driver) {
		RenewableWebElement webElement = new RenewableWebElement(driver, finder);
		return webElement.isElementPresent();

	}

	public static boolean isElementPresent(final By finder, final Long timeout,
			final WebDriver driver) {
		final RenewableWebElement webElement = new RenewableWebElement(driver,
				finder);
		try {
			return waitForCondition(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return webElement.isElementPresent();
				}
			}, timeout, driver);
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Get the identifier for the window/page. Can be used to do
	 * {@code getDriver().switchTo().window(windowHandle)}.
	 * 
	 * @return a window handle(identifier) to navigate to this page.
	 */
	public final String getWindowHandle() {
		return this.windowHandle;
	}

	/**
	 * Reload Page
	 */
	public void reloadPage() {
		getDriver().navigate().refresh();
	}
}
