package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class ProfilePage {
    private WebDriver driver;
    private WebDriverUtils utils;

    // Profile header elements
    @FindBy(css = "[data-testid='ProfileHeader']")
    private WebElement profileHeader;

    @FindBy(css = "[itemprop='name']")
    private WebElement fullName;

    @FindBy(css = "[data-octo-dimension='user_profile_login']")
    private WebElement username;

    @FindBy(css = "[data-testid='bio']")
    private WebElement bio;

    @FindBy(css = "[data-testid='profile-link']")
    private List<WebElement> profileLinks;

    // Repository section
    @FindBy(css = "[data-filterable-for='your-repos-filter']")
    private WebElement repositoryFilter;

    @FindBy(css = "a[href*='repositories']")
    private WebElement repositoriesTab;

    @FindBy(css = "[data-filterable-for='pinned-items-list'] a")
    private List<WebElement> pinnedRepositories;

    // Activity section
    @FindBy(css = "a[href*='activity']")
    private WebElement activityTab;

    @FindBy(css = "[data-testid='contribution-graph']")
    private WebElement contributionGraph;

    // Followers/Following
    @FindBy(css = "a[href*='followers']")
    private WebElement followersLink;

    @FindBy(css = "a[href*='following']")
    private WebElement followingLink;

    @FindBy(xpath = "//a[contains(@href, 'followers')]/preceding-sibling::span")
    private WebElement followersCount;

    @FindBy(xpath = "//a[contains(@href, 'following')]/preceding-sibling::span")
    private WebElement followingCount;

    // Social links
    @FindBy(css = "[rel='me']")
    private List<WebElement> socialLinks;

    // Location & Company
    @FindBy(xpath = "//svg[@aria-label='Location']/../..")
    private WebElement locationElement;

    @FindBy(xpath = "//svg[@aria-label='Organization']/../..")
    private WebElement companyElement;

    // Edit profile button
    @FindBy(xpath = "//a[contains(@href, 'settings')]")
    private WebElement editProfileButton;

    public ProfilePage(WebDriver driver, WebDriverUtils utils) {
        this.driver = driver;
        this.utils = utils;
        PageFactory.initElements(driver, this);
    }

    public boolean isProfileHeaderDisplayed() {
        return utils.isElementDisplayed(profileHeader);
    }

    public String getFullName() {
        return utils.getElementText(fullName);
    }

    public String getUsername() {
        return utils.getElementText(username);
    }

    public String getBio() {
        return utils.getElementText(bio);
    }

    public boolean isBioDisplayed() {
        return utils.isElementDisplayed(bio);
    }

    public int getPinnedRepositoriesCount() {
        return pinnedRepositories.size();
    }

    public List<String> getPinnedRepositoryNames() {
        return pinnedRepositories.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isPinnedRepositoriesVisible() {
        return pinnedRepositories.size() > 0;
    }

    public void clickRepositoriesTab() {
        utils.click(repositoriesTab);
    }

    public void clickActivityTab() {
        utils.click(activityTab);
    }

    public boolean isContributionGraphDisplayed() {
        return utils.isElementDisplayed(contributionGraph);
    }

    public void clickFollowersLink() {
        utils.click(followersLink);
    }

    public void clickFollowingLink() {
        utils.click(followingLink);
    }

    public String getFollowersCount() {
        return utils.getElementText(followersCount);
    }

    public String getFollowingCount() {
        return utils.getElementText(followingCount);
    }

    public int getSocialLinksCount() {
        return socialLinks.size();
    }

    public List<String> getSocialLinkUrls() {
        return socialLinks.stream()
                .map(link -> link.getAttribute("href"))
                .toList();
    }

    public String getLocation() {
        return utils.getElementText(locationElement);
    }

    public String getCompany() {
        return utils.getElementText(companyElement);
    }

    public boolean isLocationDisplayed() {
        return utils.isElementDisplayed(locationElement);
    }

    public boolean isCompanyDisplayed() {
        return utils.isElementDisplayed(companyElement);
    }

    public void clickEditProfileButton() {
        utils.click(editProfileButton);
    }

    public boolean isEditProfileButtonVisible() {
        return utils.isElementDisplayed(editProfileButton);
    }

    public void navigateToProfile(String username) {
        driver.get("https://github.com/" + username);
        utils.waitForPageLoad(5);
    }

    public String getCurrentProfileURL() {
        return driver.getCurrentUrl();
    }

    public boolean isProfilePageLoaded() {
        return utils.isElementDisplayed(profileHeader) &&
               utils.isElementDisplayed(username);
    }
}
