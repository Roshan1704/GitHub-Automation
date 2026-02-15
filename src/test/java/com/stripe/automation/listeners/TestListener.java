package com.stripe.automation.listeners;

import com.stripe.automation.drivers.WebDriverFactory;
import com.stripe.automation.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = WebDriverFactory.getDriver();
        try {
            var path = ScreenshotUtils.capture(driver, result.getName());
            Allure.addAttachment("Failure-Screenshot", Files.newInputStream(path));
        } catch (IOException ignored) {
        }
    }
}
