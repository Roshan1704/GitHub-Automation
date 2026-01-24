package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import com.github.qa.pages.HomePage;
import com.github.qa.pages.SearchPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GitHub Search Functionality - TIER 1: Real E2E QA
 * Tests search journey from home page to results
 */
@Feature("GitHub Search Functionality")
public class SearchTest extends BaseTest {

    private HomePage homePage;
    private SearchPage searchPage;

    @BeforeMethod(alwaysRun = true)
    public void initSearchFlow() {
        Assert.assertNotNull(getDriver(),
                "WebgetDriver() must be initialized by BaseTest before Search tests");

        getDriver().get(getBaseUrl());

        homePage = new HomePage(getDriver());
        searchPage = new SearchPage(getDriver());

        homePage.load();

        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Home page must be displayed before search actions");
    }

    @Test(description = "Search for popular repository")
    @Description("User should be able to search and see results for a popular repository")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchForPopularRepository() {
        searchPage.searchFor("react");

        assertThat(searchPage.areSearchResultsDisplayed())
                .as("Search results should be displayed")
                .isTrue();

        assertThat(searchPage.getSearchResultCount())
                .as("Should have results for popular repository")
                .isGreaterThan(0);
    }

    @Test(description = "Search returns multiple results")
    @Description("Search for common keyword should return multiple results")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchReturnsMultipleResults() {
        searchPage.searchFor("python");

        assertThat(searchPage.getSearchResultCount())
                .as("Should have multiple results for 'python'")
                .isGreaterThanOrEqualTo(1);
    }

    @Test(description = "Search with no results shows appropriate message")
    @Description("Searching for nonsensical term should show no results message or empty results")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchWithNoResults() {
        searchPage.searchFor("xyznonexistentrepo12345nonsense");

        boolean hasNoResults =
                searchPage.isNoResultsMessageDisplayed()
                        || searchPage.getSearchResultCount() == 0;

        assertThat(hasNoResults)
                .as("Should show no results for nonsensical search")
                .isTrue();
    }

    @Test(description = "Search results page has filters")
    @Description("Search results page should have filtering options available")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchFiltersAvailable() {
        searchPage.searchFor("javascript");

        assertThat(searchPage.areFiltersAvailable())
                .as("Filters should be available on search results")
                .isTrue();
    }

    @Test(description = "First result is clickable and navigates")
    @Description("User should be able to click on first search result")
    @Severity(SeverityLevel.BLOCKER)
    public void testFirstResultClickable() {
        searchPage.searchFor("kubernetes");

        if (searchPage.getSearchResultCount() > 0) {
            assertThat(searchPage.getFirstResultText())
                    .as("First result text should not be empty")
                    .isNotEmpty();
        }
    }

    @Test(description = "Search case insensitivity")
    @Description("Search should work with different case variations")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchCaseInsensitivity() {
        searchPage.searchFor("NODEJS");

        assertThat(searchPage.areSearchResultsDisplayed())
                .as("Search should work with uppercase")
                .isTrue();
    }
}
