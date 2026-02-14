package com.example;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Vulnerable dependencies (intentionally old versions for demo)
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Simple demo application that uses Guava, Commons Lang, SLF4J,
 * and intentionally vulnerable dependencies (Log4j, Jackson, Commons Collections)
 * to demonstrate building and publishing Maven artifacts to JFrog via Jenkins.
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    // Using vulnerable Log4j 2.14.1 (CVE-2021-44228 - Log4Shell)
    private static final org.apache.logging.log4j.Logger log4jLogger =
            LogManager.getLogger(App.class);

    // Using vulnerable Jackson 2.9.8 (CVE-2019-12086)
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        logger.info("Starting demo-app v1.0.1...");

        // Using Google Guava - ImmutableList and Joiner
        String joined = joinWords("Hello", "JFrog", "Artifactory");
        logger.info("Guava Joiner result: {}", joined);

        // Using Apache Commons Lang - StringUtils
        String reversed = reverseText("JFrog");
        logger.info("Commons Lang reversed: {}", reversed);

        // Using both together
        String capitalized = capitalizeWords("build", "and", "publish");
        logger.info("Capitalized words: {}", capitalized);

        // Using vulnerable Jackson for JSON serialization
        String json = serializeToJson(Map.of("app", "demo-app", "version", "1.0.1"));
        logger.info("Jackson JSON output: {}", json);

        // Using vulnerable Log4j for logging
        log4jLogger.info("Log4j logger initialized (vulnerable version 2.14.1)");

        // Using vulnerable Commons Collections
        List<String> items = Arrays.asList("maven", "jfrog", "jenkins");
        boolean hasItems = checkNotEmpty(items);
        logger.info("Commons Collections - list has items: {}", hasItems);

        logger.info("demo-app v1.0.1 finished successfully!");
    }

    /**
     * Uses Guava's Joiner and ImmutableList to join words with a space.
     */
    public static String joinWords(String... words) {
        List<String> wordList = ImmutableList.copyOf(words);
        return Joiner.on(" ").join(wordList);
    }

    /**
     * Uses Apache Commons Lang to reverse a string.
     */
    public static String reverseText(String text) {
        return StringUtils.reverse(text);
    }

    /**
     * Uses Commons Lang to capitalize each word, then Guava to join them.
     */
    public static String capitalizeWords(String... words) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (String word : words) {
            builder.add(StringUtils.capitalize(word));
        }
        return Joiner.on(" ").join(builder.build());
    }

    /**
     * Uses vulnerable Jackson Databind 2.9.8 to serialize objects to JSON.
     * CVE-2019-12086: Polymorphic deserialization vulnerability.
     */
    public static String serializeToJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * Uses vulnerable Jackson Databind 2.9.8 to parse JSON strings.
     */
    public static JsonNode parseJson(String json) throws Exception {
        return objectMapper.readTree(json);
    }

    /**
     * Uses vulnerable Commons Collections 3.2.1.
     * CVE-2015-6420: Remote code execution via deserialization.
     */
    public static boolean checkNotEmpty(List<?> list) {
        return CollectionUtils.isNotEmpty(list);
    }
}
