package com.jt.selenium.elements;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

public class LocatorStrategy {
	
	public static By by(String locator) {
		if (idLocator(locator)) {
			return By.id(stripTag(locator));
		}
		if (cssLocator(locator)) {
			return By.cssSelector(stripTag(locator));
		}
		if (xpathLocator(locator)) {
			return By.xpath(locator);
		}
		if (nameLocator(locator)) {
			return By.name(stripTag(locator));
		}
		if (tagNameLocator(locator)) {
			return By.name(stripTag(locator));
		}
		if (linkLocator(locator)) {
			return By.linkText(stripTag(locator));
		}
		return By.id(stripTag(locator));
	}
	
	private static String stripTag(String in) {
		if (StringUtils.contains(in, "=")) return StringUtils.substringAfter(in, "=");
		return in;
	}
	
	private static boolean tagNameLocator(String locator)
	{
		return locator.startsWith("tagname=") || locator.startsWith("//");
	}
	
	private static boolean xpathLocator(String locator)
	{
		return locator.startsWith("xpath=") || locator.startsWith("//");
	}

	private static boolean linkLocator(String locator)
	{
		return locator.startsWith("link=");
	}

	private static boolean nameLocator(String locator)
	{
		return locator.startsWith("name=");
	}

	private static boolean idLocator(String locator)
	{
		return locator.startsWith("id=");
	}

	private static boolean cssLocator(String locator)
	{
		return locator.startsWith("css=");
	}
}
