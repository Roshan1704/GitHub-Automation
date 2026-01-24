package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class NotificationsPage {
    private WebDriver driver;
    private WebDriverUtils utils;

    // Notification list
    @FindBy(css = "[data-testid='notification-item']")
    private List<WebElement> notifications;

    @FindBy(xpath = "//div[contains(@class, 'notification')]")
    private List<WebElement> notificationRows;

    // Filters
    @FindBy(xpath = "//button[contains(text(), 'Participating')]")
    private WebElement participatingFilter;

    @FindBy(xpath = "//button[contains(text(), 'All')]")
    private WebElement allFilter;

    // Sort options
    @FindBy(xpath = "//button[text()='Newest' or text()='Oldest']")
    private WebElement sortButton;

    // Mark as read
    @FindBy(xpath = "//button[contains(@aria-label, 'Mark')]")
    private WebElement markAsReadButton;

    @FindBy(xpath = "//button[contains(text(), 'Mark all as read')]")
    private WebElement markAllAsReadButton;

    // Inbox status
    @FindBy(xpath = "//span[contains(text(), 'notification')]")
    private WebElement notificationCount;

    // Notification type icons
    @FindBy(css = "[data-testid='notification-icon']")
    private List<WebElement> notificationIcons;

    // Empty state
    @FindBy(xpath = "//div[contains(text(), 'Great, youre all caught up')]")
    private WebElement emptyStateMessage;

    public NotificationsPage(WebDriver driver, WebDriverUtils utils) {
        this.driver = driver;
        this.utils = utils;
        PageFactory.initElements(driver, this);
    }

    public int getNotificationsCount() {
        return notifications.size();
    }

    public List<String> getNotificationTitles() {
        return notifications.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean areNotificationsDisplayed() {
        return notifications.size() > 0;
    }

    public void clickParticipatingFilter() {
        utils.click(participatingFilter);
        utils.waitForPageLoad(3);
    }

    public void clickAllFilter() {
        utils.click(allFilter);
        utils.waitForPageLoad(3);
    }

    public boolean isParticipatingFilterActive() {
        return participatingFilter.getAttribute("class").contains("selected");
    }

    public boolean isAllFilterActive() {
        return allFilter.getAttribute("class").contains("selected");
    }

    public void clickSortButton() {
        utils.click(sortButton);
    }

    public void markNotificationAsRead(int index) {
        if (index < notifications.size()) {
            List<WebElement> readButtons = driver.findElements(
                By.xpath("//button[contains(@aria-label, 'Mark')]")
            );
            if (index < readButtons.size()) {
                utils.click(readButtons.get(index));
            }
        }
    }

    public void markAllAsRead() {
        utils.click(markAllAsReadButton);
        utils.waitForPageLoad(3);
    }

    public String getNotificationCount() {
        return utils.getElementText(notificationCount);
    }

    public int getUnreadNotificationsCount() {
        String count = getNotificationCount();
        return Integer.parseInt(count.replaceAll("[^0-9]", ""));
    }

    public int getNotificationIconsCount() {
        return notificationIcons.size();
    }

    public boolean isEmptyStateDisplayed() {
        return utils.isElementDisplayed(emptyStateMessage);
    }

    public String getEmptyStateMessage() {
        return utils.getElementText(emptyStateMessage);
    }

    public void navigateToNotifications() {
        driver.get("https://github.com/notifications");
        utils.waitForPageLoad(5);
    }

    public boolean isNotificationsPageLoaded() {
        return driver.getCurrentUrl().contains("notifications");
    }

    public void clickFirstNotification() {
        if (!notifications.isEmpty()) {
            utils.click(notifications.get(0));
            utils.waitForPageLoad(3);
        }
    }

    public void clickNotificationByIndex(int index) {
        if (index < notifications.size()) {
            utils.click(notifications.get(index));
            utils.waitForPageLoad(3);
        }
    }
}
