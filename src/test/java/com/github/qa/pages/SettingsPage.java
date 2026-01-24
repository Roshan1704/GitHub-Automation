package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class SettingsPage {
    private WebDriver driver;
    private WebDriverUtils utils;

    // Settings sidebar
    @FindBy(css = "[data-testid='settings-menu-item']")
    private List<WebElement> settingsMenuItems;

    // Profile section
    @FindBy(xpath = "//a[contains(@href, '/settings/profile')]")
    private WebElement profileMenu;

    @FindBy(xpath = "//input[@id='user_profile_name']")
    private WebElement nameInput;

    @FindBy(xpath = "//textarea[@id='user_profile_bio']")
    private WebElement bioInput;

    // Account security
    @FindBy(xpath = "//a[contains(@href, '/settings/security')]")
    private WebElement securityMenu;

    @FindBy(xpath = "//button[contains(text(), 'Change password')]")
    private WebElement changePasswordButton;

    // SSH keys
    @FindBy(xpath = "//a[contains(@href, '/settings/keys')]")
    private WebElement sshKeysMenu;

    @FindBy(xpath = "//button[contains(text(), 'New SSH key')]")
    private WebElement newSSHKeyButton;

    @FindBy(css = "[data-testid='ssh-key-row']")
    private List<WebElement> sshKeys;

    // Personal access tokens
    @FindBy(xpath = "//a[contains(@href, '/settings/tokens')]")
    private WebElement tokensMenu;

    @FindBy(xpath = "//button[contains(text(), 'Generate new token')]")
    private WebElement generateTokenButton;

    @FindBy(css = "[data-testid='token-row']")
    private List<WebElement> tokens;

    // Developer settings
    @FindBy(xpath = "//a[contains(@href, '/settings/apps')]")
    private WebElement applicationsMenu;

    @FindBy(xpath = "//button[contains(text(), 'New OAuth App')]")
    private WebElement newOAuthAppButton;

    // Notifications settings
    @FindBy(xpath = "//a[contains(@href, '/settings/notifications')]")
    private WebElement notificationsMenu;

    @FindBy(xpath = "//input[@type='checkbox']")
    private List<WebElement> notificationCheckboxes;

    // Privacy settings
    @FindBy(xpath = "//a[contains(@href, '/settings/privacy')]")
    private WebElement privacyMenu;

    // Billing
    @FindBy(xpath = "//a[contains(@href, '/settings/billing')]")
    private WebElement billingMenu;

    // Sessions
    @FindBy(xpath = "//a[contains(@href, '/settings/sessions')]")
    private WebElement sessionsMenu;

    @FindBy(css = "[data-testid='session-item']")
    private List<WebElement> sessions;

    // Save button
    @FindBy(xpath = "//button[contains(text(), 'Update profile')]")
    private WebElement updateProfileButton;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Save')]")
    private WebElement saveButton;

    public SettingsPage(WebDriver driver, WebDriverUtils utils) {
        this.driver = driver;
        this.utils = utils;
        PageFactory.initElements(driver, this);
    }

    public int getSettingsMenuItemsCount() {
        return settingsMenuItems.size();
    }

    public List<String> getSettingsMenuItems() {
        return settingsMenuItems.stream()
                .map(WebElement::getText)
                .toList();
    }

    public void clickProfileMenu() {
        utils.click(profileMenu);
        utils.waitForPageLoad(3);
    }

    public void clickSecurityMenu() {
        utils.click(securityMenu);
        utils.waitForPageLoad(3);
    }

    public void clickSSHKeysMenu() {
        utils.click(sshKeysMenu);
        utils.waitForPageLoad(3);
    }

    public void clickTokensMenu() {
        utils.click(tokensMenu);
        utils.waitForPageLoad(3);
    }

    public void clickApplicationsMenu() {
        utils.click(applicationsMenu);
        utils.waitForPageLoad(3);
    }

    public void clickNotificationsMenu() {
        utils.click(notificationsMenu);
        utils.waitForPageLoad(3);
    }

    public void clickPrivacyMenu() {
        utils.click(privacyMenu);
        utils.waitForPageLoad(3);
    }

    public void clickBillingMenu() {
        utils.click(billingMenu);
        utils.waitForPageLoad(3);
    }

    public void clickSessionsMenu() {
        utils.click(sessionsMenu);
        utils.waitForPageLoad(3);
    }

    public void updateProfileName(String name) {
        utils.click(nameInput);
        utils.clearInput(nameInput);
        utils.sendKeys(nameInput, name);
    }

    public String getProfileName() {
        return utils.getElementAttribute(nameInput, "value");
    }

    public void updateProfileBio(String bio) {
        utils.click(bioInput);
        utils.clearInput(bioInput);
        utils.sendKeys(bioInput, bio);
    }

    public String getProfileBio() {
        return utils.getElementAttribute(bioInput, "value");
    }

    public void saveProfileChanges() {
        utils.click(updateProfileButton);
        utils.waitForPageLoad(3);
    }

    public void clickChangePasswordButton() {
        utils.click(changePasswordButton);
    }

    public void clickNewSSHKeyButton() {
        utils.click(newSSHKeyButton);
    }

    public int getSSHKeysCount() {
        return sshKeys.size();
    }

    public void clickGenerateTokenButton() {
        utils.click(generateTokenButton);
    }

    public int getTokensCount() {
        return tokens.size();
    }

    public void clickNewOAuthAppButton() {
        utils.click(newOAuthAppButton);
    }

    public int getNotificationCheckboxesCount() {
        return notificationCheckboxes.size();
    }

    public int getSessionsCount() {
        return sessions.size();
    }

    public void navigateToSettings() {
        driver.get("https://github.com/settings/profile");
        utils.waitForPageLoad(5);
    }

    public boolean isSettingsPageLoaded() {
        return driver.getCurrentUrl().contains("/settings");
    }

    public String getCurrentSettingsPage() {
        return driver.getCurrentUrl();
    }
}
