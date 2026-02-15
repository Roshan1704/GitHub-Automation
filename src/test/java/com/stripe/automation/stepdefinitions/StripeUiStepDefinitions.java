package com.stripe.automation.stepdefinitions;

import com.stripe.automation.config.ConfigManager;
import com.stripe.automation.drivers.WebDriverFactory;
import com.stripe.automation.ui.pages.LoginPage;
import com.stripe.automation.ui.pages.PaymentsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;

public class StripeUiStepDefinitions {
    private final LoginPage loginPage = new LoginPage(WebDriverFactory.getDriver());
    private final PaymentsPage paymentsPage = new PaymentsPage(WebDriverFactory.getDriver());
    private final SoftAssertions softAssertions = new SoftAssertions();

    @Given("the user logs into Stripe dashboard in test mode")
    public void loginToDashboard() {
        loginPage.open(ConfigManager.get("stripe.dashboard.url"));
        loginPage.login(ConfigManager.get("stripe.ui.email"), ConfigManager.get("stripe.ui.password"));
    }

    @When("the user opens the payments page")
    public void openPayments() {
        WebDriverFactory.getDriver().get(ConfigManager.get("stripe.dashboard.url") + "/payments");
    }

    @Then("the payments page should be displayed")
    public void validatePaymentsPage() {
        softAssertions.assertThat(paymentsPage.isPageDisplayed()).isTrue();
        softAssertions.assertAll();
    }

    @And("the user searches for payment {string}")
    public void searchPayment(String paymentId) {
        paymentsPage.searchPayment(paymentId);
    }

    @Then("refund status should be {string}")
    public void validateRefundStatus(String status) {
        softAssertions.assertThat(paymentsPage.refundStatus()).containsIgnoringCase(status);
        softAssertions.assertAll();
    }

    @And("user applies payment filters and validates pagination")
    public void validateFilterAndPagination() {
        paymentsPage.applyFilter();
        paymentsPage.goNextPage();
    }
}
