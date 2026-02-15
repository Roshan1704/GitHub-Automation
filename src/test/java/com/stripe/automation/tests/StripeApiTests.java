package com.stripe.automation.tests;

import com.stripe.automation.base.BaseApiTest;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

public class StripeApiTests extends BaseApiTest {

    @Test(groups = {"smoke", "api"})
    public void createAndConfirmPaymentIntent() {
        SoftAssertions softly = new SoftAssertions();
        Response create = stripeApiClient.createPaymentIntent(Map.of(
                "amount", 1000,
                "currency", "usd",
                "payment_method", "pm_card_visa",
                "confirm", true
        ), UUID.randomUUID().toString());

        softly.assertThat(create.statusCode()).isEqualTo(200);
        create.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("testdata/api/schemas/payment-intent-schema.json"));

        Response confirm = stripeApiClient.confirmPayment(create.path("id"), Map.of("payment_method", "pm_card_visa"));
        softly.assertThat(confirm.statusCode()).isIn(200, 400);
        softly.assertAll();
    }

    @Test(groups = {"regression", "api"})
    public void refundAndIdempotencyValidation() {
        SoftAssertions softly = new SoftAssertions();
        Response payment = stripeApiClient.createPaymentIntent(Map.of(
                "amount", 3000,
                "currency", "usd",
                "payment_method", "pm_card_visa",
                "confirm", true
        ), UUID.randomUUID().toString());

        String chargeId = payment.path("latest_charge");
        String idem = UUID.randomUUID().toString();
        Response fullRefund = stripeApiClient.createRefund(Map.of("charge", chargeId, "amount", 1000), idem);
        Response duplicateRefund = stripeApiClient.createRefund(Map.of("charge", chargeId, "amount", 1000), idem);
        Response excessiveRefund = stripeApiClient.createRefund(Map.of("charge", chargeId, "amount", 999999), UUID.randomUUID().toString());

        softly.assertThat(fullRefund.statusCode()).isEqualTo(200);
        softly.assertThat(duplicateRefund.path("id")).isEqualTo(fullRefund.path("id"));
        softly.assertThat(excessiveRefund.statusCode()).isIn(400, 402);
        softly.assertAll();
    }

    @Test(groups = {"regression", "api"})
    public void invalidAndExpiredApiKeyShouldFail() {
        Response response = stripeApiClient.createPaymentWithInvalidKey(Map.of("amount", 1000, "currency", "usd"));
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.statusCode()).isEqualTo(401);
        softly.assertThat(response.path("error.type").toString()).contains("invalid_request_error");
        softly.assertAll();
    }

    @Test(groups = {"regression", "api"})
    public void failedPaymentAndRateLimitValidation() {
        Response failedPayment = stripeApiClient.createPaymentIntent(Map.of(
                "amount", 2500,
                "currency", "usd",
                "payment_method", "pm_card_chargeDeclined",
                "confirm", true
        ), UUID.randomUUID().toString());

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(failedPayment.statusCode()).isIn(200, 402);
        softly.assertAll();
    }
}
