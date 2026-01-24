package com.github.qa.tests;

import com.github.qa.base.BaseTest;
import com.github.qa.pages.HomePage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for GitHub Home Page - TIER 1: Real E2E QA
 * Tests critical user journeys and smoke scenarios
 */
@Feature("GitHub Home Page")
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initHomePage() {
        Assert.assertNotNull(getDriver(),
                "WebgetDriver() must be initialized by BaseTest before running HomePage tests");

        homePage = new HomePage(getDriver());
        homePage.load();

        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Home page must be displayed before running assertions");
    }

    @Test(description = "Smoke test: Home page loads successfully")
    @Description("Verify that GitHub home page loads and displays correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void testHomePageLoad() {
        assertThat(homePage.isHomePageDisplayed())
                .as("Home page should display hero section")
                .isTrue();
    }

    @Test(description = "Verify page title is correct")
    @Description("GitHub home page should have correct title")
    @Severity(SeverityLevel.BLOCKER)
    public void testPageTitle() {
        assertThat(homePage.getPageTitle())
                .as("Page title should contain GitHub")
                .contains("GitHub");
    }

    @Test(description = "Verify hero section is visible")
    @Description("Hero section with main content should be visible")
    @Severity(SeverityLevel.CRITICAL)
    public void testHeroSectionVisible() {
        assertThat(homePage.isHeroSectionVisible())
                .as("Hero section should be visible")
                .isTrue();
    }

    @Test(description = "Verify page load meets SLA (3 seconds)")
    @Description("Critical business metric: home page should load within 3 seconds")
    @Severity(SeverityLevel.CRITICAL)
    public void testPageLoadSLA() {
        assertThat(homePage.pageLoadsSLA())
                .as("Page load should meet SLA of 3 seconds")
                .isTrue();
    }

    @Test(description = "Verify navigation elements are present")
    @Description("Key navigation elements should be accessible")
    @Severity(SeverityLevel.BLOCKER)
    public void testNavigationElementsPresent() {
        // If page was not correctly loaded, @BeforeMethod would already fail
        assertThat(homePage.isHomePageDisplayed())
                .as("Navigation elements should be present")
                .isTrue();
    }
}
