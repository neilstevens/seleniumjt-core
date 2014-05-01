package com.jt.selenium.elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.jt.selenium.configuration.SeleniumConfiguration;
import com.jt.selenium.utils.JTContainer;
import com.jt.selenium.utils.LogExecTime;
import com.thoughtworks.selenium.SeleniumException;

@Component
public class JTCore {

	@Autowired
	JTContainer jtContainer;
	
	public JTContainer getJtContainer() {
		return jtContainer;
	}

	protected WebDriver getWebDriver() {
		return jtContainer.getWebDriver();
	}

	protected SeleniumConfiguration getConfiguration() {
		return jtContainer.getConfiguration();
	}

	protected int getCommandRepeatMils() {
		return getConfiguration().getCommandRepeatMils();
	}

	protected int getRetryAttempts() {
		return getConfiguration().getRetryAttempts();
	}

	public void validatePage(String identifier) {
		checkServiceAvailability();
		verifyNoJavaScriptErrorOnPage();
		verifyPageLoadedCorrectly();
	}


	public void screenShot(String fileName) throws IOException {
		if (!StringUtils.endsWith(fileName, ".gif")) {
			fileName+=".gif";
		}
		File scrFile = ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.FILE);
		String pathname = getConfiguration().get("screenshot.loc");
		if (pathname==null) {
			throw new IllegalStateException("You need to specify a location to save your screenshots. Set 'screenshot.loc' as a preference");
		}
		FileUtils.copyFile(scrFile, new File(pathname+fileName));
	}

	public Object executeJavaScript(String script) {
		JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
		return executor.executeScript(script);
	}

	private void verifyPageLoadedCorrectly() {
		String htmlSource = StringUtils.lowerCase(getWebDriver().getPageSource());
		boolean contains = StringUtils.contains(htmlSource, "</body>");
		Assert.assertTrue(contains, "The page did not load properly - no ending body tag was found");
	}

	public void checkServiceAvailability() {
		String message = getConfiguration().getPropertyAsString("service.unavailable.message");
		if (!StringUtils.isEmpty(message)) {
			boolean present = false;
			try {
				present = isTextPresent(message);
			} catch (SeleniumException s) {
				int tries = 0;
				while (!present && tries++ < getRetryAttempts()) {
					try {
						present = isTextPresent(message);
					} catch (SeleniumException se) {
						pause(getCommandRepeatMils());
					}
				}
			}
			Assert.assertFalse(present, "Service is temporarily down");
		}
	}

	public void verifyNoJavaScriptErrorOnPage() {
		String jsErrorId = "js_error";
		boolean elementPresent = isElementPresent(jsErrorId);

		if (elementPresent) {
			try {
				String message = getText(jsErrorId);

				// this code doesn't currently ignore any errors since the code
				// inside these files (which originate these errors) both do so:
				String urlSpan = "";
				String msgSpan = "";
				String lineSpan = "";
				try {
					urlSpan = getText("js_error_span_url");
					msgSpan = getText("js_error_span_msg");
					lineSpan = getText("js_error_span_line");
				} catch (SeleniumException se) {
					// Some problem occurrred - testing the entire content of
					// the js_error div
					msgSpan = message;
				}
				if (!ignorableJsErrors(msgSpan, lineSpan, urlSpan)) {
					Assert.fail(message);
				}
			} catch (SeleniumException se) {
				// Failed as trying to getText when not visible - this is ok, we
				// can ignore... the error does not exist.
			}
		}
	}

	public void waitForPageToLoad() {
		waitHere().until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
		validatePage("waitForPageToLoad() method");
		windowFocus();
	}

	public void windowFocus() {
		if (runningInIE() && isElementPresent("tagname=body")) {
			getElement("tagname=body").click();
		}
	}

	private boolean ignorableJsErrors(String message, String line, String url) {
		String list = getConfiguration().getPropertyAsString("javascript.ignore.list");
		if (list != null) {
			String[] ignores = StringUtils.split(list, ',');
			for (String ignore : ignores) {
				if (StringUtils.contains(url, StringUtils.trim(ignore))) {
					return true;
				}
			}
		}
		return false;
	}

	public void open(String url) throws IOException {
		getWebDriver().get(getConfiguration().getTargetWebsite() + url);
		windowFocus();
		validatePage(url);
	}


	/**
	 * @deprecated Frames no longer suported in HTML5
	 * 
	 * @param targetFrame
	 */
	public void returnToFrameParent() {
		getWebDriver().switchTo().frame("relative=up");
	}

	/**
	 * @deprecated Frames no longer suported in HTML5
	 * 
	 * @param targetFrame
	 */
	public void selectFrame(String targetFrame) {
		waitHere().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(targetFrame));
	}

	public void verifyElementTextNotEqualTo(String locator, String value) {
		Assert.assertFalse(value.equalsIgnoreCase(getText(locator)), "Values are the same. They should be different ");

	}

	@LogExecTime
	public void waitForElementPresentAndVisible(String locator) {
		if (!isVisible(locator)) {
			// an 'Exception' to the rule - if not visible then wait...
			waitForElementPresent(locator);
			waitForElementVisible(locator);
		}
	}

	public void verifyOccurrencesInElement(String locator, String regex, int occurs) {
		int actual = StringUtils.countMatches(getText(locator), regex);
		Assert.assertEquals(actual, occurs,
				String.format("Expected %s occurrences of %s in %s but there were %s", occurs, regex, locator, actual));
	}

	public int getOccurrences(String regex) {
		return StringUtils.countMatches(getElementByTagName("body").getText(), regex);
	}

	private WebElement getElement(By by) {
		WebElement findElement = getWebDriver().findElement(by);
		return findElement;
	}

	public WebElement getElementByTagName(String locator) {
		return getElement(By.tagName(locator));
	}

	public WebElement getElement(String locator) {
		return getElement(LocatorStrategy.by(locator));
	}

	public void blur(String locator) {
		fireEvent(locator, "blur");
	}

	@LogExecTime
	public boolean isVisible(String locator) {
		return getElement(locator).isDisplayed();
	}

	@LogExecTime
	public void pause(int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
			System.err.print("You interrupted me");
		}

	}

	public void verifyElementText(String locator, String value, String message) {
		getElement(locator).getText();
		String actualValue = getText(locator);
		int tries = 0;
		while (!actualValue.equals(value) && tries++ < getRetryAttempts()) {
			// present and visible already checked for - just check the text
			// value...
			try {
				actualValue = getText(locator);
			} catch (SeleniumException se) {
				// oops - something went wrong. Never mind, lets keep trying
			}
			pause(getCommandRepeatMils());
		}
		Assert.assertEquals(actualValue, value, "The values don't match");
	}

	@LogExecTime
	public void waitForText(String text) {
		waitHere().until(ExpectedConditions.textToBePresentInElement(By.tagName("body"), text));
	}

	private WebDriverWait waitHere() {
		return new WebDriverWait(getWebDriver(), jtContainer.getConfiguration().getWaitingPeriod());
	}

	public void verifyTitle(String title) {
		Assert.assertEquals(getWebDriver().getTitle(), title, String.format("Title is not \"%s\"", title));
	}

	public String getHtmlSource() {
		return getWebDriver().getPageSource();
	}

	public void verifyElementNotContains(String locator, String value) {
		String actualValue = fastCheckOrWait(locator);
		int tries = 0;
		while (StringUtils.contains(actualValue, value) && tries++ < getRetryAttempts()) {
			try {
				actualValue = getText(locator);
			} catch (Exception e) {
				// Ignore... sometimes an element can disappear between the
				// previous checks.
			}

			pause(getCommandRepeatMils());
		}
		Assert.assertTrue(!StringUtils.contains(getText(locator), value),
				String.format("Value \"%s\" is not expected but is present", value));
	}

	public void verifyElementContains(String locator, String value) {
		String actualValue = fastCheckOrWait(locator);
		int tries = 0;
		while (!StringUtils.contains(actualValue, value) && tries++ < getRetryAttempts()) {
			try {
				actualValue = getText(locator);
			} catch (Exception e) {
				// Ignore... sometimes an element can disappear between the
				// previous checks.
			}

			pause(getCommandRepeatMils());
		}
		Assert.assertTrue(StringUtils.contains(getText(locator), value),
				String.format("Value \"%s\" is expected but is not present", value));
	}

	String fastCheckOrWait(String locator) {
		if (isVisible(locator)) {
			String text = getText(locator);
			return text == null ? "" : text;
		} else {
			waitForElementPresentAndVisible(locator);
		}
		String text = getText(locator);
		return text == null ? "" : text;
	}

	public void verifyElementPresent(String locator) {
		// this method doesn't care about visibility
		try {
			// attempt fast assertion first
			if (isElementPresent(locator)) {
				Assert.assertTrue(true);
			} else {
				waitForElementPresent(locator);
			}
		} catch (SeleniumException e) {
			waitForElementPresent(locator);
		}
		Assert.assertTrue(isElementPresent(locator),
				String.format("Element \"%s\" is expected but is not present", locator));
	}

	public void verifyTextNotPresent(String text, String message, boolean instantFail) {
		int tries = 0;
		if (instantFail && isTextPresent(text)) {
			Assert.fail(message);
		}
		while (isTextPresent(text) && tries++ < getRetryAttempts()) {
			pause(getCommandRepeatMils());
		}
		Assert.assertFalse(isTextPresent(text), message);
	}

	public void debugPause(int seconds, String message) {
		System.out.println("####### " + message + " #######");
		pause(seconds * 1000);
	}

	public String getText(String locator) {
		return getElement(locator).getText();
	}

	public String getAttribute(String locator, String attribute) {
		return getElement(locator).getAttribute(attribute);
	}

	public void verifyAttribute(String locator, String attribute, String value) {
		String attribute2 = getAttribute(locator, attribute);
		Assert.assertEquals(value, attribute2);
	}

	@LogExecTime
	public void waitForElementVisible(String locator) {
		waitHere().until(ExpectedConditions.visibilityOfElementLocated(LocatorStrategy.by(locator)));
	}

	public void waitForTitle(String title) {
		waitHere().until(ExpectedConditions.titleIs(title));
	}

	public void waitForTitleToContain(String text) {
		waitHere().until(ExpectedConditions.titleContains(text));
	}

	@LogExecTime
	public void waitForElementText(String locator, String text) {
		waitHere().until(ExpectedConditions.textToBePresentInElement(LocatorStrategy.by(locator), text));
	}

	@LogExecTime
	public void waitForValueInElement(String locator, String text) {
		waitHere().until(ExpectedConditions.textToBePresentInElementValue(LocatorStrategy.by(locator), text));
	}

	public void waitForAlert() {
		waitHere().until(ExpectedConditions.alertIsPresent());
	}
	
	public void closeAlert() {
		getWebDriver().switchTo().alert().accept();
	}

	public void verifyAlert(String regex) {
		waitForAlert();
		Assert.assertTrue(getWebDriver().switchTo().alert().getText().equals(regex));
		closeAlert();
	}

	@LogExecTime
	public void waitForElementPresent(String locator) {
		waitHere().until(ExpectedConditions.presenceOfElementLocated(LocatorStrategy.by(locator)));
	}

	@LogExecTime
	public boolean isElementPresent(String locator) {
		try {
			getElement(locator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	// public void waitForAjax() throws InterruptedException
	// {
	// // The docs say to use .getCurrentWindow but that method does not exist!
	// // using the .currentWindow property (which is a reference to the
	// application window) works!
	// // but not in FF4?
	// getSelenium().waitForCondition("selenium.browserbot.getCurrentWindow().jQuery.active == 0;",
	// Integer.toString(getConfiguration().getAjaxTimeout()));
	// }

	public void verifyTextPresent(String text) {
		long start = System.currentTimeMillis();
		int tries = 0;
		String textRegexed = text;
		while (!isTextPresent(textRegexed) && tries++ < getRetryAttempts()) {
			pause(getCommandRepeatMils());
		}
		long durationMills = System.currentTimeMillis() - start;
		Assert.assertTrue(isTextPresent(textRegexed), String.format("Text \"%s\" is expected but is not present after " + durationMills
				+ " milliseconds.", text));
	}

	public void verifyTextPresent(String text, String msg) {
		int tries = 0;
		while (!isTextPresent(text) && tries++ < getRetryAttempts()) {
			pause(getCommandRepeatMils());
		}
		Assert.assertTrue(isTextPresent(text), msg);
	}

	public void verifyElementVisible(String locator) {
		long start = System.currentTimeMillis();
		waitForElementPresentAndVisible(locator);
		long durationMills = System.currentTimeMillis() - start;
		Assert.assertTrue(
				isVisible(locator),
				String.format("Element \"%s\" is expected to be visible but is still hidden after " + durationMills
						+ " milliseconds.", locator));
	}

	public void verifyElementNotVisible(String locator) {
		waitForElementPresent(locator);

		// need to reverse the logic contained in
		// waitForElementPresentAndVisible() to only wait while it's still
		// visible (instead of waiting while it's not yet visible)
		long start = System.currentTimeMillis();
		int tries = 0;
		try {
			while (isVisible(locator) && tries++ < getRetryAttempts()) {
				pause(getCommandRepeatMils());
			}
		} catch (SeleniumException se) {
			// isVisible will throw an exception if the element is NOT visible -
			// so catch this exception and pass it
			Assert.assertTrue(true);
		}
		long durationMills = System.currentTimeMillis() - start;
		Assert.assertFalse(
				isVisible(locator),
				String.format("Element \"%s\" is expected to be hidden but is still visible after " + durationMills
						+ " milliseconds.", locator));
	}

	@LogExecTime
	public void waitForElementNotVisible(String locator) {
		boolean visible = isVisible(locator);
		long start = System.currentTimeMillis();
		int tries = 0;
		try {
			// wait for a while for it to appear in the DOM before giving up
			// quietly
			while (visible && tries++ < getRetryAttempts()) {
				visible = isVisible(locator);
				pause(getCommandRepeatMils());
			}
		} catch (Exception e) {
			long durationMills = System.currentTimeMillis() - start;
			Assert.fail("This method threw an exception after " + durationMills
					+ " milliseconds whilst waiting for element " + locator + " to be removed.");
		}
		if (visible) {
			long durationMills = System.currentTimeMillis() - start;
			Assert.fail(String.format("Element %s is still visible after " + durationMills + " milliseconds.", locator));
		}
	}

	@LogExecTime
	public void waitForElementNotPresent(String locator) {
		boolean present = isElementPresent(locator);
		long start = System.currentTimeMillis();
		int tries = 0;
		try {
			// wait for a while for it to appear in the DOM before giving up
			// quietly
			while (present && tries++ < getRetryAttempts()) {
				present = isElementPresent(locator);
				pause(getCommandRepeatMils());
			}
		} catch (Exception e) {
			long durationMills = System.currentTimeMillis() - start;
			Assert.fail("This method threw an exception after " + durationMills
					+ " milliseconds whilst waiting for element " + locator + " to be removed.");
		}
		if (present) {
			long durationMills = System.currentTimeMillis() - start;
			Assert.fail(String.format("Element %s is still present after " + durationMills + " milliseconds.", locator));
		}
	}

	public void verifyElementNotPresent(String locator) {
		int tries = 0;
		long start = System.currentTimeMillis();
		while (isElementPresent(locator) && tries++ < getRetryAttempts()) {
			pause(getCommandRepeatMils());
		}
		long durationMills = System.currentTimeMillis() - start;
		Assert.assertFalse(
				isElementPresent(locator),
				String.format("Element \"%s\" is NOT expected but is still present after " + durationMills
						+ " milliseconds.", locator));
	}

	public boolean isTextPresent(String text) {
		return getWebDriver().getPageSource().contains(text);
	}

	public String getLocation() {
		return getWebDriver().getCurrentUrl();
	}

	public boolean runningInIE() {
		return getConfiguration().getBrowserType().runningInIE();
	}

	public boolean runningInChrome() {
		return getConfiguration().getBrowserType().runningInChrome();
	}

	public boolean runningInPhantom() {
		return getConfiguration().getBrowserType().runningInPhantom();
	}

	public boolean runningInFirefox() {
		return getConfiguration().getBrowserType().runningInFireFox();
	}

	public boolean runningInSafari() {
		return getConfiguration().getBrowserType().runningInSafari();
	}

	public void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * onChange event on selects are not always invoked within IE when using
	 * selenium... so we had to added a special JQuery handler to simulate the
	 * change using CSS and JQuery click. Why do we not simply do a
	 * selenium.fireEvent ?? ... it's because selenium does not recognise JQuery
	 * onChange event when inside IE so firing the event will still do
	 * nothing.... but it DOES recognise click event so we can invoke this
	 * 
	 * @param locator
	 */
	// public void simulateIEOnChangeEvent(String locator)
	// {
	// locator = jqueryfyLocator(locator);
	// if (runningInIE())
	// {
	// try
	// {
	// getSelenium().click(locator);
	// }
	// catch (SeleniumException se)
	// {
	// // the onChange event MAY have been invoked in which case this element
	// may not have been visible to
	// // click... in which case the select has worked and the click was not
	// necessary
	// }
	// }
	// }

	public void fireEvent(String locator, String eventName) {

		WebElement element = getElement(locator);
		JavascriptLibrary javascript = new JavascriptLibrary();
		
		if (runningInIE()) {
			String script = String.format("$('#%s').trigger('%s');", locator, eventName);
			javascript.executeScript(getWebDriver(), script);
		}
		else {
			javascript.callEmbeddedSelenium(getWebDriver(), "triggerEvent", element, eventName);
		}

	}
}
