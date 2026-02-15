package com.stripe.automation.base;

import com.stripe.automation.api.StripeApiClient;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest {
    protected StripeApiClient stripeApiClient;

    @BeforeMethod(alwaysRun = true)
    public void initClient() {
        stripeApiClient = new StripeApiClient();
    }
}
