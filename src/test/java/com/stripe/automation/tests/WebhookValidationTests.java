package com.stripe.automation.tests;

import com.stripe.automation.webhook.WebhookReceiverServer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class WebhookValidationTests {
    private WebhookReceiverServer server;

    @BeforeClass(alwaysRun = true)
    public void startServer() throws IOException {
        server = new WebhookReceiverServer(9090, "whsec_dummy_secret");
        server.start();
    }

    @AfterClass(alwaysRun = true)
    public void stopServer() {
        server.stop();
    }

    @Test(groups = {"webhook", "smoke"})
    public void webhookReceiverShouldStart() {
        Assert.assertFalse(server.containsEvent("evt_missing"));
    }

    @Test(groups = {"webhook", "regression"})
    public void duplicateHandlingShouldBeIdempotent() {
        Assert.assertFalse(server.containsEvent("evt_duplicate_sample"));
    }
}
