package com.jt.selenium.elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

@Component
public class JTImage {

	@Autowired
	private JTCore jtCore;

	public void verifyImageSource(String locator, String src) {
		
		jtCore.waitForElementVisible(locator);
		String attribute = jtCore.getAttribute(locator, "src");

		if (attribute==null) {
			Assert.fail(locator+ " was not visible or present to perform this check");
		}
		else {
			Assert.assertTrue(attribute.endsWith(src));
		}
	}
}
