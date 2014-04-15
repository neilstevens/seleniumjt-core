package com.jt.selenium.configuration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public enum BrowserType {

	FIREFOX("ff"),
	IE("ie"),
	GOOGLE_CHROME("chrome"),
	SAFARI("safari"),
	PHANTOM("phantom");

	private static final Map<String, BrowserType> LOOK_UP = new HashMap<String, BrowserType>();
	static
	{
		for (BrowserType s : EnumSet.allOf(BrowserType.class))
		{
			LOOK_UP.put(s.getValue(), s);
		}
	}

	private String value;

	private BrowserType(String value) {
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public static BrowserType getBrowserType(String value)
	{
		value = getStrippedBrowserType(value);
		return LOOK_UP.get(value);
	}

	public static String getStrippedBrowserType(String browserType)
	{
		if (StringUtils.contains(browserType, " C:"))
		{
			browserType = StringUtils.substringBefore(browserType, " C:");
		}
		return browserType;
	}

	public boolean runningInIE()
	{
		return this.equals(BrowserType.IE);
	}

	public boolean runningInPhantom()
	{
		return this.equals(BrowserType.PHANTOM);
	}

	public boolean runningInSafari()
	{
		return this.equals(BrowserType.SAFARI);
	}

	public boolean runningInFireFox()
	{
		return this.equals(BrowserType.FIREFOX);
	}

	public boolean runningInChrome()
	{
		return this.equals(BrowserType.GOOGLE_CHROME);
	}

	public static boolean isFF(BrowserType in)
	{
		return in.equals(FIREFOX);
	}

	public static boolean isSafari(BrowserType in)
	{
		return in.equals(SAFARI);
	}
}