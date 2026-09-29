package io.github.mennatallah.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutOverviewPage {

    private final Page page;
    private final Locator items;
    private final Locator totalLabel;
    private final Locator finishButton;

    public CheckoutOverviewPage(Page page) {
        this.page = page;
        this.items = page.getByTestId("inventory-item");
        this.totalLabel = page.getByTestId("total-label");
        this.finishButton = page.getByTestId("finish");
    }

    public Locator items() {
        return items;
    }

    public Locator total() {
        return totalLabel;
    }

    public CheckoutCompletePage finish() {
        finishButton.click();
        return new CheckoutCompletePage(page);
    }
}
