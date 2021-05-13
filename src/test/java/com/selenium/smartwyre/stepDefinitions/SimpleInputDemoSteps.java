package com.selenium.smartwyre.stepDefinitions;

import com.whistl.selenium.pages.smartwyre.DemoHomePage;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.MAIN_MENU;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.SUB_MENU;
import com.whistl.selenium.pages.smartwyre.SimpleFormDemoPage;
import com.whistl.selenium.test.evaluation.SoftAssert;
import com.whistl.selenium.util.DriverFactory;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SimpleInputDemoSteps {
	private DemoHomePage demoHomePage;
	private SimpleFormDemoPage simpleFormDemoPage;

	
	@Given("User in on selenium easy demo page")
	public void user_in_on_selenium_easy_demo_page() throws Exception {
		demoHomePage= new DemoHomePage(DriverFactory.getDriver());
		demoHomePage.clickkNoThanks();
	}

	@When("User expands input form menu")
	public void user_expands_input_form_menu() {
		
		demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.ALL_EXAMPLE);
		demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.INPUT_FORM);	

	}

	@When("click on simple form demo")
	public void click_on_simple_form_demo() throws Exception {
		demoHomePage.getDemoMenuList().navigate_to_sub_menu(SUB_MENU.SIMPLE_FORM_DEMO);
		
	}
	
	@Then("user should on simple form demo page")
	public void user_should_on_simple_form_demo_page() throws Exception {
		simpleFormDemoPage = new SimpleFormDemoPage(DriverFactory.getDriver());
	}


	@When("enter <text> in the Enter Message Input Field")
	public void enter_text_in_the_enter_message_input_field(String text) {
		System.out.println("***************************   " + text);
         simpleFormDemoPage.enterInputMessage(text);
	}

	@When("click on button show message")
	public void click_on_button_show_message() {
	}

	@Then("same entered <text> should get displayed")
	public void same_entered_text_should_get_displayed(String text) {
		System.out.println("***************************   " + text);

		String displayedMessage = simpleFormDemoPage.clickShowMessage();
		SoftAssert.verifyEquals(displayedMessage,text,"Entered text message is "+ text + " but displayed text message is "+ displayedMessage);

	}

	@Given("User is on simple input form page")
	public void user_is_on_simple_input_form_page() throws Exception {
		
		simpleFormDemoPage = new SimpleFormDemoPage(DriverFactory.getDriver());
            
	}

	@When("User enters {int} in input field a and {int} in input field b")
	public void user_enters_in_input_field_a_and_in_input_field_b(Integer int1, Integer int2) {
     simpleFormDemoPage.enterValueInBothInputBox(int1, int2);
      
	}

	@When("clicks on Get Total  button")
	public void clicks_on_get_total_button() {
       simpleFormDemoPage.clickOnGetTotalButton();
	}

	@Then("user must get {int}")
	public void user_must_get(Integer actualSum) {
		int sum = simpleFormDemoPage.getTheTotalSUM();
		SoftAssert.verifyEquals(sum,actualSum,"Actual sum is "+ sum + " but sum received is "+ actualSum);

	
	}



}
