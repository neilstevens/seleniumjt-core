package com.jt.selenium;

/**
 * This class provides wrappers to the basic selenium class. It should not contain any project specific functionality. This class should be
 * extended further to incorporate custom methods.
 *
 * @author Dave Hampton 10/01/2009
 */

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.jt.selenium.configuration.SeleniumConfiguration;
import com.jt.selenium.elements.JTCSS;
import com.jt.selenium.elements.JTCore;
import com.jt.selenium.elements.JTEvent;
import com.jt.selenium.elements.JTImage;
import com.jt.selenium.elements.JTInput;
import com.jt.selenium.elements.JTLocalStorage;
import com.jt.selenium.elements.JTTinyMce;
import com.jt.selenium.utils.LogExecTime;

@Component
public class SeleniumJT
{
	
	/**
	 * This will save your screen shots to a location specified by the property 'screenshot.loc'
	 * 
	 * @param fileName Should be a gif. If '.gif' is not added to the filename it will be changed.
	 * 
	 * @throws IOException
	 */
	public void screenShot(String fileName) throws IOException {
		jtCore.screenShot(fileName);
	}

	public void verifyLocalStorageValue(String key, String value) {
		localStorage.verifyLocalStorageValue(key, value);
	}

	public void closeAlert() {
		jtCore.closeAlert();
	}

	public Properties testData;

	@Autowired
	private JTCore jtCore;

	@Autowired
	private JTCSS jtCSS;
	
	@Autowired
	private JTEvent jtEvent;
	
	@Autowired
	private JTImage jtImage;
	
	@Autowired
	private JTInput jtInput;
	
	@Autowired
	private JTTinyMce jtTinyMce;
	
	@Autowired
	private JTLocalStorage localStorage;
	
	@LogExecTime
	public WebElement getElementByTagName(String locator) {
		return jtCore.getElementByTagName(locator);
	}

	@LogExecTime
	public void waitForValueInElement(String locator, String text) {
		jtCore.waitForValueInElement(locator, text);
	}

	@LogExecTime
	public WebElement getElement(String locator) {
		return jtCore.getElement(locator);
	}

	@LogExecTime
	public void waitForAlert() {
		jtCore.waitForAlert();
	}
	
	public void setTestData(Properties testData) {
		this.testData = testData;
	}
	
	public void setWebDriver(WebDriver driver) {
		jtCore.getJtContainer().setDriver(driver);
	}
	
	public void removeItemFromLocalStorage(String item) {
		localStorage.removeItemFromLocalStorage(item);
	}

	public boolean isItemPresentInLocalStorage(String item) {
		return localStorage.isItemPresentInLocalStorage(item);
	}

	public String getItemFromLocalStorage(String key) {
		return localStorage.getItemFromLocalStorage(key);
	}

	public String getKeyFromLocalStorage(int key) {
		return localStorage.getKeyFromLocalStorage(key);
	}

	public Long getLocalStorageLength() {
		return localStorage.getLocalStorageLength();
	}

	public void setItemInLocalStorage(String item, String value) {
		localStorage.setItemInLocalStorage(item, value);
	}

	public void clearLocalStorage() {
		localStorage.clearLocalStorage();
	}

	public WebDriver getWebDriver() {
		return jtCore.getJtContainer().getWebDriver();
	}
	
	@LogExecTime
	public void executeJavaScript(String script) {
		jtCore.executeJavaScript(script);
	}
	
	public void runJavaScript(String script) {
		jtCore.executeJavaScript(script);
	}


	@LogExecTime
	public void windowFocus() {
		jtCore.windowFocus();
	}
	
	public void stop() {
		jtCore.getJtContainer().stop();
	}

	public SeleniumConfiguration getConfiguration()
	{
		return jtCore.getJtContainer().getConfiguration();
	}

	public void setConfiguration(SeleniumConfiguration configuration)
	{
		jtCore.getJtContainer().setConfiguration(configuration);
	}

	/**
	 * Verify that the src of the image [identified by it's id] is correct
	 * 
	 * @param locator
	 * @param src
	 */
	@LogExecTime
	public void verifyImageSource(String locator, String src)
	{
		jtImage.verifyImageSource(locator, src);
	}

	@LogExecTime
	public void verifySelectedLabel(String locator, String value)
	{
		jtInput.verifySelectedLabel(locator, value);
	}

