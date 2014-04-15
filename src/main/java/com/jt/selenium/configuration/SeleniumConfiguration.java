package com.jt.selenium.configuration;

import java.util.Properties;

import org.testng.ITestContext;

import com.jt.selenium.utils.PropertiesHelper;

public class SeleniumConfiguration
{

	public final static String BROWSER_TYPE = "browserType";
	public static final String USER_DIR = "user.dir";
	private final String WAITING_TIME = "WAITING_TIME";
	private final String TARGET_WEBSITE = "target.website";
	private final String PAGE_LOAD_TIME = "PAGE_LOAD_TIME";
	private final String AJAX_START_DELAY = "AJAX_START_DELAY";
	private final String AJAX_INTERVAL = "AJAX_INTERVAL";
	private final String AJAX_TIMEOUT = "AJAX_TIMEOUT";
	private final String COMMAND_REPEAT_MILS = "COMMAND_REPEAT_MILS";
	private final String RETRY_ATTEMPTS = "RETRY_ATTEMPTS";
	private final String WEBDRIVER_LOC = "webdriver.location";

	private Properties properties;

	private BrowserType browserType;

	private SeleniumConfiguration(Properties properties, BrowserType browserType) {
		this.properties = properties;
		this.browserType = browserType;
	}
	
	private static BrowserType getBrowserType(ITestContext context) {
		return BrowserType.getBrowserType(context.getCurrentXmlTest().getParameter(SeleniumConfiguration.BROWSER_TYPE));
	}
	
	public static String getOperationalDirectory() {
		return System.getProperty(USER_DIR);
	}

	public static SeleniumConfiguration getInstance(ITestContext testNgContext)
	{
		return new SeleniumConfiguration(PropertiesHelper.getRuntimeProperties(), getBrowserType(testNgContext));
	}

	public static SeleniumConfiguration getInstance(String browser)
	{
		return new SeleniumConfiguration(PropertiesHelper.getRuntimeProperties(), BrowserType.getBrowserType(browser));
	}

	public String getPropertyAsString(String key)
	{
		return getProperty(key);
	}

	private String getProperty(String key)
	{
		String property = properties.getProperty(key);
		if (property == null)
		{
			throw new IllegalStateException("No value for " + key);
		}
		return property;
	}

	public boolean getPropertyAsBoolean(String key)
	{
		return Boolean.valueOf(getProperty(key));
	}

	public int getPropertyAsInt(String key)
	{
		return Integer.valueOf(getProperty(key));
	}

	public BrowserType getBrowserType()
	{
		return browserType;
	}

	public String getTargetWebsite()
	{
		return getProperty(TARGET_WEBSITE);
	}

	public String get(String key)
	{
		return getProperty(key);
	}

	public int getRetryAttempts()
	{
		return Integer.valueOf(getProperty(RETRY_ATTEMPTS));
	}

	public String getPageLoadTime()
	{
		return getProperty(PAGE_LOAD_TIME);
	}

	public int getWaitingPeriod()
	{
		return Integer.valueOf(getProperty(WAITING_TIME));
	}

	public int getAjaxStartDelay()
	{
		return Integer.valueOf(getProperty(AJAX_START_DELAY));
	}

	public int getAjaxInterval()
	{
		return Integer.valueOf(getProperty(AJAX_INTERVAL));
	}

	public int getAjaxTimeout()
	{
		return Integer.valueOf(getProperty(AJAX_TIMEOUT));
	}

	public int getCommandRepeatMils()
	{
		return Integer.valueOf(getProperty(COMMAND_REPEAT_MILS));
	}

	public String getWebDriverLocation() {
		return get(WEBDRIVER_LOC);
	}

}
