package io.github.mennatallah.tests.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Runs right after each test (before @AfterEach closes the browser context).
 * On failure it saves a full-page screenshot and a Playwright trace, so failures
 * can be diagnosed from the CI artifacts without re-running anything locally.
 */
public class FailureArtifactsExtension implements AfterTestExecutionCallback {

    private static final Path ARTIFACTS_DIR = Paths.get("target", "artifacts");

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        BaseUiTest test = (BaseUiTest) extensionContext.getRequiredTestInstance();
        if (test.context() == null) {
            return;
        }

        boolean failed = extensionContext.getExecutionException().isPresent();
        if (!failed) {
            test.context().tracing().stop();
            return;
        }

        String name = safeFileName(extensionContext.getRequiredTestClass().getSimpleName()
                + "_" + extensionContext.getRequiredTestMethod().getName()
                + "_" + extensionContext.getDisplayName());
        try {
            test.page().screenshot(new Page.ScreenshotOptions()
                    .setPath(ARTIFACTS_DIR.resolve("screenshots").resolve(name + ".png"))
                    .setFullPage(true));
        } catch (RuntimeException e) {
            System.err.println("Could not capture screenshot: " + e.getMessage());
        }
        test.context().tracing().stop(new Tracing.StopOptions()
                .setPath(ARTIFACTS_DIR.resolve("traces").resolve(name + ".zip")));
    }

    private static String safeFileName(String raw) {
        return raw.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