	@LogExecTime
	public void verifySelectedLabelContains(String locator, String value)
	{
		jtInput.verifySelectedLabelContains(locator, value);
	}

	/**
	 * Make sure site is up and not being updated
	 */
	@LogExecTime
	public void checkServiceAvailability()
	{
		jtCore.checkServiceAvailability();
	}

	@LogExecTime
	public void verifyCssClass(String locator, String value)
	{
		jtCSS.verifyCssClass(locator, value);
	}

	/**
	 * Make sure no JavaScript error occurred. There is a special JavaScript function added to the code that outputs a div with the id js_error if an error has
	 * happened.
	 * 
	 * <pre>
	 * 	 	<SCRIPT> 
	 * 				window.onerror = function(msg, err_url, line) {
	 * 					document.write('<div id="js_error"><div id="js_error_head">A JavaScript error occurred on line: ' + line + '</div><div id="js_error_msg">Details: '+msg+'</div></div>'); 
	 * 				}
	 * 		</SCRIPT>
	 * </pre>
	 * 
	 **/
	@LogExecTime
	public void verifyNoJavaScriptErrorOnPage()
	{
		jtCore.verifyNoJavaScriptErrorOnPage();
	}

	@LogExecTime
	public void debugPause(int seconds, String message)
	{
		jtCore.debugPause(seconds, message);
	}

	@LogExecTime
	public void validatePage(String identifier)
	{
		jtCore.validatePage(identifier);
	}

	@LogExecTime
	public boolean isElementPresent(String locator)
	{
		return jtCore.isElementPresent(locator);
	}

	@LogExecTime
	public void verifyElementPresent(String locator)
	{
		jtCore.verifyElementPresent(locator);
	}

	/**
	 * Looks for a substring within the element
	 * 
	 * @param locator
	 * @param value
	 */
	@LogExecTime
	public void verifyElementContains(String locator, String value)
	{
		jtCore.verifyElementContains(locator, value);
	}

	@LogExecTime
	public void verifyElementNotContains(String locator, String value)
	{
		jtCore.verifyElementNotContains(locator, value);
	}

	@LogExecTime
	public void verifyChecked(String locator)
	{
		jtInput.verifyChecked(locator);
	}

	@LogExecTime
	public void verifyNotChecked(String locator)
	{
		jtInput.verifyNotChecked(locator);
	}

	@LogExecTime
	public String getHtmlSource()
	{
		return jtCore.getHtmlSource();
	}

	/**
	 * Verifies the value of text within the given element.
	 * 
	 * @param locator
	 *            the element.
	 * @param value
	 */
	@LogExecTime
	public void verifyElementText(String locator, String value)
	{
		verifyElementText(locator, value, "The values don't match");
	}

	/**
	 * Allow custom message to verifyElementText
	 */
	@LogExecTime
	public void verifyElementText(String locator, String value, String message)
	{
		jtCore.verifyElementText(locator, value, message);
	}

	@LogExecTime
	public void verifyElementTextNotEqualTo(String locator, String value)
	{
		jtCore.verifyElementTextNotEqualTo(locator, value);
	}

	@LogExecTime
	public void open(String url) throws IOException
	{
		jtCore.open(url);
	}

	@LogExecTime
	public void blur(String locator)
	{
		jtCore.blur(locator);
	}

	@LogExecTime
	public void verifyOccurrencesInElement(String locator, String regex, int occurs)
	{
		jtCore.verifyOccurrencesInElement(locator, regex, occurs);
	}

	@LogExecTime
	public int getOccurrences(String regex)
	{
		return jtCore.getOccurrences(regex);
	}

	/**
	 * Custom method for typing text into a tinyMce which is formed of an embedded iframe html page which we need to target and type into. After we have typed
	 * our value we then need to exit the selected iFrame and return to the main page.
	 * 
	 * @param locator
	 * @param value
	 */
	@LogExecTime
	public void typeTinyMceEditor(String locator, String value)
	{
		jtTinyMce.typeTinyMceEditor(locator, value);
	}

	@LogExecTime
	public void verifyInputValueTinyMce(String locator, String value)
	{
		jtTinyMce.verifyInputValueTinyMce(locator, value);
	}

	/**
	 * 
	 * @param targetFrame
	 */
	@LogExecTime
	public void selectFrame(String targetFrame)
	{
		jtCore.selectFrame(targetFrame);
	}

	@LogExecTime
	public void waitForTitle(String title) {
		jtCore.waitForTitle(title);
	}

