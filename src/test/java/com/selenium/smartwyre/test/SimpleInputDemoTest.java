package com.selenium.smartwyre.test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.pages.smartwyre.DemoHomePage;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.MAIN_MENU;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.SUB_MENU;
import com.whistl.selenium.pages.smartwyre.SimpleFormDemoPage;
import com.whistl.selenium.test.AbstractTest;
import com.whistl.selenium.test.evaluation.SoftAssert;

public class SimpleInputDemoTest extends AbstractTest {
	private DemoHomePage demoHomePage;
	private SimpleFormDemoPage simpleFormDemoPage;


	/**
	 * This test will enter string message in input value . And also validates if
	 * the entered text is same as displayed text {@text}
	 * 
	 * @param text to be entered in the input value
	 * @throws Exception
	 */
	@Test
	@Parameters({ "text" })
	public void seleniumEnterInputTest(String text) throws Exception {
		AbstractPage.setCaptureMode(true);
		demoHomePage = new DemoHomePage(getDriver());
		demoHomePage.clickkNoThanks();

		// @When("User expands input form menu")
		demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.ALL_EXAMPLE);
		demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.INPUT_FORM);

		// @When("click on simple form demo")

		demoHomePage.getDemoMenuList().navigate_to_sub_menu(SUB_MENU.SIMPLE_FORM_DEMO);
		simpleFormDemoPage = new SimpleFormDemoPage(getDriver());

		// @When("enter <text> in the Enter Message Input Field")
		simpleFormDemoPage.enterInputMessage(text);

		// @When("click on button show message")
		// @Then("same entered <text> should get displayed")

		String displayedMessage = simpleFormDemoPage.clickShowMessage();
		SoftAssert.verifyEquals(displayedMessage, text,
				"Entered text message is " + text + " but displayed text message is " + displayedMessage);

	}


	/**
	 * This test will enter integer in input value . 
	 * And also validates if sum of sum1+sum2 is same as expected 
	 * @param sum1 first integer value
	 * @param sum2 second integer value
	 * @param actualSum  actual expected sum for sum1+sum2
	 * @throws Exception
	 */
	@Test
	@Parameters({ "sum1", "sum2", "actualSum" })

	public void seleniumSumDemoTest(int sum1, int sum2, int actualSum) throws Exception {
		AbstractPage.setCaptureMode(true);

		// @Given("User is on simple input form page")

		simpleFormDemoPage = new SimpleFormDemoPage(getDriver());
		// @When("User enters {int} in input field a and {int} in input field b")

		simpleFormDemoPage.enterValueInBothInputBox(sum1, sum2);
		// @When("clicks on Get Total button")

		simpleFormDemoPage.clickOnGetTotalButton();

		// @Then("user must get {int}")

		int sum = simpleFormDemoPage.getTheTotalSUM();
		SoftAssert.verifyEquals(sum, actualSum, "Actual sum is " + sum + " but sum received is " + actualSum);

	}

}
