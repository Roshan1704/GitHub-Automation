# Quick Start Guide — GitHub QA Automation

Get the framework running in 5 minutes.

---

## ⚡ 5-Minute Setup

### 1. Prerequisites (2 minutes)

```bash
# Verify Java is installed (11+)
java -version

# Verify Maven is installed
mvn -version

# If not installed:
# macOS: brew install java maven
# Windows: Download from oracle.com & maven.apache.org
# Linux: sudo apt-get install default-jdk maven
```

### 2. Clone & Install (2 minutes)

```bash
# Clone repository
git clone <repository-url>
cd github-e2e-qa

# Download dependencies
mvn clean install
# First time takes ~3 minutes, then cached locally
```

### 3. Run Tests (1 minute)

```bash
# Run all tests
mvn test

# You'll see:
# [INFO] -------------------------------------------------------
# [INFO]  T E S T S
# [INFO] -------------------------------------------------------
# [INFO] Running com.github.qa.tests.HomePageTest
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
# ...
# [INFO] BUILD SUCCESS
```

✅ **Done!** Tests are running.

---

## 📊 View Test Reports

### Option 1: Allure Report (Recommended)

```bash
# Generate and open interactive dashboard
mvn allure:serve

# Opens browser automatically with:
# ✅ Test results with trends
# ✅ Timeline view
# ✅ Failure analysis
# ✅ Attachments (screenshots, videos)
```

### Option 2: Console Output

```bash
# View test results in console
mvn test | grep -E "Tests run|SUCCESS|FAILURE"
```

### Option 3: Raw Test Logs

```bash
# Find test logs
find target/logs -name "*.log" -type f

# View specific log
cat target/logs/com.github.qa.tests.HomePageTest.log
```

---

## 🎯 Run Specific Tests

### Run Single Test Class
```bash
mvn test -Dtest=HomePageTest
```

### Run Single Test Method
```bash
mvn test -Dtest=HomePageTest#testHomePageLoad
```

### Run All Search Tests
```bash
mvn test -Dtest=SearchTest
```

### Run API Tests Only
```bash
mvn test -Dtest=APITest
```

---

## 🌐 Run with Different Browsers

### Chrome (Default)
```bash
mvn test -Dbrowser=chrome
```

### Firefox
```bash
mvn test -Dbrowser=firefox
```

### Both (Parallel)
```bash
mvn test -Dbrowser=chrome,firefox
```

---

## 🔧 Advanced Options

### Verbose Logging
```bash
mvn test -X
# Shows detailed Maven and test output
```

### Skip Browser GUI
```bash
mvn test -Dheadless=true
# Runs tests without opening browser window
```

### Set Custom Timeout
```bash
mvn test -Dtimeout=30
# Sets explicit wait timeout to 30 seconds
```

### Run in Parallel
```bash
mvn test -DparallelCount=4
# Runs tests in 4 parallel threads (faster)
```

---

## 📁 Project Structure

```
github-e2e-qa/
│
├── src/test/java/com/github/qa/
│   ├── base/
│   │   └── BaseTest.java
│   ├── pages/
│   │   ├── HomePage.java
│   │   ├── SearchPage.java
│   │   └── RepositoryPage.java
│   ├── api/
│   │   └── GitHubAPIClient.java
│   ├── tests/
│   │   ├── HomePageTest.java      ← Tier 1 tests
│   │   ├── SearchTest.java        ← Tier 1 tests
│   │   └── APITest.java           ← Tier 3 tests
│   ├── utils/
│   │   └── WebDriverUtils.java
│   └── reporting/
│       └── ReleaseReadinessGate.java ← Tier 2 gate
│
├── src/test/resources/
│   └── testng.xml
│
├── pom.xml                     ← Dependencies & config
├── README.md                   ← Full documentation
├── TEST_STRATEGY.md            ← Testing philosophy
├── BUG_REPORT_SAMPLES.md       ← Professional bug examples
├── PROJECT_SUMMARY.md          ← High-level overview
└── QUICKSTART.md               ← This file
```

---

## 🚀 Common Commands Cheat Sheet

| Command | Purpose |
|---------|---------|
| `mvn test` | Run all tests |
| `mvn test -Dtest=HomePageTest` | Run one class |
| `mvn test -Dbrowser=firefox` | Run with Firefox |
| `mvn allure:serve` | View Allure report |
| `mvn clean` | Clean build artifacts |
| `mvn clean install` | Full fresh install |
| `mvn test -X` | Verbose output |
| `mvn test -DskipTests` | Build without running tests |

---

## ❓ Troubleshooting

### Error: "WebDriver not found"
```
Solution: WebDriverManager auto-downloads drivers
If issue persists:
1. Delete ~/.wdm folder (WebDriver cache)
2. Run: mvn clean install
3. Run tests again
```

### Error: "Element not found / TimeoutException"
```
Solution:
1. Verify you have internet (tests hit real GitHub)
2. Check if GitHub.com is accessible
3. Increase timeout: mvn test -Dtimeout=30
4. Check locators match current GitHub UI
```