	@LogExecTime
	public void waitForTitleToContain(String text) {
		jtCore.waitForTitleToContain(text);
	}

	@LogExecTime
	public void waitForElementText(String locator, String text) {
		jtCore.waitForElementText(locator, text);
	}

	/**
	 * To be used in conjunction with selectFrame() to return to the parent frame.
	 */
	public void returnToFrameParent()
	{
		jtCore.returnToFrameParent();
	}

	/**
	 * After clicking something that invokes an Ajax event we need to wait for some value to return before we can continue.
	 * 
	 * @param text
	 *            The text to wait for
	 */
	@LogExecTime
	public void waitForText(String text)
	{
		jtCore.waitForText(text);
	}

	@LogExecTime
	public void click(String locator)
	{
		jtEvent.click(locator);
	}

	@LogExecTime
	public void clickNoValidation(String locator)
	{
		jtEvent.clickNoValidation(locator);
	}

	@LogExecTime
	public void click(String locator, int pause)
	{
		jtEvent.click(locator, pause);
	}

	@LogExecTime
	public void check(String locator)
	{
		jtInput.check(locator);
	}

	@LogExecTime
	public void uncheck(String locator)
	{
		jtInput.uncheck(locator);
	}

	/**
	 * Selenium's attachFile does not work - so I've written my own
	 */
	@LogExecTime
	public void attachFile(String locator, String fileUrl)
	{
		jtInput.attachFile(locator, fileUrl);
	}

	public void copy(File src, File dst) throws IOException
	{
		jtCore.copy(src, dst);
	}

	@LogExecTime
	public void verifyInputValue(String locator, String value)
	{
		jtInput.verifyInputValue(locator, value);
	}

	@LogExecTime
	public void verifyInputValue(String locator, String value, String msg)
	{
		jtInput.verifyInputValue(locator, value, msg);
	}

	@LogExecTime
	public void verifyAlert(String regex)
	{
		jtCore.verifyAlert(regex);
	}

	@LogExecTime
	public void pause(int millisecs)
	{
		jtCore.pause(millisecs);
	}

	@LogExecTime
	public void select(String locator, String value)
	{
		jtInput.select(locator, value);
	}

	@LogExecTime
	public void selectAndWait(String locator, String value)
	{
		jtInput.selectAndWait(locator, value);
	}

	@LogExecTime
	public String getInputValue(String locator)
	{
		return jtInput.getInputValue(locator);
	}

	@LogExecTime
	public void type(String locator, String value)
	{
		jtInput.type(locator, value);
	}

	@LogExecTime
	public void verifyTitle(String title)
	{
		jtCore.verifyTitle(title);
	}

	/**
	 * If element is not on the screen then selenium will try 20 times, twice a second for 10 seconds before finally declaring a failure
	 * 
	 * @param locator
	 */
	@LogExecTime
	public void clickAndWait(String locator)
	{
		jtEvent.clickAndWait(locator);
	}

	@LogExecTime
	public void clickAndWaitForAjax(String locator) throws InterruptedException
	{
		throw new IllegalStateException("Not yet implemented");
		//jtEvent.clickAndWaitForAjax(locator);
	}

	/**
	 * This text should appear on the screen.
	 * 
	 * If text is not on the screen then selenium will try 20 times, twice a second for 10 seconds before finally declaring a failure
	 * 
	 * @param text
	 */
	@LogExecTime
	public void verifyTextPresent(String text)
	{
		jtCore.verifyTextPresent(text);
	}

	/**
	 * This text should appear on the screen
	 * 
	 * @param text
	 */
	@LogExecTime
	public void verifyTextPresent(String text, String msg)
	{
		jtCore.verifyTextPresent(text, msg);
	}

	@LogExecTime
	public void verifyTextNotPresent(String text)
	{
		verifyTextNotPresent(text, String.format("Text \"%s\" is NOT expected but is present", text), false);
	}

	@LogExecTime
	public void verifyTextNotPresentInstantFail(String text)
	{
		verifyTextNotPresent(text, String.format("Text \"%s\" is NOT expected but is present", text), true);
	}

	@LogExecTime
	public void verifyTextNotPresent(String text, String message, boolean instantFail)
	{
		jtCore.verifyTextNotPresent(text, message, instantFail);
	}

	@LogExecTime
	public void verifyAttribute(String locator, String attribute, String value) {
		jtCore.verifyAttribute(locator, attribute, value);
	}

