package io.github.mennatallah.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.github.mennatallah.framework.forms.FormDefinition;
import io.github.mennatallah.framework.forms.FormFiller;

import java.util.Set;

/**
 * The checkout form is filled from a {@link FormDefinition} (JSON), not hardcoded field by field.
 */
public class CheckoutInformationPage {

    private final Page page;
    private final Locator continueButton;
    private final Locator error;

    public CheckoutInformationPage(Page page) {
        this.page = page;
        this.continueButton = page.getByTestId("continue");
        this.error = page.getByTestId("error");
    }

    public CheckoutInformationPage fill(FormDefinition form) {
        FormFiller.fill(page, form);
        return this;
    }

    public CheckoutInformationPage fillExcept(FormDefinition form, String fieldToLeaveEmpty) {
        FormFiller.fill(page, form, Set.of(fieldToLeaveEmpty));
        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        continueButton.click();
        return new CheckoutOverviewPage(page);
    }

    public CheckoutInformationPage submitExpectingError() {
        continueButton.click();
        return this;
    }

    public Locator error() {
        return error;
    }
}
