package com.whistl.selenium.pages.smartwyre;

import java.awt.AWTException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.whistl.selenium.pages.AbstractPage;

public class DemoDragAndDropDemo extends AbstractPage {
	private DemoMenuList demoMenuList;
	private final static String DRAG_ELEMENT_FINDER = ".//*[@id='todrag']/span[text()='%s']";
	private final static By DRAGTO_FINDER = By.xpath(".//*[@id='mydropzone']");
	private final static By DRAGGED_LIST_FINDER = By.xpath(".//*[@id='droppedlist']/span");

	public DemoDragAndDropDemo(AbstractPage parentPage) throws Exception {
		super(parentPage);
		this.demoMenuList = new DemoMenuList(parentPage);
	}

	public DemoDragAndDropDemo(WebDriver driver) throws Exception {
		super(driver);
		this.demoMenuList = new DemoMenuList(driver);
	}
	public void dragElementByName(String dragableElement) throws AWTException {

		WebElement fromElement  = waitForElement(By.xpath(String.format(DRAG_ELEMENT_FINDER, dragableElement))).getWebElement();
		WebElement toElement = waitForElement(DRAGTO_FINDER).getWebElement();

		Actions builder = new Actions(getDriver());
		
		builder. moveToElement(toElement). perform();

		builder.moveToElement(toElement)
        .pause(Duration.ofSeconds(1))
        .clickAndHold(fromElement)
        .pause(Duration.ofSeconds(1))
        .moveToElement(toElement)
        .moveToElement(toElement)
        .moveToElement(toElement)
        .pause(Duration.ofSeconds(1))
        .release().build().perform();
		waitForLoad(3000L);	
	}

	
	public List<String> getAllDraggedElement(){
		List<String> draggedElement = new ArrayList<String>();
		List<WebElement> draggedWebElements = getDriver().findElements(DRAGGED_LIST_FINDER);
		
		int totalDraggedElements = draggedWebElements.size();
		for(int i =0;i <totalDraggedElements;i++) {
			draggedElement.add(draggedWebElements.get(i).getText());
		}
		
		return draggedElement;
	}
	@Override
	public void resetActiveIFrame() {

	}

	@Override
	public void waitForLoad() throws Exception {
waitForElement(DRAGTO_FINDER, WAIT_TIME_LIMIT);
	}

	public DemoMenuList getDemoMenuList() {
		return demoMenuList;
	}

}
