package com.github.qa.pages;

import com.github.qa.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class IssuePage {
    private WebDriver driver;
    private WebDriverUtils utils;

    // Issue header
    @FindBy(css = "[data-testid='issue.views.issue-base.title']")
    private WebElement issueTitle;

    @FindBy(css = "[data-testid='issue-number-and-references']")
    private WebElement issueNumber;

    // Issue state
    @FindBy(css = "[data-testid='issue.views.issue-base.status']")
    private WebElement issueState;

    @FindBy(xpath = "//span[contains(@class, 'State')]")
    private WebElement statusBadge;

    // Issue details
    @FindBy(css = "[data-testid='issue-detail-panel']")
    private WebElement detailsPanel;

    @FindBy(xpath = "//span[text()='Labels']/following-sibling::div//a")
    private List<WebElement> labels;

    @FindBy(xpath = "//span[text()='Assignees']/following-sibling::div//a")
    private List<WebElement> assignees;

    @FindBy(xpath = "//span[text()='Milestone']/following-sibling::div")
    private WebElement milestone;

    // Comments section
    @FindBy(css = "[data-testid='issue-comment']")
    private List<WebElement> comments;

    @FindBy(css = "#discussion_bucket .TimelineItem")
    private List<WebElement> discussionItems;

    // Comment input
    @FindBy(css = "[data-testid='comment-create-form']")
    private WebElement commentForm;

    @FindBy(css = "[aria-label='Comment body']")
    private WebElement commentTextarea;

    // Buttons
    @FindBy(xpath = "//button[contains(text(), 'Comment')]")
    private WebElement commentButton;

    @FindBy(xpath = "//button[contains(text(), 'Close')]")
    private WebElement closeButton;

    @FindBy(xpath = "//button[contains(text(), 'Reopen')]")
    private WebElement reopenButton;

    // Links section
    @FindBy(xpath = "//span[text()='Linked pull requests']/../..//a")
    private List<WebElement> linkedPullRequests;

    // Related issues
    @FindBy(xpath = "//button[contains(text(), 'Show')]//following::div//a")
    private List<WebElement> relatedIssues;

    public IssuePage(WebDriver driver, WebDriverUtils utils) {
        this.driver = driver;
        this.utils = utils;
        PageFactory.initElements(driver, this);
    }

    public String getIssueTitle() {
        return utils.getElementText(issueTitle);
    }

    public String getIssueNumber() {
        return utils.getElementText(issueNumber);
    }

    public String getIssueState() {
        return utils.getElementText(statusBadge);
    }

    public boolean isIssueOpen() {
        String state = getIssueState();
        return state != null && state.toLowerCase().contains("open");
    }

    public boolean isIssueClosed() {
        String state = getIssueState();
        return state != null && state.toLowerCase().contains("closed");
    }

    public int getLabelsCount() {
        return labels.size();
    }

    public List<String> getLabelNames() {
        return labels.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getAssigneesCount() {
        return assignees.size();
    }

    public List<String> getAssigneeNames() {
        return assignees.stream()
                .map(WebElement::getText)
                .toList();
    }

    public String getMilestone() {
        return utils.getElementText(milestone);
    }

    public boolean isMilestoneSet() {
        return utils.isElementDisplayed(milestone) && !getMilestone().isEmpty();
    }

    public int getCommentsCount() {
        return comments.size();
    }

    public int getDiscussionItemsCount() {
        return discussionItems.size();
    }

    public List<String> getCommentTexts() {
        return comments.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isCommentFormDisplayed() {
        return utils.isElementDisplayed(commentForm);
    }

    public void typeComment(String text) {
        utils.click(commentTextarea);
        utils.sendKeys(commentTextarea, text);
    }

    public void submitComment() {
        utils.click(commentButton);
        utils.waitForPageLoad(3);
    }

    public void addComment(String text) {
        typeComment(text);
        submitComment();
    }

    public void closeIssue() {
        utils.click(closeButton);
        utils.waitForPageLoad(3);
    }

    public void reopenIssue() {
        utils.click(reopenButton);
        utils.waitForPageLoad(3);
    }

    public int getLinkedPullRequestsCount() {
        return linkedPullRequests.size();
    }

    public List<String> getLinkedPullRequestTitles() {
        return linkedPullRequests.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getRelatedIssuesCount() {
        return relatedIssues.size();
    }

    public void navigateToIssue(String owner, String repo, String issueNumber) {
        driver.get("https://github.com/" + owner + "/" + repo + "/issues/" + issueNumber);
        utils.waitForPageLoad(5);
    }

    public boolean isIssuePageLoaded() {
        return utils.isElementDisplayed(issueTitle) &&
               utils.isElementDisplayed(statusBadge);
    }

    public String getIssueURL() {
        return driver.getCurrentUrl();
    }
}
