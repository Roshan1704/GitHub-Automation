package com.github.qa.pages;


import com.github.qa.utils.WebDriverUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginPage - Page Object for GitHub Login functionality
 * Handles authentication and login-related operations
 */
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverUtils utils;
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Locators
    private static final By LOGIN_BUTTON = By.linkText("Sign in");
    private static final By EMAIL_INPUT = By.id("login_field");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By SUBMIT_BUTTON = By.name("commit");
    private static final By ERROR_MESSAGE = By.cssSelector("[role='alert']");
    private static final By TWO_FACTOR_INPUT = By.id("otp");
    private static final By REMEMBER_ME_CHECKBOX = By.id("remember_me");
    private static final By FORGOT_PASSWORD_LINK = By.linkText("Forgot password?");
    private static final By CREATE_ACCOUNT_LINK = By.linkText("Create an account");
    private static final By LOGGED_IN_USER_AVATAR = By.cssSelector("[data-testid='profile-menu-button']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver);
        logger.info("LoginPage initialized");
    }

    @Step("Navigate to GitHub login page")
    public void navigateToLogin() {
        driver.get("https://github.com/login");
        utils.waitForPageLoad(driver, 10);
        logger.info("Navigated to GitHub login page");
    }

    @Step("Click Sign In button")
    public void clickSignInButton() {
        try {
            utils.click(driver.findElement(LOGIN_BUTTON));
            logger.info("Sign in button clicked");
        } catch (Exception e) {
            logger.warn("Sign in button not found on home page, already on login page");
        }
    }

    @Step("Enter email: {email}")
    public void enterEmail(String email) {
        utils.sendKeys(driver.findElement(EMAIL_INPUT), email);
        logger.info("Email entered: {}", email);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        utils.sendKeys(driver.findElement(PASSWORD_INPUT), password);
        logger.info("Password entered (masked for security)");
    }

    @Step("Click Submit button")
    public void clickSubmitButton() {
        utils.click(driver.findElement(SUBMIT_BUTTON));
        logger.info("Submit button clicked");
    }

    @Step("Perform login with email and password")
    public void login(String email, String password) {
        navigateToLogin();
        enterEmail(email);
        enterPassword(password);
        clickSubmitButton();
        logger.info("Login attempt completed");
    }

    @Step("Check if login failed with error message")
    public boolean isLoginErrorDisplayed() {
        try {
            return utils.isElementDisplayed(driver, ERROR_MESSAGE);
        } catch (Exception e) {
            logger.warn("No error message displayed");
            return false;
        }
    }

    @Step("Get login error message")
    public String getLoginErrorMessage() {
        try {
            return driver.findElement(ERROR_MESSAGE).getText();
        } catch (Exception e) {
            logger.error("Failed to get error message", e);
            return "";
        }
    }

    @Step("Check if 2FA prompt is displayed")
    public boolean isTwoFactorPromptDisplayed() {
        try {
            return utils.isElementDisplayed(driver, TWO_FACTOR_INPUT);
        } catch (Exception e) {
            logger.warn("Two factor prompt not displayed");
            return false;
        }
    }

    @Step("Enter 2FA code: {code}")
    public void enterTwoFactorCode(String code) {
        utils.sendKeys(driver.findElement(TWO_FACTOR_INPUT), code);
        logger.info("2FA code entered");
    }

    @Step("Verify user is logged in by checking avatar")
    public boolean isUserLoggedIn() {
        try {
            // Small wait to ensure page loads completely
            Thread.sleep(2000);
            return utils.isElementDisplayed(driver, LOGGED_IN_USER_AVATAR);
        } catch (InterruptedException e) {
            logger.error("Interrupted while checking login status", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Step("Click Remember Me checkbox")
    public void clickRememberMe() {
        try {
            utils.click(driver.findElement(REMEMBER_ME_CHECKBOX));
            logger.info("Remember Me checkbox clicked");
        } catch (Exception e) {
            logger.warn("Remember Me checkbox not found or disabled");
        }
    }

    @Step("Click Forgot Password link")
    public void clickForgotPasswordLink() {
        utils.click(driver.findElement(FORGOT_PASSWORD_LINK));
        logger.info("Forgot Password link clicked");
    }

    @Step("Click Create Account link")
    public void clickCreateAccountLink() {
        utils.click(driver.findElement(CREATE_ACCOUNT_LINK));
        logger.info("Create Account link clicked");
    }

    @Step("Check if on login page")
    public boolean isOnLoginPage() {
        try {
            return driver.getCurrentUrl().contains("/login") && 
                   utils.isElementDisplayed(driver, EMAIL_INPUT);
        } catch (Exception e) {
            logger.error("Error checking if on login page", e);
            return false;
        }
    }

    @Step("Get current URL")
    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    @Step("Logout by navigating to logout endpoint")
    public void logout() {
        try {
            driver.get("https://github.com/logout");
            logger.info("Logout action completed");
        } catch (Exception e) {
            logger.error("Error during logout", e);
        }
    }
}
