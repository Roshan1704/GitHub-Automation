package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Page Object for GitHub Search Results Page
 */
public class SearchPage {
    private static final Logger logger = LoggerFactory.getLogger(SearchPage.class);
    private final WebDriver driver;
    private final int TIMEOUT = 15;

    // Locators
    private final By searchInput = By.xpath("//input[@placeholder='Search GitHub']");
    private final By resultsContainer = By.id("search_results");
    private final By repositoryItems = By.xpath("//div[@data-testid='results-list']//div[@data-testid='repository-list-item']");
    private final By filterButton = By.xpath("//button[contains(text(), 'Type')]");
    private final By sortDropdown = By.xpath("//select[@aria-label='Sort']");
    private final By emptyState = By.xpath("//p[contains(text(), 'No repositories matched')]");
    private final By resultCount = By.xpath("//span[@class='color-text-secondary']");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Search for: {query}")
    public void searchFor(String query) {
        WebDriverUtils.sendKeys(driver, searchInput, query, TIMEOUT);
        driver.findElement(searchInput).submit();
        WebDriverUtils.waitForPageLoad(driver, TIMEOUT);
        logger.info("Searched for: {}", query);
    }

    @Step("Verify search results are displayed")
    public boolean areSearchResultsDisplayed() {
        try {
            List<WebElement> results = WebDriverUtils.waitForElementsVisibility(driver, repositoryItems, TIMEOUT);
            boolean hasResults = !results.isEmpty();
            logger.info("Search results displayed: {} | Count: {}", hasResults, results.size());
            return hasResults;
        } catch (Exception e) {
            logger.warn("No results found: {}", e.getMessage());
            return false;
        }
    }

    @Step("Get number of search results")
    public int getSearchResultCount() {
        try {
            List<WebElement> results = driver.findElements(repositoryItems);
            logger.info("Total results found: {}", results.size());
            return results.size();
        } catch (Exception e) {
            logger.warn("Could not count results: {}", e.getMessage());
            return 0;
        }
    }

    @Step("Verify no results message is displayed")
    public boolean isNoResultsMessageDisplayed() {
        boolean isDisplayed = WebDriverUtils.isElementDisplayed(driver, emptyState);
        logger.info("No results message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Verify filters are available")
    public boolean areFiltersAvailable() {
        boolean filtersAvailable = WebDriverUtils.isElementDisplayed(driver, filterButton);
        logger.info("Filters available: {}", filtersAvailable);
        return filtersAvailable;
    }

    @Step("Verify sort dropdown is available")
    public boolean isSortDropdownAvailable() {
        boolean sortAvailable = WebDriverUtils.isElementDisplayed(driver, sortDropdown);
        logger.info("Sort dropdown available: {}", sortAvailable);
        return sortAvailable;
    }

    @Step("Get first result text")
    public String getFirstResultText() {
        try {
            WebElement firstResult = driver.findElements(repositoryItems).get(0);
            String text = firstResult.getText();
            logger.info("First result: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Could not get first result: {}", e.getMessage());
            return "";
        }
    }

    @Step("Click on first search result")
    public void clickFirstResult() {
        try {
            List<WebElement> results = driver.findElements(repositoryItems);
            if (!results.isEmpty()) {
                results.get(0).click();
                WebDriverUtils.waitForPageLoad(driver, TIMEOUT);
                logger.info("First result clicked");
            }
        } catch (Exception e) {
            logger.error("Could not click first result: {}", e.getMessage());
            throw new RuntimeException("Failed to click first result");
        }
    }
}
