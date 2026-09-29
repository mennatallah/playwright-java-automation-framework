package io.github.mennatallah.framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.github.mennatallah.framework.config.Config;

/**
 * Launches the browser selected in configuration (chromium, firefox or webkit).
 */
public final class BrowserFactory {

    private BrowserFactory() {
    }

    public static Browser launch(Playwright playwright) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Config.headless());

        return switch (Config.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            case "chromium" -> playwright.chromium().launch(options);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + Config.browser() + "'. Use chromium, firefox or webkit.");
        };
    }
}
