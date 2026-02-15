# Project Guide

This file consolidates the previous fragmented project documentation into one product-grade reference.

## What this repository is
A Java-based QA automation suite for GitHub platform validation using:
- Selenium (UI)
- REST Assured (API)
- TestNG + Maven (execution)
- Allure (reporting)

## Quick setup
1. Install Java 17 and Maven 3.9+.
2. Clone repo and install dependencies:
   ```bash
   mvn -q -DskipTests compile
   ```
3. Run the default test suite:
   ```bash
   mvn test
   ```

## Test layout
- `src/test/java/com/github/qa/tests` → Test classes
- `src/test/java/com/github/qa/pages` → Page objects
- `src/test/java/com/github/qa/api` → API client(s)
- `src/test/java/com/github/qa/base` → Test bootstrap/setup
- `src/test/java/com/github/qa/reporting` → Release-readiness gate
- `src/test/resources/testng.xml` → Main suite definition

## Quality and reliability optimizations
- Dependency management simplified to explicit REST Assured versions to avoid BOM resolution failures in constrained environments.
- Documentation debt reduced by replacing many overlapping markdown files with this single guide.
- Keep only `README.md`, `QUICKSTART.md`, and this file as primary docs.

## Recommended workflow
- Use `mvn -q -DskipTests compile` for a quick health check.
- Use `mvn test` for full execution.
- Generate Allure results from `target/allure-results`.

## Notes
If UI tests fail in headless CI due to environment limitations (display/network/driver), run API-focused tests first and isolate UI runs with stable browser config.
