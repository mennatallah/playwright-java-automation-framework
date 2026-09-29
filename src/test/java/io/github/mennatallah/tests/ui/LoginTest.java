package io.github.mennatallah.tests.ui;

import io.github.mennatallah.framework.pages.InventoryPage;
import io.github.mennatallah.framework.pages.LoginPage;
import io.github.mennatallah.tests.base.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.github.mennatallah.tests.base.TestUsers.LOCKED_OUT_USER;
import static io.github.mennatallah.tests.base.TestUsers.PASSWORD;
import static io.github.mennatallah.tests.base.TestUsers.STANDARD_USER;

@DisplayName("Login")
class LoginTest extends BaseUiTest {

    @Test
    @DisplayName("Valid user lands on the products page")
    void validUserCanLogIn() {
        InventoryPage inventory = new LoginPage(page).open().loginAs(STANDARD_USER, PASSWORD);

        assertThat(page).hasURL(Pattern.compile(".*/inventory\\.html"));
        assertThat(inventory.title()).hasText("Products");
        assertThat(inventory.items()).hasCount(6);
    }

    @Test
    @DisplayName("Locked-out user is blocked with a clear message")
    void lockedOutUserIsBlocked() {
        LoginPage login = new LoginPage(page).open().attemptLogin(LOCKED_OUT_USER, PASSWORD);

        assertThat(login.error()).containsText("Sorry, this user has been locked out");
    }

    @ParameterizedTest(name = "user=''{0}'', password=''{1}'' -> {2}")
    @DisplayName("Invalid credentials show the right validation message")
    @CsvSource({
            "'',            secret_sauce,   Username is required",
            "standard_user, '',             Password is required",
            "standard_user, wrong_password, Username and password do not match",
            "unknown_user,  secret_sauce,   Username and password do not match"
    })
    void invalidCredentialsAreRejected(String user, String password, String expectedError) {
        LoginPage login = new LoginPage(page).open().attemptLogin(user, password);

        assertThat(login.error()).containsText(expectedError);
    }
}
