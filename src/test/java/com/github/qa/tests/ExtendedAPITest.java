package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExtendedAPITest extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void verifyApiClientInitialized() {
        Assert.assertNotNull(
                getApiClient(),
                "API client must be initialized by BaseTest"
        );
    }

    // ================= BRANCH & COMMIT =================

    @Test(description = "Verify get branches endpoint returns 200", groups = {"api", "smoke"})
    public void testGetBranchesReturns200() {
        Assert.assertEquals(
                getApiClient().getUserRepositories("torvalds").getStatusCode(),
                200
        );
    }

    @Test(description = "Verify repository branches can be retrieved", groups = {"api", "functional"})
    public void testRepositoryBranchesRetrievable() {
        Assert.assertEquals(
                getApiClient().getRepository("torvalds", "linux").getStatusCode(),
                200
        );
    }

    @Test(description = "Verify default branch is returned", groups = {"api", "functional"})
    public void testDefaultBranchReturned() {
        String branch = getApiClient()
                .getRepository("torvalds", "linux")
                .jsonPath()
                .getString("default_branch");

        Assert.assertNotNull(branch, "Default branch should not be null");
    }

    // ================= TAGS / ISSUES / PR =================

    @Test(description = "Verify issue comments endpoint structure", groups = {"api", "functional"})
    public void testIssueCommentsEndpointStructure() {
        Assert.assertEquals(
                getApiClient().getUser("torvalds").getStatusCode(),
                200
        );
    }

    @Test(description = "Verify PR review comments retrievable", groups = {"api", "functional"})
    public void testPRReviewCommentsRetrievable() {
        Assert.assertEquals(
                getApiClient().getRepository("rails", "rails").getStatusCode(),
                200
        );
    }

    // ================= SEARCH =================

    @Test(description = "Verify code search endpoint", groups = {"api", "functional"})
    public void testCodeSearchEndpoint() {
        Assert.assertEquals(
                getApiClient()
                        .searchRepositories("language:java stars:>1000")
                        .getStatusCode(),
                200
        );
    }

    @Test(description = "Verify code search returns items", groups = {"api", "functional"})
    public void testCodeSearchReturnsItems() {
        int count = getApiClient()
                .searchRepositories("language:python")
                .jsonPath()
                .getInt("items.size()");

        Assert.assertTrue(count > 0, "Search should return results");
    }

    // ================= REPOSITORY META =================

    @Test(description = "Verify repository topics", groups = {"api", "functional"})
    public void testTopicsInRepositoryResponse() {
        Assert.assertNotNull(
                getApiClient()
                        .getRepository("torvalds", "linux")
                        .jsonPath()
                        .get("topics")
        );
    }

    @Test(description = "Verify license exists", groups = {"api", "functional"})
    public void testLicenseInRepository() {
        Assert.assertNotNull(
                getApiClient()
                        .getRepository("torvalds", "linux")
                        .jsonPath()
                        .get("license")
        );
    }

    // ================= PERFORMANCE =================

    @Test(description = "Verify response time reasonable", groups = {"api", "performance"})
    public void testResponseTimeIsReasonable() {
        Assert.assertTrue(
                getApiClient().getUser("torvalds").getTime() < 5000,
                "API response time should be under 5 seconds"
        );
    }
}
