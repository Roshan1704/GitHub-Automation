package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import com.github.qa.pages.DiscoverPage;
import com.github.qa.pages.NotificationsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DiscoverAndNotificationsTest extends BaseTest {

    private DiscoverPage discoverPage;
    private NotificationsPage notificationsPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        Assert.assertNotNull(getDriver(),
                "WebgetDriver() must be initialized by BaseTest before tests run");

        getDriver().get(getBaseUrl());

        discoverPage = new DiscoverPage(getDriver(), getUtils());
        notificationsPage = new NotificationsPage(getDriver(), getUtils());

        Assert.assertNotNull(discoverPage, "DiscoverPage should be initialized");
        Assert.assertNotNull(notificationsPage, "NotificationsPage should be initialized");
    }

    // ================= TRENDING TESTS =================

    @Test(description = "Verify trending page loads", groups = {"smoke"})
    public void testTrendingPageLoads() {
        discoverPage.navigateToTrending();
        Assert.assertTrue(discoverPage.isTrendingPageLoaded());
    }

    @Test(description = "Verify trending repositories are displayed", groups = {"smoke"})
    public void testTrendingRepositoriesDisplayed() {
        discoverPage.navigateToTrending();
        Assert.assertTrue(discoverPage.getTrendingRepositoriesCount() > 0);
    }

    @Test(description = "Verify trending repository names are not empty", groups = {"functional"})
    public void testTrendingRepoNamesNotEmpty() {
        discoverPage.navigateToTrending();
        discoverPage.getTrendingRepositoryNames()
                .forEach(name -> Assert.assertFalse(name.isEmpty()));
    }

    @Test(description = "Verify first trending repository is clickable", groups = {"functional"})
    public void testFirstRepositoryClickable() {
        discoverPage.navigateToTrending();
        discoverPage.clickFirstRepository();
        getUtils().waitForPageLoad(3);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("github.com"));
    }

    // ================= TOPICS TESTS =================

    @Test(description = "Verify topics page loads", groups = {"smoke"})
    public void testTopicsPageLoads() {
        discoverPage.navigateToTopics();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("topics"));
    }

    @Test(description = "Verify topics are displayed", groups = {"functional"})
    public void testTopicsDisplayed() {
        discoverPage.navigateToTopics();
        Assert.assertTrue(discoverPage.getTopicsCount() > 0);
    }

    // ================= COLLECTIONS TESTS =================

    @Test(description = "Verify collections page loads", groups = {"smoke"})
    public void testCollectionsPageLoads() {
        discoverPage.navigateToCollections();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("collections"));
    }

    @Test(description = "Verify collections are displayed", groups = {"functional"})
    public void testCollectionsDisplayed() {
        discoverPage.navigateToCollections();
        Assert.assertTrue(discoverPage.getCollectionsCount() > 0);
    }

    // ================= EXPLORE TEST =================

    @Test(description = "Verify explore page loads", groups = {"smoke"})
    public void testExplorePageLoads() {
        discoverPage.navigateToExplore();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("explore"));
    }

    // ================= NOTIFICATIONS TESTS =================

    @Test(description = "Verify notifications page loads", groups = {"smoke"})
    public void testNotificationsPageLoads() {
        notificationsPage.navigateToNotifications();
        Assert.assertTrue(notificationsPage.isNotificationsPageLoaded());
    }

    @Test(description = "Verify notification count is displayed", groups = {"functional"})
    public void testNotificationCountDisplayed() {
        notificationsPage.navigateToNotifications();
        Assert.assertNotNull(notificationsPage.getNotificationCount());
    }

    // ================= REGRESSION =================

    @Test(description = "Verify all discover tabs are accessible", groups = {"regression"})
    public void testAllDiscoverTabsAccessible() {
        discoverPage.navigateToTrending();
        discoverPage.navigateToTopics();
        discoverPage.navigateToCollections();
        discoverPage.navigateToExplore();
    }
}
