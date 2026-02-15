package com.stripe.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        String env = System.getProperty("env", "test");
        String resource = "config/" + env + ".properties";
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new IllegalStateException("Unable to load config file: " + resource);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to initialize configuration", exception);
        }
    }

    private ConfigManager() {}

    public static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key, ""));
    }
}
