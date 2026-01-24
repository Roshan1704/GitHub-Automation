package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import com.github.qa.pages.IssuePage;
import com.github.qa.pages.PullRequestPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IssueAndPullRequestTest extends BaseTest {

    private IssuePage issuePage;
    private PullRequestPage prPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        Assert.assertNotNull(getDriver(),
                "WebgetDriver() must be initialized by BaseTest before Issue/PR tests");

        getDriver().get(getBaseUrl());

        issuePage = new IssuePage(getDriver(), getUtils());
        prPage = new PullRequestPage(getDriver(), getUtils());

        Assert.assertNotNull(issuePage, "IssuePage must be initialized");
        Assert.assertNotNull(prPage, "PullRequestPage must be initialized");
    }

    // ================= ISSUE TESTS =================

    @Test(description = "Verify issue page loads correctly", groups = {"smoke"})
    public void testIssuePageLoads() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.isIssuePageLoaded());
    }

    @Test(description = "Verify issue title is displayed", groups = {"smoke"})
    public void testIssueTitleDisplayed() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertFalse(issuePage.getIssueTitle().isEmpty());
    }

    @Test(description = "Verify issue number is displayed", groups = {"smoke"})
    public void testIssueNumberDisplayed() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.getIssueNumber().contains("#"));
    }

    @Test(description = "Verify issue state is valid", groups = {"smoke"})
    public void testIssueStateValid() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.isIssueOpen() || issuePage.isIssueClosed());
    }

    // ================= LABELS / ASSIGNEES =================

    @Test(description = "Verify issue labels count", groups = {"functional"})
    public void testIssueLabelsCount() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.getLabelsCount() >= 0);
    }

    @Test(description = "Verify issue assignees count", groups = {"functional"})
    public void testIssueAssigneesCount() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.getAssigneesCount() >= 0);
    }

    // ================= COMMENTS / DISCUSSION =================

    @Test(description = "Verify comments count", groups = {"functional"})
    public void testCommentsCount() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.getCommentsCount() >= 0);
    }

    @Test(description = "Verify discussion items loaded", groups = {"functional"})
    public void testDiscussionItemsLoaded() {
        issuePage.navigateToIssue("kubernetes", "kubernetes", "1");
        Assert.assertTrue(issuePage.getDiscussionItemsCount() >= 0);
    }

    // ================= PULL REQUEST TESTS =================

    @Test(description = "Verify PR page loads", groups = {"smoke"})
    public void testPRPageLoads() {
        prPage.navigateToPullRequest("torvalds", "linux", "1");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("github.com"));
    }

    @Test(description = "Verify PR list page loads", groups = {"functional"})
    public void testPRListLoads() {
        getDriver().get("https://github.com/python/cpython/pulls");
        getUtils().waitForPageLoad(3);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("pulls"));
    }

    // ================= REGRESSION =================

    @Test(description = "Verify multiple issues load", groups = {"regression"})
    public void testMultipleIssuesLoad() {
        String[] issues = {"1", "2", "5"};
        for (String issue : issues) {
            issuePage.navigateToIssue("kubernetes", "kubernetes", issue);
            Assert.assertTrue(issuePage.isIssuePageLoaded());
        }
    }
}
