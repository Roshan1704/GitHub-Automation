package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class PullRequestPage {
    private WebDriver driver;
    private WebDriverUtils utils;

    // PR header
    @FindBy(css = "[data-testid='pr-title-heading']")
    private WebElement prTitle;

    @FindBy(css = "[data-testid='pr-number']")
    private WebElement prNumber;

    // PR state
    @FindBy(xpath = "//span[@class='State State--green' or @class='State State--red']")
    private WebElement prState;

    // Commit count
    @FindBy(xpath = "//a[contains(@href, '/commits')]/span")
    private WebElement commitCount;

    // Reviewers
    @FindBy(xpath = "//span[text()='Reviewers']/following-sibling::div//a")
    private List<WebElement> reviewers;

    // Review status
    @FindBy(css = "[data-testid='review-status']")
    private List<WebElement> reviewStatuses;

    // Checks
    @FindBy(css = "[data-testid='checks-summary']")
    private WebElement checksSummary;

    @FindBy(css = "[data-testid='check-item']")
    private List<WebElement> checks;

    // Conversation tabs
    @FindBy(xpath = "//a[contains(@href, '#conversation')]")
    private WebElement conversationTab;

    @FindBy(xpath = "//a[contains(@href, '/commits')]")
    private WebElement commitsTab;

    @FindBy(xpath = "//a[contains(@href, '#files')]")
    private WebElement filesTab;

    // Commit files
    @FindBy(css = "[data-testid='file-tree-item']")
    private List<WebElement> changedFiles;

    // Comments
    @FindBy(css = "[data-testid='issue-comment']")
    private List<WebElement> comments;

    // Merge button
    @FindBy(xpath = "//button[contains(text(), 'Merge')]")
    private WebElement mergeButton;

    @FindBy(xpath = "//button[contains(text(), 'Squash and merge')]")
    private WebElement squashMergeButton;

    @FindBy(xpath = "//button[contains(text(), 'Rebase and merge')]")
    private WebElement rebaseMergeButton;

    // Close PR button
    @FindBy(xpath = "//button[contains(text(), 'Close')]")
    private WebElement closeButton;

    // Mergeable status
    @FindBy(css = "[data-testid='mergeable']")
    private WebElement mergeableStatus;

    // CI status
    @FindBy(xpath = "//a[contains(text(), 'ci/')]")
    private List<WebElement> ciChecks;

    public PullRequestPage(WebDriver driver, WebDriverUtils utils) {
        this.driver = driver;
        this.utils = utils;
        PageFactory.initElements(driver, this);
    }

    public String getPRTitle() {
        return utils.getElementText(prTitle);
    }

    public String getPRNumber() {
        return utils.getElementText(prNumber);
    }

    public String getPRState() {
        return utils.getElementText(prState);
    }

    public boolean isPROpen() {
        return getPRState().toLowerCase().contains("open");
    }

    public boolean isPRMerged() {
        return getPRState().toLowerCase().contains("merged");
    }

    public boolean isPRClosed() {
        return getPRState().toLowerCase().contains("closed");
    }

    public int getCommitCount() {
        String count = utils.getElementText(commitCount);
        return Integer.parseInt(count.replaceAll("[^0-9]", ""));
    }

    public int getReviewersCount() {
        return reviewers.size();
    }

    public List<String> getReviewerNames() {
        return reviewers.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getReviewStatusCount() {
        return reviewStatuses.size();
    }

    public boolean hasApprovedReviews() {
        return reviewStatuses.stream()
                .anyMatch(status -> status.getText().toLowerCase().contains("approved"));
    }

    public boolean hasRequestedChanges() {
        return reviewStatuses.stream()
                .anyMatch(status -> status.getText().toLowerCase().contains("requested changes"));
    }

    public boolean hasCommentedReviews() {
        return reviewStatuses.stream()
                .anyMatch(status -> status.getText().toLowerCase().contains("commented"));
    }

    public boolean isChecksSummaryDisplayed() {
        return utils.isElementDisplayed(checksSummary);
    }

    public int getChecksCount() {
        return checks.size();
    }

    public List<String> getCheckNames() {
        return checks.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean allChecksPassed() {
        return checks.stream()
                .allMatch(check -> check.getText().toLowerCase().contains("passed"));
    }

    public void clickConversationTab() {
        utils.click(conversationTab);
    }

    public void clickCommitsTab() {
        utils.click(commitsTab);
    }

    public void clickFilesTab() {
        utils.click(filesTab);
    }

    public int getChangedFilesCount() {
        return changedFiles.size();
    }

    public List<String> getChangedFileNames() {
        return changedFiles.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getCommentsCount() {
        return comments.size();
    }

    public void mergePR() {
        utils.click(mergeButton);
        utils.waitForPageLoad(3);
    }

    public void squashMergePR() {
        utils.click(squashMergeButton);
        utils.waitForPageLoad(3);
    }

    public void rebaseMergePR() {
        utils.click(rebaseMergeButton);
        utils.waitForPageLoad(3);
    }

    public void closePR() {
        utils.click(closeButton);
        utils.waitForPageLoad(3);
    }

    public boolean isMergeable() {
        return utils.isElementDisplayed(mergeableStatus) &&
               utils.getElementText(mergeableStatus).toLowerCase().contains("mergeable");
    }

    public int getCIChecksCount() {
        return ciChecks.size();
    }

    public void navigateToPullRequest(String owner, String repo, String prNumber) {
        driver.get("https://github.com/" + owner + "/" + repo + "/pull/" + prNumber);
        utils.waitForPageLoad(5);
    }

    // Legacy method name for backwards compatibility
    public void navigateToPR(String owner, String repo, String prNumber) {
        navigateToPullRequest(owner, repo, prNumber);
    }

    public boolean isPRPageLoaded() {
        return utils.isElementDisplayed(prTitle) &&
               utils.isElementDisplayed(prState);
    }

    public String getPRURL() {
        return driver.getCurrentUrl();
    }
}
