package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the App claddss.
 */
class AppTest {

    @Test
    void testJoinWords() {
        String result = App.joinWords("Hello", "JFrog", "Artifactory");
        assertEquals("Hello JFrog Artifactory", result);
    }

    @Test
    void testJoinWordsSingle() {
        String result = App.joinWords("Hello");
        assertEquals("Hello", result);
    }

    @Test
    void testReverseText() {
        String result = App.reverseText("JFrog");
        assertEquals("gorFJ", result);
    }

    @Test
    void testReverseTextEmpty() {
        String result = App.reverseText("");
        assertEquals("", result);
    }

    @Test
    void testCapitalizeWords() {
        String result = App.capitalizeWords("build", "and", "publish");
        assertEquals("Build And Publish", result);
    }

    @Test
    void testCapitalizeWordsAlreadyCapitalized() {
        String result = App.capitalizeWords("Already", "Done");
        assertEquals("Already Done", result);
    }

    @Test
    void testJoinWordsNotNull() {
        String result = App.joinWords("a", "b");
        assertNotNull(result);
    }

    // Tests for new vulnerable dependencies

    @Test
    void testSerializeToJson() throws Exception {
        String json = App.serializeToJson(java.util.Map.of("key", "value"));
        assertNotNull(json);
        assertTrue(json.contains("key"));
        assertTrue(json.contains("value"));
    }

    @Test
    void testParseJson() throws Exception {
        JsonNode node = App.parseJson("{\"name\":\"demo-app\",\"version\":\"1.0.1\"}");
        assertEquals("demo-app", node.get("name").asText());
        assertEquals("1.0.1", node.get("version").asText());
    }

    @Test
    void testCheckNotEmpty() {
        assertTrue(App.checkNotEmpty(Arrays.asList("a", "b")));
    }

    @Test
    void testCheckNotEmptyWithEmptyList() {
        assertFalse(App.checkNotEmpty(Collections.emptyList()));
    }
}
