package io.github.mennatallah.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Central configuration.
 * Values come from config.properties and can be overridden at run time
 * with system properties, e.g. {@code mvn test -Dheadless=false -Dbrowser=firefox}.
 */
public final class Config {

    private static final Properties PROPERTIES = load();

    private Config() {
    }

    public static String baseUrl() {
        return get("ui.baseUrl");
    }

    public static String apiBaseUrl() {
        return get("api.baseUrl");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static double timeoutMs() {
        return Double.parseDouble(get("timeout.ms"));
    }

    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing configuration key: " + key);
        }
        return value.trim();
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties was not found on the classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read config.properties", e);
        }
        return properties;
    }
}
