package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for GitHub Home Page
 */
public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final WebDriver driver;
    private final int TIMEOUT = 15;

    // Locators
    private final By searchInput = By.id("query-builder-test");
    private final By searchButton = By.name("type");
    private final By exploreLink = By.xpath("//a[contains(text(), 'Explore')]");
    private final By repositoriesLink = By.xpath("//a[contains(text(), 'Repositories')]");
    private final By pageTitle = By.xpath("//h1[contains(text(), 'Where the world builds software')]");
    private final By signInButton = By.xpath("//a[contains(text(), 'Sign in')]");
    private final By heroSection = By.xpath("//section[@class='color-bg-primary']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Load GitHub home page")
    public void load() {
        driver.get("https://github.com");
        WebDriverUtils.waitForPageLoad(driver, TIMEOUT);
        logger.info("Home page loaded successfully");
    }

    @Step("Verify home page is displayed")
    public boolean isHomePageDisplayed() {
        boolean isDisplayed = WebDriverUtils.isElementDisplayed(driver, pageTitle);
        logger.info("Home page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: {}", title);
        return title;
    }

    @Step("Click on Explore link")
    public void clickExplore() {
        WebDriverUtils.clickElement(driver, exploreLink, TIMEOUT);
        logger.info("Explore link clicked");
    }

    @Step("Click on Repositories link")
    public void clickRepositories() {
        WebDriverUtils.clickElement(driver, repositoriesLink, TIMEOUT);
        logger.info("Repositories link clicked");
    }

    @Step("Click sign in button")
    public void clickSignIn() {
        WebDriverUtils.clickElement(driver, signInButton, TIMEOUT);
        logger.info("Sign in button clicked");
    }

    @Step("Verify hero section is visible")
    public boolean isHeroSectionVisible() {
        boolean isVisible = WebDriverUtils.isElementDisplayed(driver, heroSection);
        logger.info("Hero section visible: {}", isVisible);
        return isVisible;
    }

    @Step("Check if page loads within SLA (3 seconds)")
    public boolean pageLoadsSLA() {
        long startTime = System.currentTimeMillis();
        WebDriverUtils.waitForPageLoad(driver, TIMEOUT);
        long loadTime = System.currentTimeMillis() - startTime;
        boolean passedSLA = loadTime < 3000;
        logger.info("Page load time: {}ms | Passed SLA: {}", loadTime, passedSLA);
        return passedSLA;
    }
}
