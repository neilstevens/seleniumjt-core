package com.jt.selenium.utils.testng.xml.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class TestNgBuilderHelper
{

	public static String getTestTemplate(TestNgBuilderProps props) throws ClassNotFoundException, IOException
	{
		Scanner sc = new Scanner(new File(props.getTestTemplate()));
		StringBuilder sb = new StringBuilder();
		while (sc.hasNextLine())
		{
			sb.append("\n" + sc.nextLine());
		}
		return sb.toString();

	}

	public static String getTests(List<TestNgTest> tests, String browser, String template) throws ClassNotFoundException, IOException
	{
		StringBuilder sb = new StringBuilder();
		for (TestNgTest test : tests)
		{
			sb.append(getTest(test, browser, template));
		}
		return sb.toString();

	}

	public static String getTest(TestNgTest test, String browser, String template) throws ClassNotFoundException, IOException
	{
		String s = StringUtils.replace(template, TestNgBuilder.TEST_NAME,
				test.getPackageName() + "." + test.getStringForFileName());
		s = StringUtils.replace(s, TestNgBuilder.BROWSER, browser);
		return s;

	}

}
