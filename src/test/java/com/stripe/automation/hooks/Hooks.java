package com.stripe.automation.hooks;

import com.stripe.automation.config.ConfigManager;
import com.stripe.automation.drivers.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before("@ui")
    public void setupUi() {
        WebDriverFactory.getDriver().manage().window().maximize();
        WebDriverFactory.getDriver().get(ConfigManager.get("stripe.dashboard.url"));
    }

    @After("@ui")
    public void tearDownUi() {
        WebDriverFactory.quitDriver();
    }
}
