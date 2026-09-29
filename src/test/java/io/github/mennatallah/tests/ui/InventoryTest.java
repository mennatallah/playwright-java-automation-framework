package io.github.mennatallah.tests.ui;

import io.github.mennatallah.framework.pages.InventoryPage;
import io.github.mennatallah.framework.pages.LoginPage;
import io.github.mennatallah.tests.base.BaseUiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.github.mennatallah.tests.base.TestUsers.PASSWORD;
import static io.github.mennatallah.tests.base.TestUsers.STANDARD_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product inventory")
class InventoryTest extends BaseUiTest {

    private InventoryPage inventory;

    @BeforeEach
    void logIn() {
        inventory = new LoginPage(page).open().loginAs(STANDARD_USER, PASSWORD);
        assertThat(inventory.title()).hasText("Products");
    }

    @Test
    @DisplayName("Sorting by price (low to high) orders products correctly")
    void sortByPriceLowToHigh() {
        List<Double> prices = inventory.sortBy("lohi").prices();

        List<Double> expected = prices.stream().sorted().toList();
        assertEquals(expected, prices, "Products should be sorted by ascending price");
    }

    @Test
    @DisplayName("Sorting by price (high to low) orders products correctly")
    void sortByPriceHighToLow() {
        List<Double> prices = inventory.sortBy("hilo").prices();

        List<Double> expected = prices.stream().sorted(Comparator.reverseOrder()).toList();
        assertEquals(expected, prices, "Products should be sorted by descending price");
    }

    @Test
    @DisplayName("Adding products updates the cart badge")
    void addingProductsUpdatesCartBadge() {
        inventory.addToCart("sauce-labs-backpack");
        assertThat(inventory.cartBadge()).hasText("1");

        inventory.addToCart("sauce-labs-bike-light");
        assertThat(inventory.cartBadge()).hasText("2");
    }
}
