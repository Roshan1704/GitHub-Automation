package com.github.qa.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * GitHub API Client for testing REST API endpoints
 * Uses publicly available GitHub API endpoints
 */
public class GitHubAPIClient {
    private static final Logger logger = LoggerFactory.getLogger(GitHubAPIClient.class);
    private static final String BASE_URL = "https://api.github.com";
    private static final int TIMEOUT_MILLISECONDS = 10000;  // 10 seconds in milliseconds

    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Configure timeout for all requests
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", TIMEOUT_MILLISECONDS)
                        .setParam("http.socket.timeout", TIMEOUT_MILLISECONDS));
    }

    /**
     * Search repositories by keyword
     */
    @Step("Search repositories: {query}")
    public Response searchRepositories(String query) {
        logger.info("Searching repositories for: {}", query);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .queryParam("q", query)
                .queryParam("per_page", "30")
                .get("/search/repositories");
        
        logger.info("Search API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get repository details
     */
    @Step("Get repository details: {owner}/{repo}")
    public Response getRepository(String owner, String repo) {
        logger.info("Fetching repository: {}/{}", owner, repo);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .get("/repos/{owner}/{repo}", owner, repo);
        
        logger.info("Get repository API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get repository issues
     */
    @Step("Get repository issues: {owner}/{repo}")
    public Response getRepositoryIssues(String owner, String repo) {
        logger.info("Fetching issues for: {}/{}", owner, repo);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .queryParam("state", "open")
                .queryParam("per_page", "30")
                .get("/repos/{owner}/{repo}/issues", owner, repo);
        
        logger.info("Issues API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get user details
     */
    @Step("Get user details: {username}")
    public Response getUser(String username) {
        logger.info("Fetching user: {}", username);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .get("/users/{username}", username);
        
        logger.info("User API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get trending repositories (using search API)
     */
    @Step("Get trending repositories")
    public Response getTrendingRepositories() {
        logger.info("Fetching trending repositories");
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .queryParam("q", "stars:>50000")
                .queryParam("sort", "stars")
                .queryParam("order", "desc")
                .queryParam("per_page", "30")
                .get("/search/repositories");
        
        logger.info("Trending repositories API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Health check on GitHub API
     */
    @Step("Check GitHub API health")
    public Response healthCheck() {
        logger.info("Performing health check");
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .get("/");
        
        logger.info("Health check status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Rate limit check
     */
    @Step("Check API rate limits")
    public Response getRateLimits() {
        logger.info("Checking rate limits");
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .get("/rate_limit");
        
        logger.info("Rate limit check status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get user followers
     */
    @Step("Get user followers: {username}")
    public Response getUserFollowers(String username) {
        logger.info("Fetching followers for: {}", username);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .queryParam("per_page", "30")
                .get("/users/{username}/followers", username);
        
        logger.info("User followers API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get user repositories
     */
    @Step("Get user repositories: {username}")
    public Response getUserRepositories(String username) {
        logger.info("Fetching repositories for: {}", username);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .queryParam("per_page", "30")
                .queryParam("sort", "updated")
                .get("/users/{username}/repos", username);
        
        logger.info("User repositories API status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Get users that a user is following
     */
    @Step("Get users following: {username}")
    public Response getUserFollowing(String username) {
        logger.info("Fetching following for: {}", username);
        Response response = given()
                .accept("application/vnd.github.v3+json")
                .queryParam("per_page", "30")
                .get("/users/{username}/following", username);
        
        logger.info("User following API status: {}", response.getStatusCode());
        return response;
    }
}
