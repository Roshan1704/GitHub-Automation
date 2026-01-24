# GitHub E2E QA Automation Framework

Production-grade End-to-End QA automation project for GitHub's real, live, public website.

## 🎯 Project Overview

This is a **Senior/Lead-level QA automation framework** designed to test GitHub.com safely and ethically. It demonstrates enterprise-grade QA architecture, testing strategies, and quality gates—without performing any destructive, malicious, or unethical operations.

### ✅ What We Test (Safe & Real)
- **Home page load & performance** - Smoke testing, critical user journey
- **Search functionality** - Multiple search queries, filters, pagination
- **Repository pages** - Repository details, navigation, file listings
- **API integration** - REST API contracts, data consistency, error handling
- **Performance metrics** - Page load SLA compliance (3s), API response times
- **Accessibility** - Navigation, semantic HTML validation
- **Cross-browser compatibility** - Chrome and Firefox

### ❌ What We DON'T Test
- ❌ Account creation / deletion
- ❌ Payment processing
- ❌ Data scraping
- ❌ Destructive actions
- ❌ Authentication / private accounts
- ❌ Rate limiting (intentional)

---

## 🏗️ Architecture Overview

### Three-Tier Testing Model

#### **TIER 1: Real End-to-End QA (Foundation)**
Tests real user value and critical journeys:
- Home page smoke test
- Search & result filtering
- Page navigation
- Performance baselines

#### **TIER 2: Release Readiness Quality Gate (Production Safety)**
Evaluates whether a release is safe to deploy:
- Critical failure detection (home page, search, API)
- SLA compliance monitoring (3s page load, 5s API response)
- Flaky test detection
- Automated CI/CD gate for deployments

#### **TIER 3: System & API Testing (Integration)**
Tests GitHub as a distributed system:
- Public GitHub REST API validation
- Response schema and contract testing
- Error response handling (404, 429, 5xx)
- UI ↔ API data consistency
- Rate limit awareness

---

## 📁 Project Structure

```
github-e2e-qa/
├── src/test/java/
│   ├── com/github/qa/
│   │   ├── base/
│   │   │   └── BaseTest.java                      # Base test class, driver setup
│   │   ├── pages/
│   │   │   ├── HomePage.java                      # Page Object Model: Home
│   │   │   ├── SearchPage.java                    # Page Object Model: Search
│   │   │   └── RepositoryPage.java                # Page Object Model: Repository
│   │   ├── api/
│   │   │   └── GitHubAPIClient.java               # REST API client
│   │   ├── tests/
│   │   │   ├── HomePageTest.java                  # TIER 1: Home page tests
│   │   │   ├── SearchTest.java                    # TIER 1: Search tests
│   │   │   └── APITest.java                       # TIER 3: API tests
│   │   ├── utils/
│   │   │   └── WebDriverUtils.java                # Selenium utilities, explicit waits
│   │   └── reporting/
│   │       └── ReleaseReadinessGate.java          # TIER 2: Release quality gate
│   └── resources/
│       └── testng.xml                             # TestNG configuration
├── pom.xml                                        # Maven configuration
├── README.md                                      # This file
├── TEST_STRATEGY.md                               # Testing philosophy & approach
└── BUG_REPORT_SAMPLES.md                          # Production-quality bug templates
```

---

## 🛠️ Tech Stack

| Component | Technology | Purpose |
|-----------|-----------|---------|
| **UI Automation** | Selenium 4 | Cross-browser automation |
| **Dependency Mgmt** | Maven | Build & dependency management |
| **Test Framework** | TestNG 7.8+ | Test execution & reporting |
| **Driver Mgmt** | WebDriverManager | Automatic driver provisioning |
| **API Testing** | REST Assured 5 | REST API contract testing |
| **Reports** | Allure 2.21+ | Rich test reporting & visualization |
| **Logging** | SLF4J + Logback | Structured logging |
| **Assertions** | AssertJ 3.24+ | Fluent assertions |
| **CI/CD** | GitHub Actions | Automated pipeline |

---

## 📋 Prerequisites

- **Java 17+** (LTS)
- **Maven 3.8+**
- **Chrome or Firefox** (latest version)
- **Git**

---

## 🚀 Getting Started

### 1. Clone Repository
```bash
git clone <repository-url>
cd github-e2e-qa
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Run All Tests
```bash
mvn test
```

### 4. Run Specific Test Suite
```bash
# Smoke tests only
mvn test -Dgroups=smoke

# Search tests
mvn test -Dtest=SearchTest

# API tests
mvn test -Dtest=APITest
```

### 5. Run with Specific Browser
```bash
# Firefox
mvn test -Dbrowser=firefox

