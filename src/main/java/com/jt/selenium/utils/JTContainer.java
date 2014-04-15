package com.jt.selenium.utils;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import com.jt.selenium.configuration.SeleniumConfiguration;

@Component
public class JTContainer
{
	private WebDriver webDriver;
	private SeleniumConfiguration configuration;
	
	public void setDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	public void stop() {
		webDriver.quit();
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}

	/**
	 * @return the configuration
	 */
	public SeleniumConfiguration getConfiguration()
	{
		return configuration;
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(SeleniumConfiguration configuration)
	{
		this.configuration = configuration;
	}
}
