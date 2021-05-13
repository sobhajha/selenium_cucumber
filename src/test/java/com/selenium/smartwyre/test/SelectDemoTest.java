package com.selenium.smartwyre.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.pages.smartwyre.DemoHomePage;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.MAIN_MENU;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.SUB_MENU;
import com.whistl.selenium.pages.smartwyre.DemoSelectDropDownPage;
import com.whistl.selenium.test.AbstractTest;
import com.whistl.selenium.test.evaluation.SoftAssert;

/**
 * This class will demo select functionality of the selenium
 * @author sobhajha
 *
 */
public class SelectDemoTest extends AbstractTest {

	
	private DemoHomePage demoHomePage;
	private DemoSelectDropDownPage demoSelectDropDownPage;

	/**
	 * This test will select the day from the dropdown menu by value.
	 * And also validates if the selected value is same as the {@day}
	 * @param day to be selected from the dropdown
	 * @throws Exception
	 */
	@Parameters({"day"})
	@Test
	public void  selectDayByValue(String day) throws Exception {
		AbstractPage.setCaptureMode(true);
		this.demoHomePage = new DemoHomePage(getDriver());
		this.demoHomePage.clickkNoThanks();

		// @When("User expands input form menu")
		this.demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.ALL_EXAMPLE);
		this.demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.INPUT_FORM);

       //@When("click on simple form demo")

		this.demoHomePage.getDemoMenuList().navigate_to_sub_menu(SUB_MENU.SELECT_DROP_DOWN_LIST);
		this.demoSelectDropDownPage = new DemoSelectDropDownPage(getDriver());
		
		String selectedDay = this.demoSelectDropDownPage.selectDayByValueFromDropDown(day);
		
		SoftAssert.verifyEquals(day,selectedDay, "Day select is ["+ selectedDay + "] Day expected to be selected is [" + day + " ]");
	}
	
	/**
	 * This test will select the day from the dropdown menu by index.
	 * And also validates if the selected value is same as the {@day}
	 * @param dayIndex to be selected from the dropdown
	 * @throws Exception
	 */
	@Parameters({"dayIndex"})
	@Test(dependsOnMethods = {"selectDayByValue"})
	public void  selectDayByIndex(String dayIndex) throws Exception {
    this.demoSelectDropDownPage = new DemoSelectDropDownPage(getDriver());
		
		String selectedDay = this.demoSelectDropDownPage.selectDayByIndexFromDropDown(Integer.valueOf(dayIndex));
		
		SoftAssert.verifyTrue(selectedDay!=null, "Day select is ["+ selectedDay + "] ");
	}

	/**
	 * This test will select  multiple value from the dropdown menu .
	 * And also validates if the selected value is same as the {@countries}
	 * @param countries to be selected from the dropdown
	 * @throws Exception
	 */
	@Parameters({"countries"})
	@Test(dependsOnMethods = {"selectDayByValue"})
	public void  multipleSelectCounrtiesDemo(String countries) throws Exception {
    this.demoSelectDropDownPage = new DemoSelectDropDownPage(getDriver());
		
	List<String> countriesToSelect = new ArrayList<String>();	
    String counties[] = countries.split(",");
    for (String country : counties) {
    	countriesToSelect.add(country);
    }
    
    List<String> coutriesSelected = this.demoSelectDropDownPage.selectMutipleCountries(countriesToSelect);
    SoftAssert.verifyTrue(coutriesSelected.containsAll(countriesToSelect),"Countries selected are [" +  coutriesSelected + "] countries to select are [" + countriesToSelect + "]");
	}

}
