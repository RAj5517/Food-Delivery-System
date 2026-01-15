package com.fooddelivery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test to verify application starts successfully
 * Phase 1: Project Setup Test
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationStartupTest {

    @Test
    void contextLoads() {
        // If this test passes, Spring context loads successfully
        assertNotNull(this);
    }

    @Test
    void applicationStarts() {
        // Verify application context is loaded
        // This test ensures all configurations are correct
        assertNotNull(FoodDeliveryApplication.class);
    }
}

