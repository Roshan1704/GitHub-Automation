package com.stripe.automation.model;

import java.time.Instant;

public record WebhookEventRecord(String id, String type, Instant receivedAt, String payload) {}
