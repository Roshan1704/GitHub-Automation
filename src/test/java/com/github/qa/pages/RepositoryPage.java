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
 * Page Object for GitHub Repository Page
 */
public class RepositoryPage {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryPage.class);
    private final WebDriver driver;
    private final int TIMEOUT = 15;

    // Locators
    private final By repositoryName = By.xpath("//h1//a[@data-testid='repo-header-repo']");
    private final By description = By.xpath("//p[@data-testid='repo-description']");
    private final By starButton = By.xpath("//a[contains(text(), 'Star')]");
    private final By forksCount = By.xpath("//a[contains(text(), 'Forks')]");
    private final By filesList = By.xpath("//div[@data-testid='repo-file-row']");
    private final By readmeContent = By.xpath("//article[@data-testid='readme-content']");
    private final By codeTab = By.xpath("//a[contains(text(), 'Code')]");
    private final By issuesTab = By.xpath("//a[contains(text(), 'Issues')]");
    private final By pullRequestsTab = By.xpath("//a[contains(text(), 'Pull requests')]");
    private final By aboutSection = By.xpath("//h3[contains(text(), 'About')]");

    public RepositoryPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Load repository page with URL: {url}")
    public void load(String url) {
        driver.get(url);
        WebDriverUtils.waitForPageLoad(driver, TIMEOUT);
        logger.info("Repository page loaded: {}", url);
    }

    @Step("Verify repository page is displayed")
    public boolean isRepositoryPageDisplayed() {
        boolean isDisplayed = WebDriverUtils.isElementDisplayed(driver, repositoryName);
        logger.info("Repository page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get repository name")
    public String getRepositoryName() {
        String name = WebDriverUtils.getText(driver, repositoryName, TIMEOUT);
        logger.info("Repository name: {}", name);
        return name;
    }

    @Step("Verify description is present")
    public boolean isDescriptionPresent() {
        boolean isPresent = WebDriverUtils.isElementDisplayed(driver, description);
        logger.info("Description present: {}", isPresent);
        return isPresent;
    }

    @Step("Get repository description")
    public String getDescription() {
        try {
            String desc = WebDriverUtils.getText(driver, description, TIMEOUT);
            logger.info("Description: {}", desc);
            return desc;
        } catch (Exception e) {
            logger.warn("Could not retrieve description: {}", e.getMessage());
            return "";
        }
    }

    @Step("Verify star button is visible")
    public boolean isStarButtonVisible() {
        boolean isVisible = WebDriverUtils.isElementDisplayed(driver, starButton);
        logger.info("Star button visible: {}", isVisible);
        return isVisible;
    }

    @Step("Verify repository files are displayed")
    public boolean areFilesDisplayed() {
        try {
            List<WebElement> files = driver.findElements(filesList);
            boolean hasFiles = !files.isEmpty();
            logger.info("Files displayed: {} | Count: {}", hasFiles, files.size());
            return hasFiles;
        } catch (Exception e) {
            logger.warn("Could not retrieve files: {}", e.getMessage());
            return false;
        }
    }

    @Step("Get number of files in main view")
    public int getFileCount() {
        try {
            List<WebElement> files = driver.findElements(filesList);
            logger.info("File count: {}", files.size());
            return files.size();
        } catch (Exception e) {
            logger.warn("Could not count files: {}", e.getMessage());
            return 0;
        }
    }

    @Step("Verify README is present")
    public boolean isReadmePresent() {
        boolean isPresent = WebDriverUtils.isElementDisplayed(driver, readmeContent);
        logger.info("README present: {}", isPresent);
        return isPresent;
    }

    @Step("Verify Code tab is clickable")
    public boolean isCodeTabClickable() {
        boolean isClickable = WebDriverUtils.isElementDisplayed(driver, codeTab);
        logger.info("Code tab clickable: {}", isClickable);
        return isClickable;
    }

    @Step("Verify Issues tab is available")
    public boolean isIssuesTabAvailable() {
        boolean isAvailable = WebDriverUtils.isElementDisplayed(driver, issuesTab);
        logger.info("Issues tab available: {}", isAvailable);
        return isAvailable;
    }

    @Step("Verify Pull Requests tab is available")
    public boolean isPullRequestsTabAvailable() {
        boolean isAvailable = WebDriverUtils.isElementDisplayed(driver, pullRequestsTab);
        logger.info("Pull Requests tab available: {}", isAvailable);
        return isAvailable;
    }

    @Step("Verify About section is displayed")
    public boolean isAboutSectionDisplayed() {
        boolean isDisplayed = WebDriverUtils.isElementDisplayed(driver, aboutSection);
        logger.info("About section displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Check page load time for SLA (3 seconds)")
    public boolean pageLoadsSLA() {
        long startTime = System.currentTimeMillis();
        WebDriverUtils.waitForPageLoad(driver, TIMEOUT);
        long loadTime = System.currentTimeMillis() - startTime;
        boolean passedSLA = loadTime < 3000;
        logger.info("Page load time: {}ms | Passed SLA: {}", loadTime, passedSLA);
        return passedSLA;
    }
}
