package io.github.mennatallah.tests.ui;

import io.github.mennatallah.framework.forms.FieldDefinition;
import io.github.mennatallah.framework.forms.FormDefinition;
import io.github.mennatallah.framework.pages.CartPage;
import io.github.mennatallah.framework.pages.CheckoutCompletePage;
import io.github.mennatallah.framework.pages.CheckoutInformationPage;
import io.github.mennatallah.framework.pages.CheckoutOverviewPage;
import io.github.mennatallah.framework.pages.InventoryPage;
import io.github.mennatallah.framework.pages.LoginPage;
import io.github.mennatallah.tests.base.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.github.mennatallah.tests.base.TestUsers.PASSWORD;
import static io.github.mennatallah.tests.base.TestUsers.STANDARD_USER;

@DisplayName("Checkout")
class CheckoutTest extends BaseUiTest {

    /** The checkout form is described in JSON: fields, types, values and validation rules. */
    private static final FormDefinition CHECKOUT_FORM =
            FormDefinition.fromResource("testdata/forms/checkout-information.json");

    @Test
    @DisplayName("User can buy a product end to end")
    void completePurchaseEndToEnd() {
        InventoryPage inventory = new LoginPage(page).open().loginAs(STANDARD_USER, PASSWORD);
        inventory.addToCart("sauce-labs-backpack");

        CartPage cart = inventory.openCart();
        assertThat(cart.items()).hasCount(1);

        CheckoutOverviewPage overview = cart.checkout()
                .fill(CHECKOUT_FORM)
                .continueToOverview();
        assertThat(overview.items()).hasCount(1);
        assertThat(overview.total()).containsText("Total: $");

        CheckoutCompletePage complete = overview.finish();
        assertThat(complete.header()).hasText("Thank you for your order!");
    }

    /**
     * One test is generated per required field in the JSON definition.
     * If a required field is added to the form, it gets validation coverage automatically.
     */
    @ParameterizedTest(name = "Missing \"{0}\" shows a validation error")
    @DisplayName("Required checkout fields are validated")
    @MethodSource("requiredCheckoutFields")
    void requiredFieldsAreValidated(String fieldTestId) {
        FieldDefinition field = CHECKOUT_FORM.field(fieldTestId);

        CheckoutInformationPage checkout = new LoginPage(page).open()
                .loginAs(STANDARD_USER, PASSWORD)
                .addToCart("sauce-labs-backpack")
                .openCart()
                .checkout()
                .fillExcept(CHECKOUT_FORM, field.testId())
                .submitExpectingError();

        assertThat(checkout.error()).containsText(field.requiredError());
    }

    static Stream<String> requiredCheckoutFields() {
        return CHECKOUT_FORM.requiredFields().stream().map(FieldDefinition::testId);
    }
}
