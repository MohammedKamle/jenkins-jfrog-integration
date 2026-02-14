package com.example;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Simple demo application that uses Guava, Commons Lang, and SLF4J
 * to demonstrate Maven dependency resolution through JFrog Artifactory.
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Starting demo-app...");

        // Using Google Guava - ImmutableList and Joiner
        String joined = joinWords("Hello", "JFrog", "Artifactory");
        logger.info("Guava Joiner result: {}", joined);

        // Using Apache Commons Lang - StringUtils
        String reversed = reverseText("JFrog");
        logger.info("Commons Lang reversed: {}", reversed);

        // Using both together
        String capitalized = capitalizeWords("build", "and", "publish");
        logger.info("Capitalized words: {}", capitalized);

        logger.info("demo-app finished successfully!");
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
}
