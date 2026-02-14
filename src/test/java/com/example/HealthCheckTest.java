package com.example;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HealthCheck class.
 */
class HealthCheckTest {

    @Test
    void testStatusIsUp() {
        assertEquals("UP", HealthCheck.getStatus());
    }

    @Test
    void testVersionIsCorrect() {
        assertEquals("1.0.2-SNAPSHOT", HealthCheck.getVersion());
    }

    @Test
    void testAppNameIsFormatted() {
        assertEquals("Demo-App", HealthCheck.getAppName());
    }

    @Test
    void testUptimeIsNonNegative() {
        assertTrue(HealthCheck.getUptimeSeconds() >= 0);
    }

    @Test
    void testHealthReportContainsRequiredFields() {
        Map<String, Object> report = HealthCheck.buildHealthReport();
        assertTrue(report.containsKey("status"));
        assertTrue(report.containsKey("app"));
        assertTrue(report.containsKey("version"));
        assertTrue(report.containsKey("uptimeSeconds"));
        assertTrue(report.containsKey("timestamp"));
    }

    @Test
    void testHealthReportStatusIsUp() {
        Map<String, Object> report = HealthCheck.buildHealthReport();
        assertEquals("UP", report.get("status"));
    }

    @Test
    void testHealthReportJsonIsValid() throws Exception {
        String json = HealthCheck.getHealthReportJson();
        assertNotNull(json);
        assertTrue(json.contains("\"status\""));
        assertTrue(json.contains("\"UP\""));
        assertTrue(json.contains("\"version\""));
    }
}
