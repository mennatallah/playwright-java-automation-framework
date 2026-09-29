package io.github.mennatallah.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage {

    private final Page page;
    private final Locator items;
    private final Locator checkoutButton;

    public CartPage(Page page) {
        this.page = page;
        this.items = page.getByTestId("inventory-item");
        this.checkoutButton = page.getByTestId("checkout");
    }

    public Locator items() {
        return items;
    }

    public CheckoutInformationPage checkout() {
        checkoutButton.click();
        return new CheckoutInformationPage(page);
    }
}
