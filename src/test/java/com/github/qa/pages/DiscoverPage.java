package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class DiscoverPage {
    private WebDriver driver;
    private WebDriverUtils utils;

    // Trending tab
    @FindBy(xpath = "//a[contains(@href, '/trending')]")
    private WebElement trendingTab;

    // Trending repositories
    @FindBy(css = "[data-testid='Box-row']")
    private List<WebElement> trendingRepositories;

    @FindBy(xpath = "//a[@data-testid='search-title']")
    private List<WebElement> repoNames;

    // Language filter
    @FindBy(xpath = "//details[contains(@class, 'language')]")
    private WebElement languageSelector;

    @FindBy(xpath = "//input[@aria-label='Search languages']")
    private WebElement languageSearch;

    @FindBy(css = "[data-testid='language-option']")
    private List<WebElement> languageOptions;

    // Date range filter
    @FindBy(xpath = "//summary[contains(text(), 'Spoken language')]/..//button")
    private WebElement dateRangeButton;

    // Trending topics
    @FindBy(xpath = "//a[contains(@href, '/topics')]")
    private WebElement topicsTab;

    @FindBy(css = "[data-testid='topic-item']")
    private List<WebElement> topics;

    // Collections tab
    @FindBy(xpath = "//a[contains(@href, '/collections')]")
    private WebElement collectionsTab;

    @FindBy(css = "[data-testid='collection-item']")
    private List<WebElement> collections;

    // Explore tab
    @FindBy(xpath = "//a[contains(@href, '/explore')]")
    private WebElement exploreTab;

    // Repository cards
    @FindBy(css = "[data-testid='repo-card']")
    private List<WebElement> repositoryCards;

    // Stars count
    @FindBy(xpath = "//a[contains(@href, '/stargazers')]")
    private List<WebElement> starsElements;

    // Fork count
    @FindBy(xpath = "//span[contains(text(), 'Fork')]")
    private List<WebElement> forksElements;

    // Built by section
    @FindBy(xpath = "//div[contains(text(), 'Built by')]")
    private List<WebElement> builtByElements;

    public DiscoverPage(WebDriver driver, WebDriverUtils utils) {
        this.driver = driver;
        this.utils = utils;
        PageFactory.initElements(driver, this);
    }

    public void clickTrendingTab() {
        utils.click(trendingTab);
        utils.waitForPageLoad(5);
    }

    public void clickTopicsTab() {
        utils.click(topicsTab);
        utils.waitForPageLoad(5);
    }

    public void clickCollectionsTab() {
        utils.click(collectionsTab);
        utils.waitForPageLoad(5);
    }

    public void clickExploreTab() {
        utils.click(exploreTab);
        utils.waitForPageLoad(5);
    }

    public int getTrendingRepositoriesCount() {
        return trendingRepositories.size();
    }

    public List<String> getTrendingRepositoryNames() {
        return repoNames.stream()
                .map(WebElement::getText)
                .limit(getTrendingRepositoriesCount())
                .toList();
    }

    public boolean areTrendingRepositoriesDisplayed() {
        return trendingRepositories.size() > 0;
    }

    public void selectLanguage(String language) {
        utils.click(languageSelector);
        utils.waitForPageLoad(2);
        utils.sendKeys(languageSearch, language);
        utils.waitForPageLoad(2);
        List<WebElement> options = driver.findElements(
            By.cssSelector("[data-testid='language-option']")
        );
        if (!options.isEmpty()) {
            utils.click(options.get(0));
            utils.waitForPageLoad(3);
        }
    }

    public int getLanguageOptionsCount() {
        return languageOptions.size();
    }

    public void selectDateRange(String range) {
        utils.click(dateRangeButton);
        List<WebElement> options = driver.findElements(
            By.xpath("//a[contains(text(), '" + range + "')]")
        );
        if (!options.isEmpty()) {
            utils.click(options.get(0));
            utils.waitForPageLoad(3);
        }
    }

    public int getTopicsCount() {
        return topics.size();
    }

    public List<String> getTopicNames() {
        return topics.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getCollectionsCount() {
        return collections.size();
    }

    public List<String> getCollectionNames() {
        return collections.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getRepositoryCardsCount() {
        return repositoryCards.size();
    }

    public List<String> getRepositoryCardNames() {
        return repositoryCards.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getStarsElementsCount() {
        return starsElements.size();
    }

    public int getForksElementsCount() {
        return forksElements.size();
    }

    public int getBuiltByElementsCount() {
        return builtByElements.size();
    }

    public void navigateToTrending() {
        driver.get("https://github.com/trending");
        utils.waitForPageLoad(5);
    }

    public void navigateToTopics() {
        driver.get("https://github.com/topics");
        utils.waitForPageLoad(5);
    }

    public void navigateToCollections() {
        driver.get("https://github.com/collections");
        utils.waitForPageLoad(5);
    }

    public void navigateToExplore() {
        driver.get("https://github.com/explore");
        utils.waitForPageLoad(5);
    }

    public boolean isTrendingPageLoaded() {
        return driver.getCurrentUrl().contains("trending");
    }

    public void clickFirstRepository() {
        if (!trendingRepositories.isEmpty()) {
            utils.click(trendingRepositories.get(0));
            utils.waitForPageLoad(5);
        }
    }

    public void clickRepositoryByIndex(int index) {
        if (index < trendingRepositories.size()) {
            utils.click(trendingRepositories.get(index));
            utils.waitForPageLoad(5);
        }
    }
}
