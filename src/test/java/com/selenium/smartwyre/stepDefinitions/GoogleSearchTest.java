package com.selenium.smartwyre.stepDefinitions;

import java.util.List;

import org.testng.asserts.SoftAssert;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.pages.smartwyre.GoogleHomePage;
import com.whistl.selenium.test.TestListener;
import com.whistl.selenium.util.DriverFactory;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GoogleSearchTest extends TestListener{

	private GoogleHomePage googleHomePage;
	
	@Given("a web browser is at the Google home page")
	public void a_web_browser_is_at_the_google_home_page() throws Exception {
	AbstractPage.setCaptureMode(true);
	  googleHomePage = new GoogleHomePage(DriverFactory.getDriver());	
	}

	@When("the user enters {string} into the search bar")
	public void the_user_enters_into_the_search_bar(String searchText) {
       googleHomePage.enterTextInSearchInput(searchText);
	}

	@Then("links related to {string} are shown on the results page")
	public void links_related_to_are_shown_on_the_results_page(String searchText) {
        List<String> searchResults = googleHomePage.getSearchResultsList();
         int totalResults = searchResults.size();
         for(int i =0 ; i<totalResults ; i++) {      
        	 SoftAssert softAssert = new SoftAssert();
        	 softAssert.assertTrue(searchResults.contains(searchText), "\"search results doesn't contains search text\"");
           //  SoftAssert.verifyTrue(searchResults.contains(searchText), "search results doesn't contains search text");
         }
        
	}

	@Given("Google search results for {string} are shown")
	public void google_search_results_for_are_shown(String string) throws Exception {
		AbstractPage.setCaptureMode(true);
		this.googleHomePage = new GoogleHomePage(DriverFactory.getDriver());
		 SoftAssert softAssert = new SoftAssert();
		 softAssert.assertTrue(googleHomePage.getSearchResultsList().size()>0, "No search Results Found");
	
	}

	@When("the user clicks on the {string} link at the top of the results page")
	public void the_user_clicks_on_the_link_at_the_top_of_the_results_page(String string) {
              googleHomePage.clickOnNthSearchResult(1);
	}

	@Then("images related to {string} are shown on the results page")
	public void images_related_to_are_shown_on_the_results_page(String string) {
		 SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(DriverFactory.getDriver().getTitle().contains(string), "search reults not showing result of "+ string);
	}
}