	@LogExecTime
	public boolean isTextPresent(String text)
	{
		return jtCore.isTextPresent(text);
	}

	@LogExecTime
	public String getText(String locator)
	{
		return jtCore.getText(locator);
	}

	@LogExecTime
	public String getLocation()
	{
		return jtCore.getLocation();
	}

	@LogExecTime
	public String getValue(String locator)
	{
		return jtInput.getValue(locator);
	}

	@LogExecTime
	public String getAttribute(String locator, String attribute)
	{
		return jtCore.getAttribute(locator, attribute);
	}

	@LogExecTime
	public void verifyElementNotPresent(String locator)
	{
		jtCore.verifyElementNotPresent(locator);
	}

	@LogExecTime
	public void verifyElementVisible(String locator)
	{
		jtCore.verifyElementVisible(locator);
	}

	@LogExecTime
	public void verifyElementNotVisible(String locator)
	{
		jtCore.verifyElementNotVisible(locator);
	}

	@LogExecTime
	public void closeBrowser()
	{
		jtEvent.closeBrowser();
	}

	@LogExecTime
	public void restartBrowser()
	{
		jtEvent.restartBrowser();
	}

	@LogExecTime
	public void waitForElementPresentAndVisible(String locator)
	{
		jtCore.waitForElementPresentAndVisible(locator);
	}

	@LogExecTime
	public void waitForElementVisible(String locator)
	{
		jtCore.waitForElementVisible(locator);
	}

	@LogExecTime
	public void waitForElementNotVisible(String locator)
	{
		jtCore.waitForElementNotVisible(locator);
	}

	@LogExecTime
	public void waitForElementPresent(String locator)
	{
		jtCore.waitForElementPresent(locator);
	}

	@LogExecTime
	public void waitForElementNotPresent(String locator)
	{
		jtCore.waitForElementNotPresent(locator);
	}

	@LogExecTime
	public void focus(String locator)
	{
		jtEvent.focus(locator);
	}

	@LogExecTime
	public boolean isVisible(String locator)
	{
		return jtCore.isVisible(locator);
	}

	@LogExecTime
	public boolean runningInIE()
	{
		return jtCore.runningInIE();
	}

	@LogExecTime
	public boolean runningInPhantom()
	{
		return jtCore.runningInPhantom();
	}

	@LogExecTime
	public boolean runningInChrome()
	{
		return jtCore.runningInChrome();
	}

	@LogExecTime
	public boolean runningInFirefox()
	{
		return jtCore.runningInFirefox();
	}

	@LogExecTime
	public boolean runningInSafari()
	{
		return jtCore.runningInSafari();
	}

	@LogExecTime
	public boolean attachmentCompatibleBrowser()
	{
		return jtCore.runningInFirefox();
	}

	@LogExecTime
	public void causeFailure(String message)
	{
		Assert.fail(message);
	}

	/*
	 * Drags the element locator to the element targetLocator
	 */
	@LogExecTime
	public void dragAndDropToObject(String locator, String targetLocator)
	{
		jtEvent.dragAndDropToObject(locator, targetLocator);
	}

	/*
	 * Drags the element locator to the element targetLocator with a set pause after the event
	 */
	@LogExecTime
	public void dragAndDropWithPause(String locator, String targetLocator, int pause)
	{
		jtEvent.dragAndDropWithPause(locator, targetLocator, pause);
	}

	@LogExecTime
	private void selectWindow(String locator, int tries)
	{
		// This whole mess is the only way i can seem to get selenium to wait for the popup to be present
		// the in-built waitForPopUp just will not play nice
		jtEvent.selectWindow(locator, tries);
	}

	@LogExecTime
	public void selectWindow(String locator)
	{
		selectWindow(locator, 0);
	}

	/**
	 * This requires a jquery locator or an ID. The leading hash (#) should not be provided to this method if it is just an ID
	 */
	@LogExecTime
	public void fireEvent(String locator, String eventName)
	{
		jtCore.fireEvent(locator, eventName);
	}

//	public void waitForAjax() throws InterruptedException
//	{
//		jtCore.waitForAjax();
//	}

	@LogExecTime
	public void typeHidden(String locator, String value) {
		jtInput.typeHidden(locator, value);
	}

	@LogExecTime
	public String data(String key) {
		return (String) testData.get(key);
	}

}
