package com.stripe.automation.utils;

import java.util.UUID;

public final class CorrelationIdContext {
    private static final ThreadLocal<String> CORRELATION = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    private CorrelationIdContext() {}

    public static String getId() {
        return CORRELATION.get();
    }

    public static void reset() {
        CORRELATION.set(UUID.randomUUID().toString());
    }
}
