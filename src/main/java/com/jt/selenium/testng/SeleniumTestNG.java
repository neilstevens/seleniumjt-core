package com.jt.selenium.testng;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.jt.selenium.SeleniumJT;
import com.jt.selenium.factory.SeleniumJTFactory;

/**
 * 
 * Tests for selenium should extend this class to ensure that the underlying selenium configuration
 * is instantiated correctly.
 * 
 * @author Dave.Hampton
 *
 */
public abstract class SeleniumTestNG
{
	protected SeleniumJT test;

	@BeforeClass(alwaysRun = true)
	public void startSession(ITestContext context) throws Exception
	{
		test = SeleniumJTFactory.getSeleniumJT(context);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(ITestContext context) throws Exception
	{
		test.stop();
	}

}
