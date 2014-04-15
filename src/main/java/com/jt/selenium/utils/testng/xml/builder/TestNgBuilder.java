package com.jt.selenium.utils.testng.xml.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.jt.selenium.configuration.BrowserType;
import com.jt.selenium.utils.PropertiesHelper;

public class TestNgBuilder
{
	private static final String FILE_EXTENSION = ".xml";
	private static final String TESTS = "@tests@";
	public static final String BROWSER = "@browser@";
	public static final String TEST_NAME = "@test-name@";
	public static final String TEST_PACKAGE = "@package@";

	private TestNgBuilderProps props;

	public TestNgBuilder(String workingDirectory) throws FileNotFoundException, IOException {
		super();
		Properties p = PropertiesHelper.getRuntimeProperties();
		props = new TestNgBuilderProps(p, workingDirectory);
	}

	public static String getSuiteTemplate(TestNgBuilderProps props) throws ClassNotFoundException, IOException
	{
		String suiteTemplate = props.getSuiteTemplate();
		System.out.println(suiteTemplate);
		Scanner sc = new Scanner(new File(suiteTemplate));
		StringBuilder sb = new StringBuilder();
		while (sc.hasNextLine())
		{
			sb.append(sc.nextLine() + "\n");
		}
		return sb.toString();

	}

	public List<TestNgTest> recursiveTestBuilder(String packageName) throws ClassNotFoundException, IOException
	{
		String dirName = props.getTestDir(packageName);
		File dir = new File(dirName);
		List<String> testList = null;
		try {
			testList = Arrays.asList(dir.list());
		}
		catch (NullPointerException e) {
			throw new IllegalStateException("There is an error. testng.xml.build.properties defines test.package as "+dirName+" which does not exist");
		}
		List<String> subdirs = new ArrayList<String>();
		List<TestNgTest> ngTests = new ArrayList<TestNgTest>();
		for (String fileName : testList)
		{
			if (!".svn".equals(fileName)) {
				if (isJavaTest(fileName))
				{
					ngTests.add(new TestNgTest(packageName, fileName));
				}
				else
				{
					subdirs.add(fileName);
				}
			}
		}
		if (!subdirs.isEmpty())
		{
			for (String fileName : subdirs)
			{
				ngTests.addAll(recursiveTestBuilder(packageName + "." + fileName));
			}
		}

		return ngTests;
	}

	private boolean isJavaTest(String string)
	{
		return StringUtils.endsWith(string, ".java");
	}

	public void writeTests() throws ClassNotFoundException, IOException
	{

		String suiteTemplate = getSuiteTemplate(props);
		String testTemplate = TestNgBuilderHelper.getTestTemplate(props);

		List<TestNgTest> testNgTests = recursiveTestBuilder(props.getTestPackage());

		BrowserType[] browsers = BrowserType.values();
		createFullSuites(suiteTemplate, testTemplate, testNgTests, browsers);
		createIndividualTestFiles(suiteTemplate, testTemplate, testNgTests, browsers);
		consol("===================");
		consol("==== COMPLETED ====");
		consol("===================");
	}

	private void consol(String msg)
	{
		System.out.println(msg);
	}

	private void createFullSuites(String suiteTemplate, String testTemplate, List<TestNgTest> testList, BrowserType[] browsers) throws ClassNotFoundException,
			IOException
	{
		for (BrowserType browserType : browsers)
		{
			String fullTests = TestNgBuilderHelper.getTests(testList, browserType.getValue(), testTemplate);
			String browserSuiteTemplate = StringUtils.replace(suiteTemplate, BROWSER, browserType.getValue());
			String fullSuite = StringUtils.replace(browserSuiteTemplate, TESTS, fullTests);
			String fileName = String.format("%s/fullsuite-%s%s", props.getTargetDir(), browserType.name().toLowerCase(), FILE_EXTENSION);
			writeToFile(fileName, fullSuite);

		}
	}

	private void createIndividualTestFiles(String suiteTemplate, String testTemplate, List<TestNgTest> testList, BrowserType[] values)
			throws ClassNotFoundException, IOException
	{
		for (BrowserType browserType : values)
		{
			String browserSuiteTemplate = StringUtils.replace(suiteTemplate, BROWSER, browserType.getValue());
			String directory = String.format("%s/%s", props.getTargetDir(), browserType.name().toLowerCase());
			initDirStructure(directory);
			for (TestNgTest test : testList)
			{
				String targetSubDir = props.getTargetSubDir(test.getPackageName());
				if (!StringUtils.isBlank(targetSubDir))
				{
					initDirStructure(directory + targetSubDir);
				}
			}
			for (TestNgTest test : testList)
			{
				String fullTest = TestNgBuilderHelper.getTest(test, browserType.getValue(), testTemplate);
				String fullSuite = StringUtils.replace(browserSuiteTemplate, TESTS, fullTest);

				String fileName = String.format("%s/singletest-%s%s",
						directory + props.getTargetSubDir(test.getPackageName()),
						test.getStringForFileName(),
						FILE_EXTENSION);
				writeToFile(fileName, fullSuite);
			}
		}
	}

	private String initDirStructure(String directory) throws IOException
	{
		File directoryFile = new File(directory);
		if (!directoryFile.exists())
		{
			FileUtils.forceMkdir(directoryFile);
		}
		else
		{
			FileUtils.deleteDirectory(directoryFile);
			FileUtils.forceMkdir(directoryFile);
		}
		return directory;
	}

	private void writeToFile(String fileName, String content) throws IOException
	{
		consol("Writing file " + fileName);
		FileUtils.writeStringToFile(FileUtils.getFile(fileName), content);
	}

}
