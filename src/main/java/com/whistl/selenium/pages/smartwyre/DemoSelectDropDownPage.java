package com.whistl.selenium.pages.smartwyre;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.whistl.selenium.pages.AbstractPage;

public class DemoSelectDropDownPage extends AbstractPage {
	private DemoMenuList demoMenuList;

	private final static By DROP_DOWN_LIST_HEADER = By.xpath(".//*[text()='Select List Demo']");
	private final static By DROP_DOWN_SELECT_FINDER = By.xpath(".//*[@id='select-demo']");

	private final static By DROP_DOWN_MULTI_SELECT_FINDER = By.xpath(".//*[@id='multi-select']");

	public String selectDayByValueFromDropDown(String option) {

		Select selectDay = new Select(waitForElement(DROP_DOWN_SELECT_FINDER, WAIT_TIME_LIMIT));
		selectDay.selectByValue(option);
		String selectedText = selectDay.getFirstSelectedOption().getText();
        return selectedText;
	}

	public String selectDayByIndexFromDropDown(int index) {

		Select selectDay = new Select(waitForElement(DROP_DOWN_SELECT_FINDER, WAIT_TIME_LIMIT));
		selectDay.selectByIndex(index);
		String selectedText = selectDay.getFirstSelectedOption().getText();
        return selectedText;
	}

	
	
	public List<String> selectMutipleCountries(List<String> countries){
		Select selectmultipleCountries = new Select(waitForElement(DROP_DOWN_MULTI_SELECT_FINDER, WAIT_TIME_LIMIT));

	    int countriesToSelect = countries.size();
		List<String> countriesSelected = new ArrayList<String>();
	    for(int i =0 ;i< countriesToSelect;i++) {
	    	
	    	selectmultipleCountries.selectByValue(countries.get(i));
	    }
		
	    List<WebElement> countriesSelectedElement = selectmultipleCountries.getAllSelectedOptions();
	    
	    for(WebElement countrySelectEle:countriesSelectedElement) {
	    	countriesSelected.add(countrySelectEle.getText());
	    }
	    
	    return countriesSelected;
	    
	    
	}
	public DemoSelectDropDownPage(AbstractPage parentPage) throws Exception {
		super(parentPage);
		this.demoMenuList = new DemoMenuList(parentPage);
	}
	
	public DemoSelectDropDownPage(WebDriver driver) throws Exception {
		super(driver);
		this.demoMenuList = new DemoMenuList(driver);

	}

	@Override
	public void resetActiveIFrame() {

	}

	@Override
	public void waitForLoad() throws Exception {
		waitForElement(DROP_DOWN_LIST_HEADER, WAIT_TIME_LIMIT);
	}

	public DemoMenuList getDemoMenuList() {
		return demoMenuList;
	}

}
