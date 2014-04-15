package com.jt.selenium.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.ITestContext;

public class PropertiesHelper {

	private static InputStream getInputStream(String file) throws FileNotFoundException {
		String format = String.format("%s%s", FileHelper.PROPERTIES_LOC, file);
		System.out.println("Loading from class loader "+format);
		return FileHelper.getInputStreamByClassLoader(format);
	}

	public static Properties getRuntimeProperties() {
		Properties p = new Properties();
		try {
			InputStream inputStream = getInputStream(FileHelper.SELENIUM_PROPERTIES);
			if (inputStream==null) {
				throw new IllegalStateException(FileHelper.PROPERTIES_LOC + FileHelper.SELENIUM_PROPERTIES+" is missing");
			}
			p.load(inputStream);
			Properties myP = new Properties();
			InputStream myInputStream = getInputStream(FileHelper.LOCAL_SELENIUM_PROPERTIES);
			if (myInputStream != null) {
				myP.load(myInputStream);
				p.putAll(myP);
			}
		} catch (IOException e) {
			throw new IllegalStateException(FileHelper.PROPERTIES_LOC + FileHelper.SELENIUM_PROPERTIES+" is missing");
		}
		return p;
	}
	
	public static Properties getDataProperties(ITestContext context) {
		Properties p = new Properties();
		Map<String, String> parameters = new HashMap<String, String>();
		try {
			p.load(getInputStream(FileHelper.TESTDATA_PROPERTIES));
			parameters = context.getCurrentXmlTest().getParameters();
			p.putAll(parameters);
			if (parameters.containsKey(FileHelper.PROPERTY_FILE_PARAMETER)) {
				Properties p2 = new Properties();
				p2.load(getInputStream(parameters.get(FileHelper.PROPERTY_FILE_PARAMETER)));
				p.putAll(p2);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error loading testData "+e.getMessage());
		}catch (NullPointerException e) {
			throw new IllegalStateException("Property file does not exist "+parameters.get(FileHelper.PROPERTY_FILE_PARAMETER));
		}
		return p;
	}
	
	public static boolean isDebugMode() throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(getInputStream(FileHelper.SELENIUM_PROPERTIES));
		return Boolean.valueOf((String) p.get(FileHelper.DEBUGMODE));
	}
}
