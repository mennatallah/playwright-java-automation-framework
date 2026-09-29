package io.github.mennatallah.framework.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

/**
 * Thin wrapper around Playwright's APIRequestContext.
 * The same client can be used for pure API tests and for fast test-data setup in UI tests.
 */
public class ApiClient implements AutoCloseable {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final APIRequestContext request;

    public ApiClient(Playwright playwright, String baseUrl) {
        this.request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl)
                .setExtraHTTPHeaders(Map.of("Accept", "application/json")));
    }

    public APIResponse get(String path) {
        return request.get(path);
    }

    public APIResponse post(String path, Object body) {
        return request.post(path, jsonBody(body));
    }

    public APIResponse put(String path, Object body) {
        return request.put(path, jsonBody(body));
    }

    public APIResponse delete(String path) {
        return request.delete(path);
    }

    public static JsonNode json(APIResponse response) {
        try {
            return MAPPER.readTree(response.text());
        } catch (IOException e) {
            throw new UncheckedIOException("Response body is not valid JSON: " + response.text(), e);
        }
    }

    private static RequestOptions jsonBody(Object body) {
        return RequestOptions.create()
                .setHeader("Content-Type", "application/json; charset=UTF-8")
                .setData(body);
    }

    @Override
    public void close() {
        request.dispose();
    }
}
