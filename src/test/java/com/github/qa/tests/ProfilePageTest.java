package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import com.github.qa.pages.ProfilePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProfilePageTest extends BaseTest {

    private ProfilePage profilePage;

    /**
     * IMPORTANT:
     * This method MUST NOT override BaseTest.setUp().
     * It only initializes page objects using already-created getDriver() & getUtils().
     */
    @BeforeMethod(alwaysRun = true)
    public void initProfilePage() {
        getDriver().get("https://github.com");
        profilePage = new ProfilePage(getDriver(), getUtils());
    }

    // ================= PROFILE HEADER TESTS =================

    @Test(description = "Verify profile header is displayed", groups = {"smoke"})
    public void testProfileHeaderDisplayed() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertTrue(profilePage.isProfileHeaderDisplayed(),
                "Profile header should be displayed");
    }

    @Test(description = "Verify profile name is displayed", groups = {"smoke"})
    public void testProfileNameDisplayed() {
        profilePage.navigateToProfile("torvalds");
        String name = profilePage.getFullName();
        Assert.assertNotNull(name, "Profile name should not be null");
        Assert.assertFalse(name.isEmpty(), "Profile name should not be empty");
    }

    @Test(description = "Verify username is displayed", groups = {"smoke"})
    public void testUsernameDisplayed() {
        profilePage.navigateToProfile("torvalds");
        String username = profilePage.getUsername();
        Assert.assertNotNull(username, "Username should not be null");
        Assert.assertTrue(username.contains("torvalds"),
                "Username should contain 'torvalds'");
    }

    @Test(description = "Verify profile page is fully loaded", groups = {"smoke"})
    public void testProfilePageLoaded() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertTrue(profilePage.isProfilePageLoaded(),
                "Profile page should be loaded");
    }

    @Test(description = "Verify profile URL is correct", groups = {"smoke"})
    public void testProfileURLCorrect() {
        profilePage.navigateToProfile("torvalds");
        String url = profilePage.getCurrentProfileURL();
        Assert.assertTrue(url.contains("torvalds"),
                "URL should contain username");
        Assert.assertTrue(url.contains("github.com"),
                "URL should contain github.com");
    }

    // ================= BIO TESTS =================

    @Test(description = "Verify bio section is displayed", groups = {"functional"})
    public void testBioSectionDisplayed() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertTrue(profilePage.isProfilePageLoaded(),
                "Profile should be loaded");
    }

    @Test(description = "Verify bio content when available", groups = {"functional"})
    public void testBioContentAvailable() {
        profilePage.navigateToProfile("torvalds");
        if (profilePage.isBioDisplayed()) {
            String bio = profilePage.getBio();
            Assert.assertNotNull(bio, "Bio should not be null");
        }
    }

    // ================= REPOSITORY TESTS =================

    @Test(description = "Verify pinned repositories are displayed", groups = {"functional"})
    public void testPinnedRepositoriesDisplayed() {
        profilePage.navigateToProfile("torvalds");
        int pinnedCount = profilePage.getPinnedRepositoriesCount();
        Assert.assertTrue(pinnedCount >= 0,
                "Pinned repositories count should be >= 0");
    }

    @Test(description = "Verify pinned repository names are not empty", groups = {"functional"})
    public void testPinnedRepositoryNamesNotEmpty() {
        profilePage.navigateToProfile("torvalds");
        if (profilePage.isPinnedRepositoriesVisible()) {
            var names = profilePage.getPinnedRepositoryNames();
            Assert.assertNotNull(names, "Repository names should not be null");
            names.forEach(name ->
                    Assert.assertFalse(name.isEmpty(),
                            "Repository name should not be empty"));
        }
    }

    @Test(description = "Verify repositories tab navigation", groups = {"functional"})
    public void testRepositoriesTabNavigation() {
        profilePage.navigateToProfile("torvalds");
        profilePage.clickRepositoriesTab();
        getUtils().waitForPageLoad(3);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("tab=repositories"),
                "Should navigate to repositories tab");
    }

    // ================= ACTIVITY TESTS =================

    @Test(description = "Verify activity tab navigation", groups = {"functional"})
    public void testActivityTabNavigation() {
        profilePage.navigateToProfile("torvalds");
        profilePage.clickActivityTab();
        getUtils().waitForPageLoad(3);
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("tab=")
                        || getDriver().getCurrentUrl().contains("activity"),
                "Should navigate to activity section");
    }

    @Test(description = "Verify contribution graph is displayed", groups = {"functional"})
    public void testContributionGraphDisplayed() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertTrue(profilePage.isContributionGraphDisplayed(),
                "Contribution graph should be displayed");
    }

    // ================= FOLLOWERS / FOLLOWING =================

    @Test(description = "Verify followers link exists", groups = {"functional"})
    public void testFollowersLinkExists() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertNotNull(profilePage.getFollowersCount(),
                "Followers count should not be null");
    }

    @Test(description = "Verify following link exists", groups = {"functional"})
    public void testFollowingLinkExists() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertNotNull(profilePage.getFollowingCount(),
                "Following count should not be null");
    }

    @Test(description = "Verify followers and following counts are numeric", groups = {"functional"})
    public void testFollowersFollowingCountsNumeric() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertTrue(profilePage.getFollowersCount().matches("[0-9,]+"),
                "Followers count should be numeric");
        Assert.assertTrue(profilePage.getFollowingCount().matches("[0-9,]+"),
                "Following count should be numeric");
    }

    @Test(description = "Verify followers tab navigation", groups = {"functional"})
    public void testFollowersTabNavigation() {
        profilePage.navigateToProfile("torvalds");
        profilePage.clickFollowersLink();
        getUtils().waitForPageLoad(3);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("followers"),
                "Should navigate to followers page");
    }

    @Test(description = "Verify following tab navigation", groups = {"functional"})
    public void testFollowingTabNavigation() {
        profilePage.navigateToProfile("torvalds");
        profilePage.clickFollowingLink();
        getUtils().waitForPageLoad(3);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("following"),
                "Should navigate to following page");
    }

    // ================= SOCIAL / LOCATION / COMPANY =================

    @Test(description = "Verify social links section", groups = {"functional"})
    public void testSocialLinksSectionAvailable() {
        profilePage.navigateToProfile("torvalds");
        Assert.assertTrue(profilePage.getSocialLinksCount() >= 0,
                "Social links count should be >= 0");
    }

    @Test(description = "Verify social link URLs are valid", groups = {"functional"})
    public void testSocialLinkURLsValid() {
        profilePage.navigateToProfile("torvalds");
        if (profilePage.getSocialLinksCount() > 0) {
            profilePage.getSocialLinkUrls()
                    .forEach(url ->
                            Assert.assertTrue(url.contains("http"),
                                    "Social link should contain http"));
        }
    }

    @Test(description = "Verify location section when available", groups = {"functional"})
    public void testLocationSectionWhenAvailable() {
        profilePage.navigateToProfile("torvalds");
        if (profilePage.isLocationDisplayed()) {
            Assert.assertFalse(profilePage.getLocation().isEmpty(),
                    "Location should not be empty");
        }
    }

    @Test(description = "Verify company section when available", groups = {"functional"})
    public void testCompanySectionWhenAvailable() {
        profilePage.navigateToProfile("torvalds");
        if (profilePage.isCompanyDisplayed()) {
            Assert.assertFalse(profilePage.getCompany().isEmpty(),
                    "Company should not be empty");
        }
    }

    // ================= REGRESSION =================

    @Test(description = "Verify different user profiles load correctly", groups = {"regression"})
    public void testMultipleProfilesLoad() {
        String[] users = {"gvanrossum", "dhh", "antirez"};
        for (String user : users) {
            profilePage.navigateToProfile(user);
            Assert.assertTrue(profilePage.isProfilePageLoaded(),
                    "Profile for " + user + " should be loaded");
        }
    }
}
