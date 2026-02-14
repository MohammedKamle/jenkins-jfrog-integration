package com.example;

import com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.util.Map;

/**
 * Health check utility that reports the status of the application.
 * Returns a JSON health report with app info and system metrics.
 */
public class HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheck.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String APP_VERSION = "1.0.2-SNAPSHOT";

    /**
     * Returns the application status as a string: "UP" or "DOWN".
     */
    public static String getStatus() {
        return "UP";
    }

    /**
     * Returns the application version.
     */
    public static String getVersion() {
        return APP_VERSION;
    }

    /**
     * Returns a formatted application name.
     */
    public static String getAppName() {
        return StringUtils.capitalize("demo") + "-" + StringUtils.capitalize("app");
    }

    /**
     * Returns JVM uptime in seconds.
     */
    public static long getUptimeSeconds() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        return rb.getUptime() / 1000;
    }

    /**
     * Builds a health report as an immutable map.
     */
    public static Map<String, Object> buildHealthReport() {
        return ImmutableMap.<String, Object>builder()
                .put("status", getStatus())
                .put("app", getAppName())
                .put("version", getVersion())
                .put("uptimeSeconds", getUptimeSeconds())
                .put("timestamp", Instant.now().toString())
                .build();
    }

    /**
     * Returns the health report as a JSON string.
     */
    public static String getHealthReportJson() throws Exception {
        Map<String, Object> report = buildHealthReport();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);
    }

    public static void main(String[] args) throws Exception {
        logger.info("Running health check...");
        String report = getHealthReportJson();
        logger.info("Health Report:\n{}", report);
    }
}
