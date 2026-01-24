package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ComprehensiveAPITest extends BaseTest {

    private static final String[] PUBLIC_USERS =
            {"torvalds", "gvanrossum", "dhh", "antirez", "mojombo"};

    private static final String[] PUBLIC_REPOS =
            {"linux", "cpython", "rails", "redis", "grit"};

    @BeforeMethod(alwaysRun = true)
    public void verifyApiClientInitialized() {
        Assert.assertNotNull(
                getApiClient(),
                "API client must be initialized by BaseTest"
        );
    }

    // ================= USER API TESTS =================

    @Test(description = "Verify user endpoint returns 200 for valid user", groups = {"api", "smoke"})
    public void testGetValidUserReturns200() {
        Response response = getApiClient().getUser("torvalds");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "Verify user response contains login field", groups = {"api", "smoke"})
    public void testUserResponseContainsLogin() {
        Assert.assertEquals(
                getApiClient().getUser("torvalds").jsonPath().getString("login"),
                "torvalds"
        );
    }

    @Test(description = "Verify user response contains id field", groups = {"api", "functional"})
    public void testUserResponseContainsId() {
        Assert.assertTrue(
                getApiClient().getUser("torvalds").jsonPath().getInt("id") > 0
        );
    }

    @Test(description = "Verify user response contains public_repos field", groups = {"api", "functional"})
    public void testUserResponseContainsPublicRepos() {
        Assert.assertTrue(
                getApiClient().getUser("torvalds")
                        .jsonPath().getInt("public_repos") >= 0
        );
    }

    @Test(description = "Verify user response contains followers field", groups = {"api", "functional"})
    public void testUserResponseContainsFollowers() {
        Assert.assertTrue(
                getApiClient().getUser("torvalds")
                        .jsonPath().getInt("followers") >= 0
        );
    }

    @Test(description = "Verify user response contains following field", groups = {"api", "functional"})
    public void testUserResponseContainsFollowing() {
        Assert.assertTrue(
                getApiClient().getUser("torvalds")
                        .jsonPath().getInt("following") >= 0
        );
    }

    @Test(description = "Verify multiple valid users return 200", groups = {"api", "regression"})
    public void testMultipleValidUsersReturn200() {
        for (String user : PUBLIC_USERS) {
            Assert.assertEquals(
                    getApiClient().getUser(user).getStatusCode(),
                    200
            );
        }
    }

    @Test(description = "Verify invalid user returns 404", groups = {"api", "negative"})
    public void testGetInvalidUserReturns404() {
        Assert.assertEquals(
                getApiClient().getUser("nonexistentuser123456").getStatusCode(),
                404
        );
    }

    // ================= REPOSITORY API TESTS =================

    @Test(description = "Verify valid repo returns 200", groups = {"api", "smoke"})
    public void testGetValidRepoReturns200() {
        Assert.assertEquals(
                getApiClient().getRepository("torvalds", "linux").getStatusCode(),
                200
        );
    }

    @Test(description = "Verify repo owner is correct", groups = {"api", "functional"})
    public void testRepoResponseContainsOwner() {
        Assert.assertEquals(
                getApiClient()
                        .getRepository("torvalds", "linux")
                        .jsonPath()
                        .getString("owner.login"),
                "torvalds"
        );
    }

    @Test(description = "Verify invalid repo returns 404", groups = {"api", "negative"})
    public void testGetInvalidRepoReturns404() {
        Assert.assertEquals(
                getApiClient()
                        .getRepository("torvalds", "nonexistentrepo123")
                        .getStatusCode(),
                404
        );
    }

    // ================= SEARCH API TESTS =================

    @Test(description = "Verify search repositories returns results", groups = {"api", "functional"})
    public void testSearchRepositoriesReturns200() {
        Assert.assertEquals(
                getApiClient().searchRepositories("language:java").getStatusCode(),
                200
        );
    }

    @Test(description = "Verify search returns items", groups = {"api", "functional"})
    public void testSearchResponseContainsItems() {
        Assert.assertTrue(
                getApiClient()
                        .searchRepositories("language:python")
                        .jsonPath()
                        .getInt("items.size()") > 0
        );
    }

    // ================= ADDITIONAL TESTS =================

    @Test(description = "Verify user followers endpoint", groups = {"api", "functional"})
    public void testUserFollowersEndpoint() {
        Assert.assertEquals(
                getApiClient().getUserFollowers("torvalds").getStatusCode(),
                200
        );
    }

    @Test(description = "Verify response time is reasonable", groups = {"api", "performance"})
    public void testResponseTimeIsReasonable() {
        Assert.assertTrue(
                getApiClient().getUser("torvalds").getTime() < 5000,
                "Response time should be under 5 seconds"
        );
    }
}
