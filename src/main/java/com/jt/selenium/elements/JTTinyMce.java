package com.jt.selenium.elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JTTinyMce {

	@Autowired
	private JTCore jtCore;
	
	@Autowired
	private JTInput jtInput;

	public void typeTinyMceEditor(String locator, String value) {
		jtCore.waitForElementPresentAndVisible(locator + "_toolbar1");
		// Selects the locator iframe in a UI editor
		final String frame = locator + "_ifr";
		if (jtCore.isElementPresent(frame)) {
			jtCore.selectFrame(frame);
			jtInput.type("jquery=#tinymce", value);
			// Pops selenium out of the selected frame and back to the main
			// page.
			jtCore.returnToFrameParent();
		} else {
			jtInput.type(locator, value);
		}
	}

	public void verifyInputValueTinyMce(String locator, String value) {
		// Selects the target iframe in a UI editor
		final String frame = locator + "_ifr";
		if (jtCore.isElementPresent(frame)) {
			jtCore.selectFrame(frame);
			jtCore.verifyElementText("jquery=#tinymce", value, "TinyMCE Value is not "+value);
			// Pops selenium out of the selected frame and back to the main
			// page.
			jtCore.returnToFrameParent();
		} else {
			jtInput.type(locator, value);
		}
	}
}
