package com.jt.selenium.elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;


@Component
public class JTCSS
{
	
	@Autowired
	private JTCore jtCore;
	

	public void verifyCssClass(String locator, String value)
	{
		jtCore.waitForElementPresent(locator);
		String aValue = "";
		if (jtCore.isVisible(locator))
		{
			aValue = jtCore.getAttribute(locator, "class");
		}
		else
		{
			// this doesn't wait for it to be visible to allow the CSS class of hidden things to be checked
			jtCore.waitForElementPresent(locator);
		}
		// Wait for value note this currently only copes with a vanilla ID attribute being passed in for the locator
		int tries = 0;
		while (!aValue.equals(value) && tries++ < jtCore.getRetryAttempts())
		{
			jtCore.pause(jtCore.getCommandRepeatMils());
			aValue = jtCore.getAttribute(locator, "class");
		}
		Assert.assertEquals(aValue, value, String.format("CSS class value for %s is not as expected. Expected %s, got %s ", locator, value, aValue));
	}
}
