package io.github.mennatallah.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.playwright.APIResponse;
import io.github.mennatallah.framework.api.ApiClient;
import io.github.mennatallah.tests.base.BaseApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Posts API")
class PostsApiTest extends BaseApiTest {

    @Test
    @DisplayName("GET /posts/1 returns the expected post")
    void getSinglePost() {
        APIResponse response = api.get("/posts/1");

        assertThat(response).isOK();
        JsonNode post = ApiClient.json(response);
        assertAll(
                () -> assertEquals(1, post.get("id").asInt()),
                () -> assertEquals(1, post.get("userId").asInt()),
                () -> assertFalse(post.get("title").asText().isBlank(), "title should not be blank"),
                () -> assertFalse(post.get("body").asText().isBlank(), "body should not be blank")
        );
    }

    @Test
    @DisplayName("GET /posts returns all 100 posts with the required fields")
    void getAllPosts() {
        APIResponse response = api.get("/posts");

        assertThat(response).isOK();
        JsonNode posts = ApiClient.json(response);
        assertEquals(100, posts.size());
        for (JsonNode post : posts) {
            assertTrue(post.hasNonNull("id") && post.hasNonNull("userId")
                            && post.hasNonNull("title") && post.hasNonNull("body"),
                    "Every post must have id, userId, title and body: " + post);
        }
    }

    @Test
    @DisplayName("GET /posts?userId=1 returns only that user's posts")
    void filterPostsByUser() {
        APIResponse response = api.get("/posts?userId=1");

        assertThat(response).isOK();
        JsonNode posts = ApiClient.json(response);
        assertFalse(posts.isEmpty(), "Expected at least one post for user 1");
        for (JsonNode post : posts) {
            assertEquals(1, post.get("userId").asInt());
        }
    }

    @Test
    @DisplayName("POST /posts creates a post and returns 201")
    void createPost() {
        Map<String, Object> newPost = Map.of(
                "title", "Automation with Playwright",
                "body", "Created by an automated API test",
                "userId", 1);

        APIResponse response = api.post("/posts", newPost);

        assertEquals(201, response.status());
        JsonNode created = ApiClient.json(response);
        assertAll(
                () -> assertTrue(created.hasNonNull("id"), "Created post should have an id"),
                () -> assertEquals("Automation with Playwright", created.get("title").asText()),
                () -> assertEquals("Created by an automated API test", created.get("body").asText()),
                () -> assertEquals(1, created.get("userId").asInt())
        );
    }

    @Test
    @DisplayName("PUT /posts/1 updates the post")
    void updatePost() {
        Map<String, Object> updated = Map.of(
                "id", 1,
                "title", "Updated title",
                "body", "Updated body",
                "userId", 1);

        APIResponse response = api.put("/posts/1", updated);

        assertThat(response).isOK();
        JsonNode post = ApiClient.json(response);
        assertEquals("Updated title", post.get("title").asText());
        assertEquals("Updated body", post.get("body").asText());
    }

    @Test
    @DisplayName("DELETE /posts/1 succeeds")
    void deletePost() {
        APIResponse response = api.delete("/posts/1");

        assertThat(response).isOK();
    }

    @Test
    @DisplayName("GET a post that does not exist returns 404")
    void missingPostReturns404() {
        APIResponse response = api.get("/posts/99999");

        assertEquals(404, response.status());
    }
}
