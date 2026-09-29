package io.github.mennatallah.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutCompletePage {

    private final Locator header;

    public CheckoutCompletePage(Page page) {
        this.header = page.getByTestId("complete-header");
    }

    public Locator header() {
        return header;
    }
}
