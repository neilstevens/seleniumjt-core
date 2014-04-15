package com.jt.selenium.utils.testng.xml.builder;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.jt.selenium.utils.FileHelper;

public class TestNgBuilderProps
{
	private static final String FILE_LOC_RELATIVE_TO_ANT = "%s/%s";
	private final Properties properties;
	private final String testPackage = "test.package";
	private final String targetDir = "target.file.dir";

	private final String workingDirectory;

	public String getWorkingDir()
	{
		return workingDirectory;
	}

	public Properties getProperties()
	{
		return properties;
	}

	public String getTestDir(String packageName)
	{
		return String.format("%s/src/test/java/%s", getWorkingDir(), StringUtils.replace(packageName, ".", "/"));
	}

	public String getTestPackage()
	{
		return properties.getProperty(testPackage);
	}

	public String getTargetDir()
	{
		return String.format("%s%s", getWorkingDir(), properties.getProperty(targetDir));
	}

	public String getTargetSubDir(String packageName)
	{
		if (packageName.equals(testPackage))
		{
			return "";
		}
		String subPackage = StringUtils.substring(packageName, getTestPackage().length(), packageName.length());
		return StringUtils.replace(subPackage, ".", "/");
	}

	public String getSuiteTemplate()
	{
		return String.format(FILE_LOC_RELATIVE_TO_ANT, getWorkingDir(), FileHelper.getExtractedFile(FileHelper.SUITE_TEMPLATE_TPL));
	}

	public String getTestTemplate()
	{
		return String.format(FILE_LOC_RELATIVE_TO_ANT, getWorkingDir(), FileHelper.getExtractedFile(FileHelper.TEST_TEMPLATE_TPL));
	}

	public TestNgBuilderProps(Properties properties, String workingDirectory) {
		super();
		this.properties = properties;
		this.workingDirectory = workingDirectory;
	}

}
