package com.jt.selenium.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

@Component
public class JTInput
{
	@Autowired
	protected JTCore jtCore;

	public void verifySelectedLabel(String locator, String value)
	{
		Assert.assertEquals(value, getSelectedLabel(locator));
	}

	String getSelectedLabel(String locator)
	{
		jtCore.waitForElementPresent(locator);
		return new Select(jtCore.getElement(locator)).getFirstSelectedOption().getText();

	}

	public void verifySelectedLabelContains(String locator, String value)
	{
		Assert.assertTrue(getSelectedLabel(locator).contains(value), "Selected value of \"" + locator + "\" is not " + value);
	}

	public void verifyChecked(String locator)
	{
		Assert.assertTrue(jtCore.getElement(locator).isSelected());
	}

	public void verifyNotChecked(String locator)
	{
		Assert.assertFalse(jtCore.getElement(locator).isSelected());
	}

	public void check(String locator)
	{
		jtCore.waitForElementPresent(locator);
		WebElement element = jtCore.getElement(locator);
		element.click();
	}

	public void uncheck(String locator)
	{
		jtCore.waitForElementPresent(locator);
		jtCore.getElement(locator).click();
//		locator = getLocator(locator);
//		waitForElementPresent(locator);
//		getSelenium().uncheck(locator);
	}

	public void attachFile(String locator, String fileUrl)
	{
		jtCore.waitForElementPresent(locator);
		type(locator, fileUrl);
	}

	public void type(String locator, String value)
	{
		jtCore.waitForElementPresent(locator);
		WebElement element = jtCore.getElement(locator);
		element.clear();
		element.sendKeys(value);
	}


	public void verifyInputValue(String locator, String value)
	{
		Assert.assertEquals(getInputValue(locator), value, String.format("Input value is not \"%s\" as expected.", value));
	}

	public void verifyInputValue(String locator, String value, String msg)
	{
		Assert.assertEquals(getInputValue(locator), value, String.format(msg, value));
	}

	public String getInputValue(String locator)
	{
		return jtCore.getAttribute(locator, "value");
	}

	public void select(String locator, String value) {
		jtCore.waitForElementPresent(locator);
		WebElement element = jtCore.getElement(locator);
		Select selectBox = new Select(element);
		selectBox.selectByValue(value);
		
	}

	public String getValue(String locator)
	{
		jtCore.waitForElementPresent(locator);
		return jtCore.getAttribute(locator, "value");
	}

	public void selectAndWait(String locator, String value)
	{
		select(locator, value);
		try
		{
			jtCore.waitForPageToLoad();
		}
		catch (Exception e)
		{
			Assert.fail("This method threw an exception while selecting " + value + " from " + locator + "." + e.getMessage());
		}
	}

	public void typeHidden(String locator, String value) {
		jtCore.waitForElementPresent(locator);
		jtCore.executeJavaScript(String.format("document.getElementById('%s').value = '%s';", locator, value));
	}
}
