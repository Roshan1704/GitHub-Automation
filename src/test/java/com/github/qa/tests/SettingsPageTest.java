package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import com.github.qa.pages.SettingsPage;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SettingsPageTest extends BaseTest {

    private SettingsPage settingsPage;

    @BeforeMethod(alwaysRun = true)
    public void setUpTest() {
        // BaseTest already initializes getDriver() + getUtils()
    	getDriver().get("https://github.com");
        settingsPage = new SettingsPage(getDriver(), getUtils());
    }

    /**
     * Utility method:
     * GitHub Settings require login.
     * If redirected to /login → skip test.
     */
    private void navigateOrSkipIfLoggedOut() {
        settingsPage.navigateToSettings();
        getUtils().waitForPageLoad(3);

        if (getDriver().getCurrentUrl().contains("/login")) {
            throw new SkipException(
                "Skipping test: GitHub Settings page requires authenticated user"
            );
        }
    }

    // ------------------- Smoke -------------------

    @Test(description = "Verify settings page loads (logged-in users only)", groups = {"smoke"})
    public void testSettingsPageLoads() {
        navigateOrSkipIfLoggedOut();
        Assert.assertTrue(
                settingsPage.isSettingsPageLoaded(),
                "Settings page should load for authenticated user"
        );
    }

    // ------------------- Menu Tests -------------------

    @Test(description = "Verify settings menu items exist", groups = {"functional"})
    public void testSettingsMenuItemsExist() {
        navigateOrSkipIfLoggedOut();
        int menuItemsCount = settingsPage.getSettingsMenuItemsCount();
        Assert.assertTrue(menuItemsCount > 0, "Settings menu should have items");
    }

    @Test(description = "Verify settings menu items are not empty", groups = {"functional"})
    public void testSettingsMenuItemsNotEmpty() {
        navigateOrSkipIfLoggedOut();
        settingsPage.getSettingsMenuItems()
                .forEach(item ->
                        Assert.assertFalse(item.isBlank(), "Menu item should not be empty")
                );
    }

    // ------------------- Profile Settings -------------------

    @Test(description = "Verify profile settings menu is clickable", groups = {"functional"})
    public void testProfileMenuClickable() {
        navigateOrSkipIfLoggedOut();
        settingsPage.clickProfileMenu();
        getUtils().waitForPageLoad(2);
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("profile"),
                "Should navigate to profile settings"
        );
    }

    // ------------------- Security -------------------

    @Test(description = "Verify security menu is clickable", groups = {"functional"})
    public void testSecurityMenuClickable() {
        navigateOrSkipIfLoggedOut();
        settingsPage.clickSecurityMenu();
        getUtils().waitForPageLoad(2);
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("security"),
                "Should navigate to security settings"
        );
    }

    // ------------------- SSH Keys -------------------

    @Test(description = "Verify SSH keys menu is clickable", groups = {"functional"})
    public void testSSHKeysMenuClickable() {
        navigateOrSkipIfLoggedOut();
        settingsPage.clickSSHKeysMenu();
        getUtils().waitForPageLoad(2);
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("keys"),
                "Should navigate to SSH keys page"
        );
    }

    @Test(description = "Verify SSH keys list is retrievable", groups = {"functional"})
    public void testSSHKeysListRetrievable() {
        navigateOrSkipIfLoggedOut();
        settingsPage.clickSSHKeysMenu();
        getUtils().waitForPageLoad(2);
        Assert.assertTrue(
                settingsPage.getSSHKeysCount() >= 0,
                "SSH keys count should be >= 0"
        );
    }

    // ------------------- Tokens -------------------

    @Test(description = "Verify tokens menu is clickable", groups = {"functional"})
    public void testTokensMenuClickable() {
        navigateOrSkipIfLoggedOut();
        settingsPage.clickTokensMenu();
        getUtils().waitForPageLoad(2);
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("tokens"),
                "Should navigate to tokens page"
        );
    }

    // ------------------- Notifications -------------------

    @Test(description = "Verify notifications menu is clickable", groups = {"functional"})
    public void testNotificationsMenuClickable() {
        navigateOrSkipIfLoggedOut();
        settingsPage.clickNotificationsMenu();
        getUtils().waitForPageLoad(2);
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("notifications"),
                "Should navigate to notifications settings"
        );
    }

    // ------------------- Regression -------------------

    @Test(description = "Verify settings page persists after refresh", groups = {"regression"})
    public void testSettingsNavigationPersistence() {
        navigateOrSkipIfLoggedOut();
        getDriver().navigate().refresh();
        getUtils().waitForPageLoad(3);

        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("settings"),
                "Should remain on settings page after refresh"
        );
    }
}
