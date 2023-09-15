package com.example.subscription;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;

class SubscriptionTrackerApplicationTest {
    @Test
    void runTest() {
        String[] args = new String[]{""};
        assertAll(() -> SubscriptionTrackerApplication.main(args));
    }
}