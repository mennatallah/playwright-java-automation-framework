package io.github.mennatallah.framework.forms;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.Set;

/**
 * Fills any form from its {@link FormDefinition}, using one generic handler per field type.
 * No form-specific code is needed, so the same component works for every form in the application.
 */
public final class FormFiller {

    private FormFiller() {
    }

    public static void fill(Page page, FormDefinition form) {
        fill(page, form, Set.of());
    }

    /**
     * Fills every field except the ones listed in {@code fieldsToSkip}
     * (useful for negative tests that leave required fields empty).
     */
    public static void fill(Page page, FormDefinition form, Set<String> fieldsToSkip) {
        for (FieldDefinition field : form.fields()) {
            if (fieldsToSkip.contains(field.testId())) {
                continue;
            }
            Locator input = page.getByTestId(field.testId());
            switch (field.type()) {
                case "text" -> input.fill(field.value());
                case "select" -> input.selectOption(field.value());
                case "checkbox" -> input.setChecked(Boolean.parseBoolean(field.value()));
                default -> throw new IllegalArgumentException(
                        "Unsupported field type '" + field.type() + "' for field '" + field.testId() + "'");
            }
        }
    }
}
