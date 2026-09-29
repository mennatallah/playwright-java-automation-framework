package io.github.mennatallah.framework.forms;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * A whole form described as data (loaded from JSON).
 * Adding, removing or changing a field means editing the JSON file, not the test code.
 */
public record FormDefinition(String name, List<FieldDefinition> fields) {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static FormDefinition fromResource(String resourcePath) {
        try (InputStream in = FormDefinition.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Form definition not found: " + resourcePath);
            }
            return MAPPER.readValue(in, FormDefinition.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read form definition " + resourcePath, e);
        }
    }

    public List<FieldDefinition> requiredFields() {
        return fields.stream().filter(FieldDefinition::required).toList();
    }

    public FieldDefinition field(String testId) {
        return fields.stream()
                .filter(f -> f.testId().equals(testId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Field '" + testId + "' is not defined in form '" + name + "'"));
    }
}
