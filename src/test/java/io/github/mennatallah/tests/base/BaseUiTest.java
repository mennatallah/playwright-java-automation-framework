package io.github.mennatallah.tests.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import io.github.mennatallah.framework.config.Config;
import io.github.mennatallah.framework.core.BrowserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * One browser per test class, one fresh isolated context (clean cookies/storage) per test.
 * Every test is traced; traces and screenshots are kept only when a test fails.
 */
@Tag("ui")
@ExtendWith(FailureArtifactsExtension.class)
public abstract class BaseUiTest {

    private static Playwright playwright;
    private static Browser browser;

    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        // Stable selectors: locate elements by their data-test attribute, not by text or layout
        playwright.selectors().setTestIdAttribute("data-test");
        browser = BrowserFactory.launch(playwright);
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(Config.baseUrl())
                .setViewportSize(1366, 768));
        context.setDefaultTimeout(Config.timeoutMs());
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    Page page() {
        return page;
    }

    BrowserContext context() {
        return context;
    }
}
