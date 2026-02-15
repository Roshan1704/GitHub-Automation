package com.stripe.automation.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.automation.model.WebhookEventRecord;
import com.stripe.automation.utils.CorrelationIdContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebhookReceiverServer {
    private final HttpServer server;
    private final ObjectMapper objectMapper;
    private final Map<String, WebhookEventRecord> eventStore;
    private final String endpointSecret;

    public WebhookReceiverServer(int port, String endpointSecret) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.objectMapper = new ObjectMapper();
        this.eventStore = new ConcurrentHashMap<>();
        this.endpointSecret = endpointSecret;
        server.createContext("/webhook", new StripeWebhookHandler());
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public boolean containsEvent(String eventId) {
        return eventStore.containsKey(eventId);
    }

    class StripeWebhookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            CorrelationIdContext.reset();
            String payload = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String signature = exchange.getRequestHeaders().getFirst("Stripe-Signature");
            if (!isValidSignature(payload, signature)) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }
            JsonNode jsonNode = objectMapper.readTree(payload);
            String eventId = jsonNode.path("id").asText();
            String eventType = jsonNode.path("type").asText();
            eventStore.putIfAbsent(eventId, new WebhookEventRecord(eventId, eventType, Instant.now(), payload));
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        }

        private boolean isValidSignature(String payload, String signature) {
            if (signature == null || signature.isBlank()) {
                return false;
            }
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(endpointSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                String computed = Base64.getEncoder().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
                return signature.contains(computed);
            } catch (Exception e) {
                return false;
            }
        }
    }
}
