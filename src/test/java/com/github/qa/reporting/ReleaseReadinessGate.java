package com.github.qa.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * TIER 2: Release Readiness Quality Gate
 * Evaluates whether a release is production-ready based on QA metrics
 */
public class ReleaseReadinessGate {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseReadinessGate.class);

    private static final int HOME_PAGE_LOAD_TIME_SLA_MS = 3000;
    private static final int API_RESPONSE_TIME_SLA_MS = 5000;
    private static final double MIN_SEARCH_RESULTS_THRESHOLD = 1;
    private static final double MAX_FLAKY_TEST_PERCENTAGE = 5.0;
    private static final double MIN_PASS_RATE_CRITICAL = 95.0;  // Rule 8
    private static final double MIN_PASS_RATE_WARNING = 90.0;   // Rule 9
    private static final int MAX_CONSECUTIVE_FAILURES = 3;      // Rule 10
    private static final long MIN_API_AVAILABILITY = 99;        // Rule 11 (percentage)

    private Map<String, TestResult> testResults = new HashMap<>();
    private Map<String, Integer> pageLoadTimes = new HashMap<>();
    private Map<String, Integer> apiResponseTimes = new HashMap<>();
    private Map<String, Integer> testRetryCount = new HashMap<>();
    private Map<String, Integer> apiFailureCount = new HashMap<>();
    private int totalAPIRequests = 0;

    public enum ReleaseStatus {
        PASS("✅ APPROVED - Release to production"),
        WARN("⚠️  WARNING - Monitor in production"),
        FAIL("❌ BLOCKED - Do not release");

        private final String message;

        ReleaseStatus(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class TestResult {
        public String testName;
        public boolean passed;
        public String severity;
        public String category;

        public TestResult(String testName, boolean passed, String severity, String category) {
            this.testName = testName;
            this.passed = passed;
            this.severity = severity;
            this.category = category;
        }
    }

    /**
     * Record test result
     */
    public void recordTestResult(String testName, boolean passed, String severity, String category) {
        testResults.put(testName, new TestResult(testName, passed, severity, category));
        logger.info("Test recorded: {} | Status: {} | Severity: {}", testName, passed ? "PASS" : "FAIL", severity);
    }

    /**
     * Record page load time
     */
    public void recordPageLoadTime(String pageName, int loadTimeMs) {
        pageLoadTimes.put(pageName, loadTimeMs);
        logger.info("Page load time recorded: {} | Time: {}ms", pageName, loadTimeMs);
    }

    /**
     * Record API response time
     */
    public void recordAPIResponseTime(String endpoint, int responseTimeMs) {
        apiResponseTimes.put(endpoint, responseTimeMs);
        logger.info("API response time recorded: {} | Time: {}ms", endpoint, responseTimeMs);
    }

    /**
     * Record test retry count (for flaky test detection)
     */
    public void recordTestRetry(String testName, int retryCount) {
        testRetryCount.put(testName, retryCount);
        logger.info("Test retry recorded: {} | Retries: {}", testName, retryCount);
    }

    /**
     * Record API failure
     */
    public void recordAPIFailure(String endpoint) {
        apiFailureCount.merge(endpoint, 1, Integer::sum);
        totalAPIRequests++;
        logger.warn("API failure recorded: {}", endpoint);
    }

    /**
     * Record successful API call
     */
    public void recordAPISuccess(String endpoint) {
        totalAPIRequests++;
        logger.debug("API success recorded: {}", endpoint);
    }

    /**
     * Evaluate release readiness - 11 Quality Gate Rules
     */
    public ReleaseStatus evaluateRelease() {
        logger.info("=== EVALUATING RELEASE READINESS (11 Quality Gate Rules) ===");

        // Check critical failures
        List<String> criticalFailures = new ArrayList<>();

        // Rule 1: Home page must load
        if (!testResults.getOrDefault("HomePageLoad", new TestResult("", true, "", "")).passed) {
            criticalFailures.add("❌ RULE 1: Home page failed to load");
        }

        // Rule 2: Search must return results
        if (!testResults.getOrDefault("SearchTest", new TestResult("", true, "", "")).passed) {
            criticalFailures.add("❌ RULE 2: Search functionality is broken");
        }

        // Rule 3: API health check
        if (!testResults.getOrDefault("APIHealthCheck", new TestResult("", true, "", "")).passed) {
            criticalFailures.add("❌ RULE 3: API health check failed");
        }

        // Rule 4: Critical navigation
        List<String> navigationIssues = checkNavigationIntegrity();
        criticalFailures.addAll(navigationIssues);

        // Rule 5: SLA breaches
        List<String> slaBreaches = checkSLACompliance();
        
        // Rule 6: Flaky tests
        List<String> flakyTestWarnings = checkFlakyTests();

        // Rule 7: Check consecutive failures
        List<String> consecutiveFailureWarnings = checkConsecutiveFailures();

        // Rule 8: Check overall pass rate (critical)
        List<String> passRateCritical = checkPassRateCritical();
        if (!passRateCritical.isEmpty()) {
            criticalFailures.addAll(passRateCritical);
        }

        // Rule 9: Check overall pass rate (warning)
        List<String> passRateWarnings = checkPassRateWarning();

        // Rule 10: Check API availability
        List<String> apiAvailabilityIssues = checkAPIAvailability();
        if (!apiAvailabilityIssues.isEmpty()) {
            criticalFailures.addAll(apiAvailabilityIssues);
        }

        // Rule 11: Check performance degradation
        List<String> performanceDegradation = checkPerformanceDegradation();

        // Determine release status
        ReleaseStatus status = determineStatus(criticalFailures, slaBreaches, flakyTestWarnings, 
                                               consecutiveFailureWarnings, passRateWarnings, performanceDegradation);

        return status;
    }

    /**
     * Rule 7: Check for consecutive test failures
     */
    private List<String> checkConsecutiveFailures() {
        List<String> warnings = new ArrayList<>();
        int consecutiveFailures = 0;

        for (TestResult result : testResults.values()) {
            if (!result.passed) {
                consecutiveFailures++;
                if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                    warnings.add("⚠️  RULE 7: Consecutive test failures detected (" + 
                               consecutiveFailures + ")");
                    break;
                }
            } else {
                consecutiveFailures = 0;
            }
        }

        logger.info("Consecutive failures check: {} consecutive failures found", consecutiveFailures);
        return warnings;
    }

    /**
     * Rule 8: Check pass rate is above critical threshold
     */
    private List<String> checkPassRateCritical() {
        List<String> failures = new ArrayList<>();
        long passedTests = testResults.values().stream().filter(r -> r.passed).count();
        double passRate = (double) passedTests / testResults.size() * 100;

        if (passRate < MIN_PASS_RATE_CRITICAL) {
            failures.add("❌ RULE 8: Pass rate below critical threshold: " + 
                        String.format("%.1f", passRate) + "% (min required: " + 
                        MIN_PASS_RATE_CRITICAL + "%)");
        }

        logger.info("Pass rate (critical) check: {}", String.format("%.1f", passRate) + "%");
        return failures;
    }

    /**
     * Rule 9: Check pass rate is above warning threshold
     */
    private List<String> checkPassRateWarning() {
        List<String> warnings = new ArrayList<>();
        long passedTests = testResults.values().stream().filter(r -> r.passed).count();
        double passRate = (double) passedTests / testResults.size() * 100;

        if (passRate < MIN_PASS_RATE_WARNING && passRate >= MIN_PASS_RATE_CRITICAL) {
            warnings.add("⚠️  RULE 9: Pass rate in warning range: " + 
                        String.format("%.1f", passRate) + "% (warning threshold: " + 
                        MIN_PASS_RATE_WARNING + "%)");
        }

        logger.info("Pass rate (warning) check: {}", String.format("%.1f", passRate) + "%");
        return warnings;
    }

    /**
     * Rule 10: Check API availability/reliability
     */
    private List<String> checkAPIAvailability() {
        List<String> issues = new ArrayList<>();

        if (totalAPIRequests > 0) {
            long totalFailures = apiFailureCount.values().stream().mapToLong(Long::valueOf).sum();
            long availability = ((totalAPIRequests - totalFailures) * 100) / totalAPIRequests;

            if (availability < MIN_API_AVAILABILITY) {
                issues.add("❌ RULE 10: API availability below threshold: " + availability + 
                          "% (min required: " + MIN_API_AVAILABILITY + "%)");
            }

            logger.info("API availability check: {}%", availability);
        }

        return issues;
    }

    /**
     * Rule 11: Check for performance degradation
     */
    private List<String> checkPerformanceDegradation() {
        List<String> warnings = new ArrayList<>();
        int slowPageLoads = 0;
        int slowAPIResponses = 0;

        for (Integer time : pageLoadTimes.values()) {
            if (time > HOME_PAGE_LOAD_TIME_SLA_MS * 1.5) {
                slowPageLoads++;
            }
        }

        for (Integer time : apiResponseTimes.values()) {
            if (time > API_RESPONSE_TIME_SLA_MS * 1.5) {
                slowAPIResponses++;
            }
        }

        if (slowPageLoads > 0 || slowAPIResponses > 0) {
            warnings.add("⚠️  RULE 11: Performance degradation detected - " +
                        slowPageLoads + " slow page loads, " + slowAPIResponses + " slow API responses");
        }

        logger.info("Performance degradation check: {} slow pages, {} slow APIs", 
                   slowPageLoads, slowAPIResponses);
        return warnings;
    }

    /**
     * Check navigation integrity (critical journeys)
     */
    private List<String> checkNavigationIntegrity() {
        List<String> issues = new ArrayList<>();
        
        // Home -> Search navigation
        if (!testResults.getOrDefault("SearchTest", new TestResult("", true, "", "")).passed) {
            issues.add("❌ Home → Search journey broken");
        }
        
        logger.info("Navigation integrity check: {} issues found", issues.size());
        return issues;
    }

    /**
     * Check SLA compliance
     */
    private List<String> checkSLACompliance() {
        List<String> breaches = new ArrayList<>();
        
        // Page load SLA
        pageLoadTimes.forEach((page, time) -> {
            if (time > HOME_PAGE_LOAD_TIME_SLA_MS) {
                breaches.add("⚠️  SLA BREACH: " + page + " load time " + time + "ms (SLA: " + 
                           HOME_PAGE_LOAD_TIME_SLA_MS + "ms)");
            }
        });

        // API response SLA
        apiResponseTimes.forEach((endpoint, time) -> {
            if (time > API_RESPONSE_TIME_SLA_MS) {
                breaches.add("⚠️  SLA BREACH: " + endpoint + " response time " + time + "ms (SLA: " + 
                           API_RESPONSE_TIME_SLA_MS + "ms)");
            }
        });

        logger.info("SLA compliance check: {} breaches found", breaches.size());
        return breaches;
    }

    /**
     * Check for flaky tests (tests that sometimes fail)
     */
    private List<String> checkFlakyTests() {
        List<String> warnings = new ArrayList<>();
        
        // Count test failures
        long failedTests = testResults.values().stream()
                .filter(r -> !r.passed)
                .count();
        
        double failureRate = (double) failedTests / testResults.size() * 100;
        
        if (failureRate > MAX_FLAKY_TEST_PERCENTAGE && failureRate < 50) {
            warnings.add("⚠️  FLAKY TESTS DETECTED: " + String.format("%.1f", failureRate) + 
                        "% failure rate (threshold: " + MAX_FLAKY_TEST_PERCENTAGE + "%)");
        }

        logger.info("Flaky test check: failure rate {}", String.format("%.1f", failureRate) + "%");
        return warnings;
    }

    /**
     * Determine final release status (with 11 quality gate rules)
     */
    private ReleaseStatus determineStatus(List<String> criticalFailures, 
                                          List<String> slaBreaches,
                                          List<String> flakyTests,
                                          List<String> consecutiveFailures,
                                          List<String> passRateWarnings,
                                          List<String> performanceIssues) {
        if (!criticalFailures.isEmpty()) {
            logger.error("RELEASE BLOCKED - Critical failures found");
            criticalFailures.forEach(logger::error);
            return ReleaseStatus.FAIL;
        }

        if (!slaBreaches.isEmpty() || !flakyTests.isEmpty() || !consecutiveFailures.isEmpty() ||
            !passRateWarnings.isEmpty() || !performanceIssues.isEmpty()) {
            logger.warn("RELEASE WARNING - Issues detected");
            slaBreaches.forEach(logger::warn);
            flakyTests.forEach(logger::warn);
            consecutiveFailures.forEach(logger::warn);
            passRateWarnings.forEach(logger::warn);
            performanceIssues.forEach(logger::warn);
            return ReleaseStatus.WARN;
        }

        logger.info("RELEASE APPROVED - All 11 quality gate rules passed");
        return ReleaseStatus.PASS;
    }

    /**
     * Generate release readiness report
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n");
        report.append("╔════════════════════════════════════════════════╗\n");
        report.append("║     RELEASE READINESS QUALITY GATE REPORT      ║\n");
        report.append("╚════════════════════════════════════════════════╝\n\n");

        report.append("📊 Test Execution Summary:\n");
        long passedCount = testResults.values().stream().filter(r -> r.passed).count();
        long failedCount = testResults.size() - passedCount;
        report.append(String.format("   ✅ Passed: %d\n", passedCount));
        report.append(String.format("   ❌ Failed: %d\n", failedCount));
        report.append(String.format("   📈 Pass Rate: %.1f%%\n\n", (passedCount * 100.0) / testResults.size()));

        report.append("⚡ Performance Metrics:\n");
        if (!pageLoadTimes.isEmpty()) {
            report.append("   Page Load Times:\n");
            pageLoadTimes.forEach((page, time) -> {
                String status = time <= HOME_PAGE_LOAD_TIME_SLA_MS ? "✅" : "⚠️";
                report.append(String.format("     %s %s: %dms (SLA: %dms)\n", 
                    status, page, time, HOME_PAGE_LOAD_TIME_SLA_MS));
            });
        }

        if (!apiResponseTimes.isEmpty()) {
            report.append("\n   API Response Times:\n");
            apiResponseTimes.forEach((endpoint, time) -> {
                String status = time <= API_RESPONSE_TIME_SLA_MS ? "✅" : "⚠️";
                report.append(String.format("     %s %s: %dms (SLA: %dms)\n", 
                    status, endpoint, time, API_RESPONSE_TIME_SLA_MS));
            });
        }

        report.append("\n📋 Release Decision:\n");
        ReleaseStatus status = evaluateRelease();
        report.append("   " + status.getMessage() + "\n");

        report.append("\n🕐 Report Generated: ");
        report.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        report.append("\n");

        return report.toString();
    }
}
