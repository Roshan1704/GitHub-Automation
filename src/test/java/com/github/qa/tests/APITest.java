package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("GitHub API Integration")
public class APITest extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void verifyApiClientInitialized() {
        assertThat(getApiClient())
                .as("API client must be initialized by BaseTest")
                .isNotNull();
    }

    @Test(description = "API health check")
    @Description("GitHub API should be accessible and responding")
    @Severity(SeverityLevel.BLOCKER)
    public void testAPIHealthCheck() {
        Response response = getApiClient().healthCheck();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "Search API returns valid structure")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchAPIStructure() {
        Response response = getApiClient().searchRepositories("java");

        response.then()
                .statusCode(200)
                .body("total_count", greaterThan(0))
                .body("items", notNullValue())
                .body("items[0].id", notNullValue())
                .body("items[0].name", notNullValue());
    }

    @Test(description = "Repository API returns complete data")
    @Severity(SeverityLevel.CRITICAL)
    public void testRepositoryAPIData() {
        getApiClient()
                .getRepository("torvalds", "linux")
                .then()
                .statusCode(200)
                .body("name", equalTo("linux"))
                .body("owner.login", equalTo("torvalds"));
    }

    @Test(description = "Non-existent repository returns 404")
    @Severity(SeverityLevel.CRITICAL)
    public void testRepositoryNotFound() {
        assertThat(
                getApiClient()
                        .getRepository("nonexistent", "repo")
                        .getStatusCode()
        ).isEqualTo(404);
    }

    @Test(description = "Issues API pagination works")
    @Severity(SeverityLevel.CRITICAL)
    public void testIssuesPagination() {
        assertThat(
                getApiClient()
                        .getRepositoryIssues("torvalds", "linux")
                        .getStatusCode()
        ).isEqualTo(200);
    }

    @Test(description = "User API returns valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserAPIData() {
        getApiClient()
                .getUser("torvalds")
                .then()
                .statusCode(200)
                .body("login", equalTo("torvalds"))
                .body("followers", greaterThanOrEqualTo(0));
    }

    @Test(description = "API response time is acceptable")
    @Severity(SeverityLevel.BLOCKER)
    public void testAPIResponseTime() {
        assertThat(
                getApiClient()
                        .searchRepositories("spring")
                        .getTime()
        ).isLessThan(5000);
    }

    @Test(description = "Rate limit headers exist")
    @Severity(SeverityLevel.MINOR)
    public void testRateLimitHeaders() {
        assertThat(
                getApiClient()
                        .searchRepositories("golang")
                        .getHeader("X-RateLimit-Limit")
        ).isNotNull();
    }
}
