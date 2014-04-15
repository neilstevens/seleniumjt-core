package com.jt.selenium.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class SeleneseConvertor {

	private final String IDE_IN_TXT = "ide.in.txt";
	private final String IDE_CONVERTOR_LOC = "ide.convertor.loc";
	private final String SELENIUM_MATCHERS_PROPERTIES = "selenium-matchers.properties";
	private final String CLICK = "selenium.click";
	private final String PAGE_WAIT = "waitForPageToLoad";
	private final String SELENIUM_MATCHER = "selenium.";
	private final String DEL = "<$>";

	/**
	 * @param args
	 * @throws IOException
	 */
	public void convert() throws IOException {
		Properties testNGProperties = PropertiesHelper.getRuntimeProperties() ;
		String ideDirectoryLocation = testNGProperties.getProperty(IDE_CONVERTOR_LOC);
		InputStream is = FileHelper.getInputStreamByDir(ideDirectoryLocation+SELENIUM_MATCHERS_PROPERTIES);
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		List<String> seleniumProps = new ArrayList<String>();
		String line;
		while ((line = r.readLine()) != null) {
			seleniumProps.add(line);
		}
		is = FileHelper.getInputStreamByDir(ideDirectoryLocation+IDE_IN_TXT);
		r = new BufferedReader(new InputStreamReader(is));
		List<String> lines = new ArrayList<String>();
		while ((line = r.readLine()) != null) {
			lines.add(line);
		}
		List<String> readLines = clearWaits(lines);
		for (String string : readLines) {
			for (String s : seleniumProps) {
				String[] split = s.split("=");
				string = parse(string, split[0], split[1]);
			}
			System.out.println(string);
		}
	}
	private List<String> clearWaits(List<String> readLines) {
		List<String> result = new ArrayList<String>();
		int size = readLines.size();
		for (int i = 0; i < size; i++) {
			String string = readLines.get(i);
			if (string.contains(CLICK)) {
				String waitline = readLines.get(i + 1);
				if (waitline.contains(PAGE_WAIT)) {
					string = string.replace(CLICK, "selenium.clickAndWait");
				}
			}
			if (!string.contains(PAGE_WAIT)) {
				result.add(string);
			}
		}
		return result;
	}

	private String getValue(String source, String start, String end) {
		return StringUtils.substringBetween(source, start, end);
	}

	private String parse(String source, String matcher, String output) {
		source = source.replace("\"id=", "\"");
		if (source.contains(SELENIUM_MATCHER) && !source.contains("import") && !source.contains("(selenium.")) {
			source = source.replace(SELENIUM_MATCHER, "test.");
		}
		String[] msplit = StringUtils.split(matcher, DEL);
		String result = output;
		String orig = matcher;
		if (StringUtils.contains(source, msplit[0])) {
			for (int i = 0; i < msplit.length - 1; i++) {
				String value = getValue(source, msplit[i], msplit[i + 1]);
				result = StringUtils.replaceOnce(result, DEL, value);
				orig = StringUtils.replaceOnce(orig, DEL, value);
			}
			result = StringUtils.replace(source, orig, result);
			return result;
		}
		return source;
	}

}
