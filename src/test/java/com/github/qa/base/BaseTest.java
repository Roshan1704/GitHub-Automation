package com.github.qa.base;

import com.github.qa.api.GitHubAPIClient;
import com.github.qa.utils.WebDriverUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.Locale;

public abstract class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverUtils> utilsThread = new ThreadLocal<>();
    private static final ThreadLocal<GitHubAPIClient> apiClientThread = new ThreadLocal<>();

    private static final String DEFAULT_BASE_URL = "https://github.com";
    private static final int DEFAULT_IMPLICIT_WAIT_SECONDS = 10;
    private static final int DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS = 30;
    private static final int DEFAULT_SCRIPT_TIMEOUT_SECONDS = 30;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        logger.info("Initializing test context. browser={}, headless={}", browser, headless);

        WebDriver driver = createDriver(browser, headless);

        if (!headless) {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(getIntProperty("implicitWaitSeconds", DEFAULT_IMPLICIT_WAIT_SECONDS)))
                .pageLoadTimeout(Duration.ofSeconds(getIntProperty("pageLoadTimeoutSeconds", DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS)))
                .scriptTimeout(Duration.ofSeconds(getIntProperty("scriptTimeoutSeconds", DEFAULT_SCRIPT_TIMEOUT_SECONDS)));

        driverThread.set(driver);
        utilsThread.set(new WebDriverUtils(driver));
        apiClientThread.set(new GitHubAPIClient());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
        }

        driverThread.remove();
        utilsThread.remove();
        apiClientThread.remove();
    }

    protected WebDriver getDriver() {
        return requireInitialized(driverThread.get(), "WebDriver is not initialized. Did @BeforeMethod run?");
    }

    protected WebDriverUtils getUtils() {
        return requireInitialized(utilsThread.get(), "WebDriverUtils is not initialized. Did @BeforeMethod run?");
    }

    protected GitHubAPIClient getApiClient() {
        return requireInitialized(apiClientThread.get(), "GitHubAPIClient is not initialized. Did @BeforeMethod run?");
    }

    protected String getBaseUrl() {
        return System.getProperty("baseUrl", DEFAULT_BASE_URL);
    }

    private WebDriver createDriver(String browser, boolean headless) {
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless) {
                    chromeOptions.addArguments(
                            "--headless=new",
                            "--disable-gpu",
                            "--window-size=1920,1080",
                            "--no-sandbox",
                            "--disable-dev-shm-usage"
                    );
                }

                return new ChromeDriver(chromeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser + ". Supported: chrome, firefox");
        }
    }

    private int getIntProperty(String propertyName, int defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            logger.warn("Invalid integer for property {}: {}. Falling back to {}", propertyName, value, defaultValue);
            return defaultValue;
        }
    }

    private <T> T requireInitialized(T value, String message) {
        if (value == null) {
            throw new IllegalStateException(message);
        }
        return value;
    }
}