# Chrome (default)
mvn test -Dbrowser=chrome
```

---

## 📊 Test Execution & Reports

### Generate Allure Report
```bash
mvn clean test
mvn allure:report
mvn allure:serve
```

This opens an interactive HTML dashboard with:
- Test execution history
- Failure trends
- Timeline view
- Attachment screenshots

### View Test Logs
```bash
# Logback logs are written to console and file (if configured)
tail -f allure-results/*.log
```

---

## 🎯 TIER 2: Release Readiness Quality Gate

### How It Works

The `ReleaseReadinessGate` class evaluates whether GitHub is ready for deployment:

```java
ReleaseReadinessGate gate = new ReleaseReadinessGate();
gate.recordTestResult("HomePageTest", true, "CRITICAL", "smoke");
gate.recordPageLoadTime("HomePage", 1500);
gate.recordAPIResponseTime("searchAPI", 2300);

ReleaseStatus status = gate.evaluateRelease();
// Returns: PASS, WARN, or FAIL
```

### Quality Gate Rules

| Rule | Failure Condition | Action |
|------|------------------|--------|
| **Critical Features** | Home page doesn't load | ❌ FAIL |
| **Core Functionality** | Search is broken | ❌ FAIL |
| **API Health** | API unreachable | ❌ FAIL |
| **SLA Compliance** | Page load > 3 seconds | ⚠️ WARN |
| **Flaky Tests** | > 5% failure rate | ⚠️ WARN |
| **All Checks Pass** | No failures or warnings | ✅ PASS |

### CI/CD Integration

```yaml
# GitHub Actions Example
- name: Release Readiness Gate
  run: mvn test -Dgate=true
  # Fails if status is FAIL, succeeds if PASS/WARN
```

---

## 🔄 CI/CD Pipeline

### GitHub Actions Workflow

Location: `.github/workflows/qa-automation.yml`

**Features:**
- Runs on every PR/push
- Parallel execution (Chrome + Firefox)
- Nightly regression runs
- Allure report artifacts
- Release gate enforcement
- Slack notifications (optional)

```yaml
jobs:
  qa-automation:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        browser: [chrome, firefox]
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
      - run: mvn clean test -Dbrowser=${{ matrix.browser }}
      - name: Publish Allure Report
        uses: actions/upload-artifact@v3
```

---

## 📝 Page Object Model (POM)

### Example: HomePage.java

```java
public class HomePage {
    private final WebDriver driver;
    private final By searchInput = By.id("query-builder-test");
    
    @Step("Load GitHub home page")
    public void load() {
        driver.get("https://github.com");
        WebDriverUtils.waitForPageLoad(driver, 15);
    }
    
    @Step("Verify home page is displayed")
    public boolean isHomePageDisplayed() {
        return WebDriverUtils.isElementDisplayed(driver, pageTitle);
    }
}
```

**Benefits:**
- ✅ Centralized locators
- ✅ Reusable methods
- ✅ Reduced test code duplication
- ✅ Easy maintenance when UI changes

---

## 🔧 WebDriver Best Practices

### Explicit Waits (NOT Implicit)

```java
// ✅ CORRECT: Explicit wait
WebElement element = WebDriverUtils.waitForElementClickable(driver, locator, 15);

// ❌ WRONG: Implicit wait (fragile, slow)
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
```

### Example Test
```java
@Test
public void testSearch() {
    HomePage home = new HomePage(driver);
    home.load();
    
    SearchPage search = new SearchPage(driver);
    search.searchFor("kubernetes");
    
    assertThat(search.areSearchResultsDisplayed()).isTrue();
}
```

---

## 🛡️ Ethical & Legal Safeguards

### What This Framework NEVER Does:

1. **No Authentication Required** - Tests public areas only
2. **No Data Scraping** - No bulk downloads or API abuse
3. **No Payments** - No real transactions
4. **No Account Abuse** - No account creation/deletion/modification
5. **No Rate Limiting Stress** - Respects API rate limits
6. **No Destructive Actions** - Read-only operations only

### Why GitHub?

- ✅ Public, live website with millions of users
- ✅ Encourages security testing of public surfaces
- ✅ Rich, complex functionality to automate
- ✅ Public API for integration testing
- ✅ Zero risk: no accounts touched, no payments, no scraping

---

## 🚨 Troubleshooting

### Tests Fail with "StaleElementReferenceException"
**Cause**: Element went stale between locating and interacting  
**Solution**: Use explicit waits, avoid storing WebElement references

### Tests Fail with "TimeoutException"
**Cause**: Element not found within timeout  
**Solution**: 
- Increase timeout via method parameter
- Check if element is on page with browser DevTools
- Verify locator is correct

### WebDriver Not Starting
**Cause**: Browser not installed or WebDriverManager can't find driver  
**Solution**: 
```bash
# Chrome: Install Chrome browser
# Firefox: Install Firefox browser
# WebDriverManager will auto-download drivers
```

---

## 📚 Further Reading

- **Testing Strategy** → See `TEST_STRATEGY.md`
- **Bug Report Samples** → See `BUG_REPORT_SAMPLES.md`
- **Selenium Best Practices** → [selenium.dev](https://www.selenium.dev)
- **TestNG Documentation** → [testng.org](https://testng.org)
- **REST Assured Docs** → [rest-assured.io](https://rest-assured.io)

---

## 👤 Author

**Senior QA Architect** | Production-grade E2E Automation  
GitHub: [your-github-profile]

---

## 📄 License

This project is for educational and demonstration purposes. GitHub is a trademark of Microsoft Corporation.

---

## 🤝 Contributing

This is a reference implementation. For improvements, please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit changes (`git commit -m 'Add feature'`)
4. Push to branch (`git push origin feature/improvement`)
5. Open a Pull Request

---

## ⭐ Key Features That Stand Out

✅ **Enterprise-Grade Architecture**
- Page Object Model with explicit waits
- Centralized configuration and utilities
- Comprehensive logging and reporting

✅ **Production-Ready Quality Metrics**
- SLA monitoring (page load, API response)
- Flaky test detection
- Release readiness automation

✅ **Real-World Integration**
- GitHub REST API testing
- UI ↔ API consistency validation
- Error handling for distributed systems

✅ **Recruitment-Impressive**
- Demonstrates Lead QA thinking
- Shows understanding of test architecture
- Proves ability to balance speed vs quality

---

**This framework is designed to showcase how a senior QA architect approaches real-world testing challenges.**
