package com.stripe.automation.api;

import com.stripe.automation.config.ConfigManager;
import com.stripe.automation.utils.CorrelationIdContext;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class ApiClientFactory {
    private ApiClientFactory() {}

    public static RequestSpecification stripeSpec(String apiKey) {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("stripe.base.uri"))
                .setContentType(ContentType.URLENC)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("X-Correlation-ID", CorrelationIdContext.getId())
                .log(LogDetail.URI)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static ResponseSpecification standardResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
