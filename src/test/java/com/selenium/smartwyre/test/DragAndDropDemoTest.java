package com.selenium.smartwyre.test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.whistl.selenium.pages.AbstractPage;
import com.whistl.selenium.pages.smartwyre.DemoDragAndDropDemo;
import com.whistl.selenium.pages.smartwyre.DemoHomePage;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.MAIN_MENU;
import com.whistl.selenium.pages.smartwyre.DemoMenuList.SUB_MENU;
import com.whistl.selenium.test.AbstractTest;
import com.whistl.selenium.test.Logger;

/**
 * This class demonstrate the drag and drop feature
 * @author sobhajha
 *
 */
public class DragAndDropDemoTest extends AbstractTest {

	private DemoHomePage demoHomePage;
	private DemoDragAndDropDemo demoDragAndDropDemo;

	
	/**
	 * This test will drag the elements and drop it into dropbox.
	 * And also validates the elements dragged must be same as elements meant to be dragged
	 * @param draggableElement, elements which need to be dragged
	 * @throws Exception
	 */
	@Parameters({"draggableElement"})
	@Test
	public void  dragDropTest(String draggableElement) throws Exception {
		AbstractPage.setCaptureMode(true);
		this.demoHomePage = new DemoHomePage(getDriver());
		this.demoHomePage.clickkNoThanks();

		// @When("User expands input form menu")
		this.demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.ALL_EXAMPLE);
		this.demoHomePage.getDemoMenuList().expand_all_example_header_menu(MAIN_MENU.OTHERS);

       //@When("click on simple form demo")

		this.demoHomePage.getDemoMenuList().navigate_to_sub_menu(SUB_MENU.DRAG_AND_DROP);
		this.demoDragAndDropDemo = new DemoDragAndDropDemo(getDriver());
		
		//String[] draggable
		this.demoDragAndDropDemo.dragElementByName("Draggable 1");
		this.demoDragAndDropDemo.dragElementByName("Draggable 3");
		Logger.logStepResult(this.demoDragAndDropDemo.getAllDraggedElement().toString(), true);
	}
}
