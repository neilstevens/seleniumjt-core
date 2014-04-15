package com.jt.selenium.utils.testng.xml.builder;

import org.apache.commons.lang3.StringUtils;

public class TestNgTest
{
	private String packageName;
	private String testName;

	public String getPackageName()
	{
		return packageName;
	}

	public String getTestName()
	{
		return testName;
	}

	public String getStringForFileName()
	{
		return StringUtils.substring(getTestName(), 0, getTestName().length() - 5);
	}

	public TestNgTest(String packageName, String testName) {
		super();
		this.packageName = packageName;
		this.testName = testName;
	}

}
