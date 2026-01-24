package com.github.qa.base;

import com.github.qa.api.GitHubAPIClient;
import com.github.qa.utils.WebDriverUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverUtils> utilsThread = new ThreadLocal<>();
    private static final ThreadLocal<GitHubAPIClient> apiClientThread = new ThreadLocal<>();

    private static final String BASE_URL = "https://github.com";
    private static final int IMPLICIT_WAIT = 10;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        // ================= CONFIG =================
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "true")
        );

        // ================= UI =================
        WebDriver driver = createDriver(browser, headless);

        driver.manage().window().maximize();
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));

        driverThread.set(driver);
        utilsThread.set(new WebDriverUtils(driver));

        // ================= API =================
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

    // ================= GETTERS =================

    protected WebDriver getDriver() {
        return driverThread.get();
    }

    protected WebDriverUtils getUtils() {
        return utilsThread.get();
    }

    protected GitHubAPIClient getApiClient() {
        return apiClientThread.get();
    }

    protected String getBaseUrl() {
        return BASE_URL;
    }

    // ================= DRIVER FACTORY =================

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
            default:
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
        }
    }
}
