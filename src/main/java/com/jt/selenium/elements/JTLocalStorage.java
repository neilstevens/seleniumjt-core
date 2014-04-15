package com.jt.selenium.elements;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.jt.selenium.utils.JTContainer;

@Component
public class JTLocalStorage
{

	@Autowired
	JTCore jtCore;
	
	private JavascriptExecutor js = null;
	
	public JavascriptExecutor getJavascriptExecutor() {
		if (js==null) {
			JTContainer jtContainer = jtCore.getJtContainer();
			WebDriver webDriver = jtContainer.getWebDriver();
			js = (JavascriptExecutor) webDriver;
		}
		return js;
	}


	public void removeItemFromLocalStorage(String item) {
		getJavascriptExecutor().executeScript(String.format("window.localStorage.removeItem('%s');", item));
	}

	public boolean isItemPresentInLocalStorage(String item) {
		if (getJavascriptExecutor().executeScript(String.format("return window.localStorage.getItem('%s');", item)) == null)
			return false;
		else
			return true;
	}

	public String getItemFromLocalStorage(String key) {
		return (String) getJavascriptExecutor().executeScript(String.format("return window.localStorage.getItem('%s');", key));
	}

	public String getKeyFromLocalStorage(int key) {
		return (String) getJavascriptExecutor().executeScript(String.format("return window.localStorage.key('%s');", key));
	}

	public Long getLocalStorageLength() {
		return (Long) getJavascriptExecutor().executeScript("return window.localStorage.length;");
	}

	public void setItemInLocalStorage(String item, String value) {
		getJavascriptExecutor().executeScript(String.format("window.localStorage.setItem('%s','%s');", item, value));
	}

	public void verifyLocalStorageValue(String key, String value) {
		String itemFromLocalStorage = getItemFromLocalStorage(key);
		Assert.assertEquals(itemFromLocalStorage, value);
	}

	public void clearLocalStorage() {
		getJavascriptExecutor().executeScript(String.format("window.localStorage.clear();"));
	}
}
