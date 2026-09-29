package io.github.mennatallah.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class InventoryPage {

    private final Page page;
    private final Locator title;
    private final Locator items;
    private final Locator itemPrices;
    private final Locator sortDropdown;
    private final Locator cartBadge;
    private final Locator cartLink;

    public InventoryPage(Page page) {
        this.page = page;
        this.title = page.getByTestId("title");
        this.items = page.getByTestId("inventory-item");
        this.itemPrices = page.getByTestId("inventory-item-price");
        this.sortDropdown = page.getByTestId("product-sort-container");
        this.cartBadge = page.getByTestId("shopping-cart-badge");
        this.cartLink = page.getByTestId("shopping-cart-link");
    }

    public Locator title() {
        return title;
    }

    public Locator items() {
        return items;
    }

    public Locator cartBadge() {
        return cartBadge;
    }

    /**
     * @param productSlug product id as used by the app, e.g. "sauce-labs-backpack"
     */
    public InventoryPage addToCart(String productSlug) {
        page.getByTestId("add-to-cart-" + productSlug).click();
        return this;
    }

    /**
     * @param option one of: az, za, lohi, hilo
     */
    public InventoryPage sortBy(String option) {
        sortDropdown.selectOption(option);
        return this;
    }

    public List<Double> prices() {
        itemPrices.first().waitFor();
        return itemPrices.allTextContents().stream()
                .map(text -> Double.parseDouble(text.replace("$", "").trim()))
                .toList();
    }

    public CartPage openCart() {
        cartLink.click();
        return new CartPage(page);
    }
}
