package com.example.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.subscription.util")
public class SubscriptionTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionTrackerApplication.class, args);
    }

}
