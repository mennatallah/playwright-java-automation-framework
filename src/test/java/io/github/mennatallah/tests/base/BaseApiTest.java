package io.github.mennatallah.tests.base;

import com.microsoft.playwright.Playwright;
import io.github.mennatallah.framework.api.ApiClient;
import io.github.mennatallah.framework.config.Config;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

@Tag("api")
public abstract class BaseApiTest {

    private static Playwright playwright;
    protected static ApiClient api;

    @BeforeAll
    static void createApiClient() {
        playwright = Playwright.create();
        api = new ApiClient(playwright, Config.apiBaseUrl());
    }

    @AfterAll
    static void closeApiClient() {
        if (api != null) {
            api.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
