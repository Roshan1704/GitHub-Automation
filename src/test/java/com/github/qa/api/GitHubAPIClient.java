package com.github.qa.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GitHub API Client for testing public REST API endpoints.
 */
public class GitHubAPIClient {
    private static final Logger logger = LoggerFactory.getLogger(GitHubAPIClient.class);

    private static final String DEFAULT_BASE_URL = "https://api.github.com";
    private static final String GITHUB_ACCEPT_HEADER = "application/vnd.github+json";
    private static final int DEFAULT_TIMEOUT_MILLISECONDS = 10_000;

    private final RequestSpecification baseRequestSpec;

    public GitHubAPIClient() {
        String baseUrl = System.getProperty("github.api.baseUrl", DEFAULT_BASE_URL);
        int timeoutMs = parseIntProperty("github.api.timeoutMs", DEFAULT_TIMEOUT_MILLISECONDS);
        String token = System.getProperty("github.api.token", "");

        RequestSpecification spec = RestAssured.given()
                .baseUri(baseUrl)
                .accept(GITHUB_ACCEPT_HEADER)
                .config(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", timeoutMs)
                                .setParam("http.socket.timeout", timeoutMs)));

        if (!token.isBlank()) {
            spec.header("Authorization", "Bearer " + token);
        }

        this.baseRequestSpec = spec;
        logger.info("GitHubAPIClient initialized. baseUrl={}, timeoutMs={}, tokenConfigured={}", baseUrl, timeoutMs, !token.isBlank());
    }

    @Step("Search repositories: {query}")
    public Response searchRepositories(String query) {
        validateRequired("query", query);
        logger.info("Searching repositories for: {}", query);
        return executeGet("/search/repositories", newRequest().queryParam("q", query).queryParam("per_page", 30));
    }

    @Step("Get repository details: {owner}/{repo}")
    public Response getRepository(String owner, String repo) {
        validateRequired("owner", owner);
        validateRequired("repo", repo);
        logger.info("Fetching repository: {}/{}", owner, repo);
        return executeGet("/repos/{owner}/{repo}", newRequest(), owner, repo);
    }

    @Step("Get repository issues: {owner}/{repo}")
    public Response getRepositoryIssues(String owner, String repo) {
        validateRequired("owner", owner);
        validateRequired("repo", repo);
        logger.info("Fetching issues for: {}/{}", owner, repo);
        return executeGet(
                "/repos/{owner}/{repo}/issues",
                newRequest().queryParam("state", "open").queryParam("per_page", 30),
                owner,
                repo
        );
    }

    @Step("Get user details: {username}")
    public Response getUser(String username) {
        validateRequired("username", username);
        logger.info("Fetching user: {}", username);
        return executeGet("/users/{username}", newRequest(), username);
    }

    @Step("Get trending repositories")
    public Response getTrendingRepositories() {
        logger.info("Fetching trending repositories");
        return executeGet(
                "/search/repositories",
                newRequest().queryParam("q", "stars:>50000")
                        .queryParam("sort", "stars")
                        .queryParam("order", "desc")
                        .queryParam("per_page", 30)
        );
    }

    @Step("Check GitHub API health")
    public Response healthCheck() {
        logger.info("Performing health check");
        return executeGet("/", newRequest());
    }

    @Step("Check API rate limits")
    public Response getRateLimits() {
        logger.info("Checking rate limits");
        return executeGet("/rate_limit", newRequest());
    }

    @Step("Get user followers: {username}")
    public Response getUserFollowers(String username) {
        validateRequired("username", username);
        logger.info("Fetching followers for: {}", username);
        return executeGet(
                "/users/{username}/followers",
                newRequest().queryParam("per_page", 30),
                username
        );
    }

    @Step("Get user repositories: {username}")
    public Response getUserRepositories(String username) {
        validateRequired("username", username);
        logger.info("Fetching repositories for: {}", username);
        return executeGet(
                "/users/{username}/repos",
                newRequest().queryParam("per_page", 30).queryParam("sort", "updated"),
                username
        );
    }

    @Step("Get users following: {username}")
    public Response getUserFollowing(String username) {
        validateRequired("username", username);
        logger.info("Fetching following for: {}", username);
        return executeGet(
                "/users/{username}/following",
                newRequest().queryParam("per_page", 30),
                username
        );
    }


    private RequestSpecification newRequest() {
        return RestAssured.given().spec(baseRequestSpec);
    }

    private int parseIntProperty(String propertyName, int defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            logger.warn("Invalid integer for property {}: {}. Falling back to {}", propertyName, value, defaultValue);
            return defaultValue;
        }
    }

    private Response executeGet(String path, RequestSpecification specification, Object... pathParams) {
        Response response = specification.get(path, pathParams);
        logger.info("GET {} -> status {}", path, response.getStatusCode());
        return response;
    }

    private void validateRequired(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null/blank");
        }
    }
}
