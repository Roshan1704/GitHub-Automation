package com.stripe.automation.api;

import com.stripe.automation.config.ConfigManager;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class StripeApiClient {
    private final String apiKey;

    public StripeApiClient() {
        this.apiKey = ConfigManager.get("stripe.secret.key");
    }

    public Response createPaymentIntent(Map<String, Object> payload, String idempotencyKey) {
        return withRetry(() -> given()
                .spec(ApiClientFactory.stripeSpec(apiKey))
                .header("Idempotency-Key", idempotencyKey)
                .formParams(payload)
                .when().post("/v1/payment_intents")
                .then().spec(ApiClientFactory.standardResponseSpec()).extract().response());
    }

    public Response confirmPayment(String paymentIntentId, Map<String, Object> payload) {
        return withRetry(() -> given()
                .spec(ApiClientFactory.stripeSpec(apiKey))
                .formParams(payload)
                .when().post("/v1/payment_intents/" + paymentIntentId + "/confirm")
                .then().spec(ApiClientFactory.standardResponseSpec()).extract().response());
    }

    public Response createRefund(Map<String, Object> payload, String idempotencyKey) {
        return withRetry(() -> given()
                .spec(ApiClientFactory.stripeSpec(apiKey))
                .header("Idempotency-Key", idempotencyKey)
                .formParams(payload)
                .when().post("/v1/refunds")
                .then().spec(ApiClientFactory.standardResponseSpec()).extract().response());
    }

    public Response createPaymentWithInvalidKey(Map<String, Object> payload) {
        return given()
                .spec(ApiClientFactory.stripeSpec("expired_or_invalid"))
                .formParams(payload)
                .when().post("/v1/payment_intents")
                .then().extract().response();
    }

    private Response withRetry(SupplierWithResponse supplier) {
        int retries = Integer.parseInt(ConfigManager.get("api.retry.count"));
        Response response = null;
        for (int i = 0; i <= retries; i++) {
            response = supplier.get();
            if (response.statusCode() != 429 && response.statusCode() < 500) {
                return response;
            }
        }
        return response;
    }

    @FunctionalInterface
    interface SupplierWithResponse {
        Response get();
    }
}
