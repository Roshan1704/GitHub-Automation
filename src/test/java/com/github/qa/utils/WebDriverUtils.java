package com.github.qa.utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Utility class for common WebDriver operations.
 * Provides explicit wait wrappers and element interaction methods.
 */
public class WebDriverUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverUtils.class);
    private final WebDriver driver;

    /**
     * Constructor - accepts WebDriver instance for instance-level operations
     */
    public WebDriverUtils(WebDriver driver) {
        this.driver = driver;
        logger.info("WebDriverUtils initialized with driver");
    }

    /**
     * Wait for element to be visible with explicit waits
     */
    @Step("Wait for element visibility: {locator}")
    public static WebElement waitForElementVisibility(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Timeout waiting for element visibility: {}", locator, e);
            throw new RuntimeException("Element not visible within timeout: " + locator);
        }
    }

    /**
     * Wait for element to be clickable with explicit waits
     */
    @Step("Wait for element clickability: {locator}")
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element is clickable: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Timeout waiting for element clickability: {}", locator, e);
            throw new RuntimeException("Element not clickable within timeout: " + locator);
        }
    }

    /**
     * Wait for element to be present in DOM
     */
    @Step("Wait for element presence: {locator}")
    public static WebElement waitForElementPresence(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Element is present in DOM: {}", locator);
            return element;
        } catch (Exception e) {
            logger.error("Timeout waiting for element presence: {}", locator, e);
            throw new RuntimeException("Element not present in DOM within timeout: " + locator);
        }
    }

    /**
     * Wait for all elements to be visible
     */
    @Step("Wait for elements visibility: {locator}")
    public static List<WebElement> waitForElementsVisibility(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            logger.info("All elements are visible: {}", locator);
            return elements;
        } catch (Exception e) {
            logger.error("Timeout waiting for elements visibility: {}", locator, e);
            throw new RuntimeException("Elements not visible within timeout: " + locator);
        }
    }

    /**
     * Click on element with explicit wait
     */
    @Step("Click on element: {locator}")
    public static void clickElement(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebElement element = waitForElementClickable(driver, locator, timeoutSeconds);
            element.click();
            logger.info("Element clicked successfully: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", locator, e);
            throw new RuntimeException("Click action failed: " + locator);
        }
    }

    /**
     * Send keys to element with explicit wait
     */
    @Step("Send keys to element: {locator} with text: {text}")
    public static void sendKeys(WebDriver driver, By locator, String text, int timeoutSeconds) {
        try {
            WebElement element = waitForElementVisibility(driver, locator, timeoutSeconds);
            element.clear();
            element.sendKeys(text);
            logger.info("Text sent to element: {} | Text: {}", locator, text);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: {}", locator, e);
            throw new RuntimeException("SendKeys action failed: " + locator);
        }
    }

    /**
     * Get text from element with explicit wait
     */
    @Step("Get text from element: {locator}")
    public static String getText(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebElement element = waitForElementVisibility(driver, locator, timeoutSeconds);
            String text = element.getText();
            logger.info("Text retrieved from element: {} | Text: {}", locator, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", locator, e);
            throw new RuntimeException("GetText action failed: " + locator);
        }
    }

    /**
     * Wait for page to load (title is not empty) - with driver parameter
     */
    @Step("Wait for page load")
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(driver1 -> !driver1.getTitle().isEmpty());
            logger.info("Page loaded with title: {}", driver.getTitle());
        } catch (Exception e) {
            logger.error("Timeout waiting for page load", e);
            throw new RuntimeException("Page did not load within timeout");
        }
    }

    /**
     * Wait for page to load (title is not empty) - without driver parameter
     * Uses a default 10 second timeout
     */
    @Step("Wait for page load (default timeout)")
    public void waitForPageLoad(int timeoutSeconds) {
        logger.warn("Using WebDriverUtils instance method without driver - this should use static method instead");
        throw new RuntimeException("Please use static waitForPageLoad(WebDriver driver, int timeoutSeconds)");
    }

    /**
     * Check if element is displayed (By locator)
     */
    @Step("Check element visibility: {locator}")
    public static boolean isElementDisplayed(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not displayed: {}", locator);
            return false;
        }
    }

    /**
     * Check if element is displayed (WebElement)
     * Overload for direct WebElement checking
     */
    @Step("Check WebElement visibility")
    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.warn("WebElement not displayed");
            return false;
        }
    }

    /**
     * Click on a WebElement directly (instance method)
     */
    @Step("Click on WebElement directly")
    public void click(WebElement element) {
        try {
            element.click();
            logger.info("Element clicked successfully");
        } catch (Exception e) {
            logger.error("Failed to click element", e);
            throw new RuntimeException("Click action failed", e);
        }
    }

    /**
     * Get text from a WebElement directly (instance method)
     */
    @Step("Get text from WebElement directly")
    public String getElementText(WebElement element) {
        try {
            String text = element.getText();
            logger.info("Text retrieved from element: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element", e);
            throw new RuntimeException("GetText action failed", e);
        }
    }

    /**
     * Send keys to a WebElement directly (instance method)
     */
    @Step("Send keys to WebElement directly")
    public void sendKeys(WebElement element, String text) {
        try {
            element.clear();
            element.sendKeys(text);
            logger.info("Text sent to element: {}", text);
        } catch (Exception e) {
            logger.error("Failed to send keys to element", e);
            throw new RuntimeException("SendKeys action failed", e);
        }
    }

    /**
     * Check if a WebElement is displayed (instance method)
     */
    @Step("Check WebElement visibility")
    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not displayed");
            return false;
        }
    }

    /**
     * Get attribute value from a WebElement (instance method)
     */
    @Step("Get attribute from WebElement")
    public String getAttribute(WebElement element, String attributeName) {
        try {
            String value = element.getAttribute(attributeName);
            logger.info("Attribute '{}' value: {}", attributeName, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute from element", e);
            throw new RuntimeException("GetAttribute action failed", e);
        }
    }

    /**
     * Clear input field (instance method)
     */
    @Step("Clear input field")
    public void clearInput(WebElement element) {
        try {
            element.clear();
            logger.info("Input field cleared successfully");
        } catch (Exception e) {
            logger.error("Failed to clear input field", e);
            throw new RuntimeException("Clear action failed", e);
        }
    }

    /**
     * Get element attribute value (instance method - alternative name)
     */
    @Step("Get element attribute")
    public String getElementAttribute(WebElement element, String attribute) {
        return getAttribute(element, attribute);
    }
}
