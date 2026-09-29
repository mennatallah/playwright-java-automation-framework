package io.github.mennatallah.framework.forms;

/**
 * One form field, described as data instead of code.
 *
 * @param testId        stable data-test id of the field
 * @param type          field type: text, select or checkbox
 * @param value         value to enter when filling the form
 * @param required      whether the application should reject the form when this field is empty
 * @param requiredError the validation message expected when a required field is empty
 */
public record FieldDefinition(
        String testId,
        String type,
        String value,
        boolean required,
        String requiredError) {
}
