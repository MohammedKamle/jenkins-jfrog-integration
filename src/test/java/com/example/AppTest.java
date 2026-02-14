package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the App class.
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
}