### Error: "Port 4444 already in use"
```
Solution: Kill existing driver process
macOS/Linux: lsof -i :4444 | kill -9
Windows: netstat -ano | findstr :4444
```

### Tests pass locally but fail in CI
```
Likely causes:
1. Different timing (CI is slower) → increase timeouts
2. Different browser versions → use WebDriverManager
3. Network issues → add retry logic
4. Check CI logs for detailed error messages
```

---

## 📈 Understanding Test Results

### Successful Run
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.github.qa.tests.HomePageTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.github.qa.tests.SearchTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.github.qa.tests.APITest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] -------------------------------------------------------
[INFO] BUILD SUCCESS
```

✅ **All 20 tests passed!**

### With Failures
```
[INFO] Tests run: 20, Failures: 1, Errors: 0, Skipped: 0

FAILURE: testRepositoryPageSLA (APITest)
java.lang.AssertionError: 
  Expected: API response < 5000ms
  But was: 7234ms
```

⚠️ **1 test failed** — Check detailed logs in `target/` folder

---

## 🎓 Next Steps

### 1. Read Documentation
- **README.md** — Full feature list, architecture, tech stack
- **TEST_STRATEGY.md** — Why tests are designed this way
- **BUG_REPORT_SAMPLES.md** — How to report bugs professionally

### 2. Explore Code
```bash
# Open test examples
cat src/test/java/com/github/qa/tests/HomePageTest.java

# View Page Objects
cat src/test/java/com/github/qa/pages/HomePage.java

# Check utilities
cat src/test/java/com/github/qa/utils/WebDriverUtils.java
```

### 3. Modify & Experiment
```bash
# Edit a test
nano src/test/java/com/github/qa/tests/SearchTest.java

# Add new search term
# searchPage.searchFor("golang");

# Run modified test
mvn test -Dtest=SearchTest
```

### 4. Create Your Own Test
```bash
# 1. Create new test class
touch src/test/java/com/github/qa/tests/MyTest.java

# 2. Write test following existing patterns
# 3. Run: mvn test -Dtest=MyTest

# See examples in SearchTest.java or APITest.java
```

---

## 📊 Release Readiness Gate

The framework includes an automated quality gate (TIER 2) that determines if a release is safe:

### Check Release Status
```bash
mvn test -Dgate=true

# Output example:
# ╔════════════════════════════════════════════╗
# ║     RELEASE READINESS QUALITY GATE REPORT  ║
# ╠════════════════════════════════════════════╣
# ║ Tests Passed: 19/20 (95.0%)                ║
# ║ Page Load SLA: ✅ (1.2s < 3s)              ║
# ║ API Response SLA: ✅ (800ms < 5s)          ║
# ║ Critical Failures: 0                       ║
# ║ RELEASE STATUS: ⚠️ WARN (1 test failure)  ║
# ╚════════════════════════════════════════════╝
```

### Gate Rules
- ❌ **FAIL** — Home page down, search broken, critical paths fail
- ⚠️ **WARN** — Performance SLA breached, 1-2 tests fail
- ✅ **PASS** — All systems operational, safe to deploy

---

## 🔗 Integration with CI/CD

The framework is pre-configured for GitHub Actions CI/CD:

### On Pull Request
```
1. Tests run automatically on Chrome + Firefox
2. Allure report generated
3. Release gate evaluated
4. Comment added to PR with status
```

### On Push to Main
```
1. Full regression suite runs
2. Report artifacts uploaded
3. Slack notification sent
```

### Scheduled (Nightly)
```
Runs at 2 AM UTC:
1. Complete test suite
2. Trend analysis generated
3. Email report sent
```

---

## 💡 Tips & Best Practices

### 1. Run Tests Locally Before Pushing
```bash
mvn clean test
```

### 2. Keep Tests Green
If test breaks:
1. Run locally to reproduce
2. Check if it's flaky (run 3 times)
3. Fix or quarantine if necessary
4. Update documentation

### 3. Watch Test Trends
```bash
# Open Allure report weekly
mvn allure:serve

# Look for:
# - Increasing failure rate?
# - Slower tests?
# - New flaky tests?
```

### 4. Use Descriptive Test Names
```
❌ testSearch1
✅ testSearchReturnsMultipleResults

❌ test2
✅ testFirstResultClickableAndNavigates
```

### 5. Keep Tests Focused
```
One test = one user action
Don't test multiple things in one @Test
```

---

## 📞 Support

### Questions?
1. Check README.md sections
2. Review TEST_STRATEGY.md
3. Look at similar test examples
4. Check logs: `target/logs/`

### Found a Bug?
1. Reproduce it locally
2. Create issue with: steps, expected, actual
3. Attach logs/screenshot
4. See BUG_REPORT_SAMPLES.md for format

### Want to Contribute?
1. Fork repository
2. Create feature branch
3. Make changes
4. Run tests: `mvn test`
5. Submit PR

---

## 🎉 You're Ready!

```
✅ Framework installed
✅ Tests running
✅ Reports available
✅ Documentation available

Next: Read README.md for deep dive into architecture
```

**Happy testing!**

---

**Quick Start Version**: 1.0  
**Last Updated**: 2026-01-23
