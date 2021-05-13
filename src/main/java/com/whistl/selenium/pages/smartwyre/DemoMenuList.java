package com.whistl.selenium.pages.smartwyre;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.util.RenewableWebElement;

public class DemoMenuList extends AbstractPage {
	
	
	public static final String EXPANDED_MAIN_MENU_FINDER_TEMPLATE = ".//li[*[text()='%s']]/i[contains(@class,'glyphicon-chevron-down')]";
	public static final String MAIN_MENU_HEADER_FINDER_TEMPLATE = ".//a[text()='%s']";
	public static final String CLOSED_MAIN_MENU_FINDER = ".//li[*[text()='%s']]/i[contains(@class,'glyphicon-chevron-right')]";

	
	
	
	public static enum MAIN_MENU {
		ALL_EXAMPLE("All Examples"), INPUT_FORM("Input Forms"), DATE_PICKERS(
				"Date pickers"), TABLE("Table"), PROGRESS_BAR_AND_SLIDERS(
				"Progress Bars & Sliders"), ALERT_AND_MODELS(
				"Alerts & Modals"),OTHERS("Others");

		public static final String EXPANDED_MAIN_MENU_FINDER_TEMPLATE = ".//li[*[text()='%s']]/i[contains(@class,'glyphicon-chevron-down')]";
		public static final String MAIN_MENU_HEADER_FINDER_TEMPLATE = ".//a[text()='%s']";
		public static final String CLOSED_MAIN_MENU_FINDER = ".//li[*[text()='%s']]/i[contains(@class,'glyphicon-chevron-right')]";
	
		
		By expanded_main_menu_finder;
		By closed_main_menu_finder;
		By main_menu_finder;
		String titleText;

		private MAIN_MENU(String menuText) {
			this.titleText = menuText;
			this.expanded_main_menu_finder = By.xpath(String.format(EXPANDED_MAIN_MENU_FINDER_TEMPLATE,
					menuText));
			this.closed_main_menu_finder= By.xpath(String.format(CLOSED_MAIN_MENU_FINDER, menuText));
			this.main_menu_finder = By.xpath(String.format(MAIN_MENU_HEADER_FINDER_TEMPLATE, menuText));
		}

		public By getExpandedMenuFinder() {
			return this.expanded_main_menu_finder;
		}

		public By getClosedMenuFinder() {
			return this.closed_main_menu_finder;
		}
		
		public By getMainMenuFinder() {
			return this.main_menu_finder;
		}
		public String getSelectedTitleText() {
			return this.titleText;
		}
	}
	
	
	public static enum SUB_MENU {
		SIMPLE_FORM_DEMO("Simple Form Demo"), SELECT_DROP_DOWN_LIST("Select Dropdown List"), INPUT_FORM_SUBMIT(
				"Input Form Submit"), BOOT_STRAP_DATE_PICKER("Bootstrap Date Picker"), TABLE_FILTER(
				"Table Filter"), DRAG_AND_DROP_SLIDERS("Drag & Drop Sliders"),ALERT_AND_MODELS("Alerts & Modals"),DRAG_AND_DROP("Drag and Drop");

		public static final String SUB_MENU_HEADER_FINDER_TEMPLATE = ".//*[@id='treemenu']//a[text()='%s']";
	
		
		
		By sub_menu_finder;
		String titleText;

		private SUB_MENU(String menuText) {
			this.titleText = menuText;
				this.sub_menu_finder = By.xpath(String.format(MAIN_MENU_HEADER_FINDER_TEMPLATE, menuText));
		}

		
		public By getSubMenuFinder() {
			return this.sub_menu_finder;
		}
		public String getSelectedTitleText() {
			return this.titleText;
		}
	}
	
	
	public void expand_all_example_header_menu(MAIN_MENU menu) {
		
		RenewableWebElement All_Example_Header = waitForElement(menu.main_menu_finder);
		waitForCondition(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
              if(isElementPresent(menu.closed_main_menu_finder)) {
            	  
            	  All_Example_Header.click();
            	  waitForLoad(3000L);
              }
			
              return  isElementPresent(menu.expanded_main_menu_finder);

			}
		}, WAIT_TIME_LIMIT);
		
		
	}
	
	
	public void navigate_to_sub_menu(SUB_MENU subMenu) {
		
		RenewableWebElement submenuEle = waitForElement(subMenu.getSubMenuFinder(),WAIT_TIME_LIMIT);
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		executor.executeScript("arguments[0].click();", submenuEle.getWebElement());
		waitForLoad(2000L);
	}
	
	public DemoMenuList(AbstractPage parentPage) throws Exception {
		super(parentPage);
	}

	
	public DemoMenuList(WebDriver driver) throws Exception {
		super(driver);
	}
	@Override
	public void resetActiveIFrame() {

	}

	@Override
	public void waitForLoad() throws Exception {

	}

}
