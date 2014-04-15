package com.jt.selenium.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.jt.selenium.configuration.SeleniumConfiguration;

public class FileHelper {
	
	public static final String TESTNG_XML_BUILD_PROPERTIES = "testng.xml.build.properties";
	public static final String TEST_TEMPLATE_TPL = "test-template.tpl";
	public static final String SUITE_TEMPLATE_TPL = "suite-template.tpl";
	
	public static final String DEBUGMODE = "debugmode";
	public static final String PROPERTIES_LOC = "properties/";
	public static final String TESTDATA_PROPERTIES = "testdata.properties";
	public static final String PROPERTY_FILE_PARAMETER = "properties.file";

	public static final String SELENIUM_PROPERTIES = "selenium.properties";
	public static final String LOCAL_SELENIUM_PROPERTIES = "my.selenium.properties";
	
	private static final String CORE_FILES_EXTRACTION_LOC = "target/jtcore/";
	
	
	public static InputStream getInputStreamByClassLoader(String fileName) {
		return FileHelper.class.getClassLoader().getResourceAsStream(fileName);
	}
	public static InputStream getInputStreamByDir(String fileName) throws FileNotFoundException {
		return new FileInputStream(SeleniumConfiguration.getOperationalDirectory()+fileName);
	}
	
	public static String getExtractedFile(String fileName) {
		return CORE_FILES_EXTRACTION_LOC+fileName;
	}
	
	
	
}
