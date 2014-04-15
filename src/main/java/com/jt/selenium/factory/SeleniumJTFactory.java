package com.jt.selenium.factory;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.ITestContext;

import com.jt.selenium.SeleniumJT;
import com.jt.selenium.configuration.SeleniumConfiguration;
import com.jt.selenium.utils.FileHelper;
import com.jt.selenium.utils.PropertiesHelper;

public class SeleniumJTFactory
{
	
	private static final String IE_DRIVER = FileHelper.getExtractedFile("IEDriverServer");
	private static final String WEBDRIVER_IE_DRIVER = "webdriver.ie.driver";
	private static final String WEBDRIVER_FIREFOX_BIN = "webdriver.firefox.bin";
	private static final String FIREFOX_EXE_LOCATION = "firefox.exe.location";
	private static final String PHANTOM_JS_LOC = "phantomjs.binary.path";
	private static final String CONFIG_PATH = "application-config.xml";
	private static final String WEBDRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
    private static final String CHROMEDRIVER_WIN_LOC = "chromedriver.exe";
    private static final String PHANTOM_WIN_LOC = "phantomjs.exe";
    private static final String CHROMEDRIVER_LINUX_LOC = "chromedriver";
	

	private static SeleniumJT sjt;

	private static WebDriver getWebDriver(SeleniumConfiguration config) {
		WebDriver driver = null;
		if(config.getBrowserType().runningInIE()) {
			driver = getIEDriver(config);
		}else if(config.getBrowserType().runningInFireFox()) {
			driver = getFFDriver(config);
		}else if(config.getBrowserType().runningInChrome()) {
			driver = getChromeDriver(config);
		}else if(config.getBrowserType().runningInPhantom()) {
			
			driver = getPhantomDriver(config);
		}
		return driver;
	}

	private static WebDriver getChromeDriver(SeleniumConfiguration config) {
        String chromdriverLoc = CHROMEDRIVER_WIN_LOC;
        if(SystemUtils.IS_OS_LINUX) {
            chromdriverLoc = CHROMEDRIVER_LINUX_LOC;
        }
        String loc = String.format("%s/%s", config.getWebDriverLocation(), chromdriverLoc);
		System.setProperty(WEBDRIVER_SYSTEM_PROPERTY, loc);
		return new ChromeDriver();
	}

	private static WebDriver getPhantomDriver(SeleniumConfiguration config) {
		DesiredCapabilities dCaps = new DesiredCapabilities();
		dCaps.setJavascriptEnabled(true);
		String[] args = { "--ignore-ssl-errors=yes" };
		dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, args);
        System.setProperty(PHANTOM_JS_LOC, String.format("%s/%s", config.getWebDriverLocation(), PHANTOM_WIN_LOC));
		return new PhantomJSDriver(dCaps);
	}

	private static WebDriver getFFDriver(SeleniumConfiguration config) {
        System.setProperty(WEBDRIVER_FIREFOX_BIN, config.get(FIREFOX_EXE_LOCATION));
		return new FirefoxDriver();
	}

	private static WebDriver getIEDriver(SeleniumConfiguration config) {
		String driverVersion = config.get("ieDriverVersion");
		String ieDriver = driverVersion==null? IE_DRIVER+"32.exe" : IE_DRIVER+driverVersion+".exe";
		System.setProperty(WEBDRIVER_IE_DRIVER, String.format("%s/%s", config.getWebDriverLocation(), ieDriver));
		return new InternetExplorerDriver();
	}
	
	public static SeleniumJT getSeleniumJT(ITestContext testNgContext) {
		final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_PATH);
		sjt = context.getBean(SeleniumJT.class);
		sjt.setConfiguration(SeleniumConfiguration.getInstance(testNgContext));
		sjt.setWebDriver(getWebDriver(sjt.getConfiguration()));
		sjt.setTestData(PropertiesHelper.getDataProperties(testNgContext));
		return sjt;
	}
	
	public static SeleniumJT getSeleniumJT(String browser) {
		final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_PATH);
		sjt = context.getBean(SeleniumJT.class);
		sjt.setConfiguration(SeleniumConfiguration.getInstance(browser));
		sjt.setWebDriver(getWebDriver(sjt.getConfiguration()));
		return sjt;
	}

}
