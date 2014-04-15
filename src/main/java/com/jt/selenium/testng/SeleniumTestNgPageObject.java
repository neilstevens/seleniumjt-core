package com.jt.selenium.testng;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import com.jt.selenium.SeleniumJT;
import com.jt.selenium.factory.SeleniumJTFactory;


/**
 * 
 * @author Dave.Hampton
 *
 */
public abstract class SeleniumTestNgPageObject
{
	// static single instance of this class required to be shared across
	// multiple test classes configured within the TestNG XML
	protected static SeleniumJT test;

	@BeforeClass(alwaysRun = true)
	public void startSession(ITestContext context) throws Exception
	{
		if (test==null) test = SeleniumJTFactory.getSeleniumJT(context);
	}

	@AfterSuite(alwaysRun = true)
	public void afterClass(ITestContext context) throws Exception
	{
		test.stop();
	}
}
