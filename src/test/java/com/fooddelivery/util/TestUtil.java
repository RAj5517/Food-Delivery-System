package com.fooddelivery.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for testing purposes
 */
public class TestUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert object to JSON string
     */
    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert JSON string to object
     */
    public static <T> T fromJsonString(final String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a random email for testing
     */
    public static String randomEmail() {
        return "test_" + System.currentTimeMillis() + "@test.com";
    }

    /**
     * Create a random phone number for testing
     */
    public static String randomPhone() {
        return "9" + (int) (Math.random() * 1000000000);
    }
}

