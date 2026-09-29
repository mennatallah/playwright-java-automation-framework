package io.github.mennatallah.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator error;

    public LoginPage(Page page) {
        this.page = page;
        this.username = page.getByTestId("username");
        this.password = page.getByTestId("password");
        this.loginButton = page.getByTestId("login-button");
        this.error = page.getByTestId("error");
    }

    public LoginPage open() {
        page.navigate("/");
        return this;
    }

    public InventoryPage loginAs(String user, String pass) {
        attemptLogin(user, pass);
        return new InventoryPage(page);
    }

    public LoginPage attemptLogin(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        loginButton.click();
        return this;
    }

    public Locator error() {
        return error;
    }
}
