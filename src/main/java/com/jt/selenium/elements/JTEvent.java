package com.jt.selenium.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JTEvent
{

	@Autowired
	private JTCore jtCore;
	

	public void click(String locator)
	{
		clickNoValidation(locator);
		jtCore.verifyNoJavaScriptErrorOnPage();
	}

	public void clickNoValidation(String locator)
	{
		jtCore.waitForElementPresent(locator);
		WebElement element = jtCore.getElement(locator);
		element.click();
	}

	public void click(String locator, int pause)
	{
		click(locator);
		jtCore.pause(pause);
	}

	public void clickAndWait(String locator)
	{
		click(locator);
		jtCore.waitForPageToLoad();
	}

	public void selectWindow(String locator, int tries)
	{
		jtCore.waitForElementPresent(locator);
		jtCore.getWebDriver().switchTo().window(locator);
	}

	/*
	 * Drags the element locator to the element targetLocator
	 */
	public void dragAndDropToObject(String locator, String targetLocator)
	{
		jtCore.waitForElementPresent(locator);
		(new Actions(jtCore.getWebDriver())).dragAndDrop(jtCore.getElement(locator), jtCore.getElement(targetLocator)).perform();
	}

	/*
	 * Drags the element locator to the element targetLocator with a set pause after the event
	 */
	public void dragAndDropWithPause(String locator, String targetLocator, int pause)
	{
		dragAndDropToObject(locator, targetLocator);
		jtCore.pause(pause);
	}

	public void focus(String locator)
	{
		jtCore.waitForElementPresent(locator);
		jtCore.fireEvent(locator, "focus");
	}

	public void closeBrowser()
	{
		jtCore.getWebDriver().close();
	}

	public void restartBrowser()
	{
		throw new IllegalStateException("not implemented. Restarting the browser closes WebDriver sessions. You should restart the browser by starting a new test");

	}
}
