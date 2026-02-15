package com.stripe.automation.ui.pages;

import com.stripe.automation.base.BaseUiPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PaymentsPage extends BaseUiPage {
    private final By pageHeader = By.xpath("//h1[contains(.,'Payments')]");
    private final By searchInput = By.cssSelector("input[placeholder*='Search']");
    private final By refundStatusBadge = By.cssSelector("[data-testid='refund-status']");
    private final By filterButton = By.cssSelector("button[data-testid='filter-button']");
    private final By nextPageButton = By.cssSelector("button[aria-label='Next page']");

    public PaymentsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader)).isDisplayed();
    }

    public void searchPayment(String paymentId) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)).sendKeys(paymentId);
    }

    public String refundStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(refundStatusBadge)).getText();
    }

    public void applyFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(filterButton)).click();
    }

    public void goNextPage() {
        wait.until(ExpectedConditions.elementToBeClickable(nextPageButton)).click();
    }
}
