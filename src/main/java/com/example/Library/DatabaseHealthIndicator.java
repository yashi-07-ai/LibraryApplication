package com.example.Library;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Add custom logic to check database connectivity
        boolean dbIsUp = checkDatabaseConnection();
        if (dbIsUp) {
            return Health.up().withDetail("Database", "Running").build();
        } else {
            return Health.down().withDetail("Database", "Not Reachable").build();
        }
    }

    private boolean checkDatabaseConnection() {
        // Simulate database connectivity check
        return true;
    }
}
