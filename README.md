# GitHub Automation Framework

## 🚀 Real-World Platform QA for GitHub Engineering Workflows

This repository contains a **production-grade QA automation framework** built to validate **real GitHub engineering workflows** using **public APIs and public UI surfaces**.

This is **not a demo project**.
It is designed to demonstrate how **senior QA / SDET / Platform QA engineers** think about **quality at system level**, not just feature testing.

---

## 🎯 Why This Project Exists

Most automation examples focus on isolated UI flows or mock APIs.

In real companies, quality breaks when:
- Pull requests fail silently
- APIs drift from expected contracts
- CI pipelines give false confidence
- Platform changes impact multiple teams

This framework focuses on **confidence-building automation** for **developer platforms**.

---

## 🧩 What This Framework Tests (Real & Ethical)

### ✅ Platform Workflows
- Public repository discovery & validation
- Repository metadata consistency
- Pull request lifecycle (read-only validation)
- Issues & workflow visibility

### ✅ API-First Quality
- GitHub REST API contract validation
- Status codes, schemas, pagination
- Error handling (404, 403, 429, 5xx)
- API ↔ UI consistency checks

### ✅ Release Readiness Signals
- Critical workflow availability
- API health checks
- Performance baselines
- Failure aggregation for go/no-go decisions

---

## ❌ What This Framework Explicitly Does NOT Do

- ❌ No authentication abuse
- ❌ No account creation/deletion
- ❌ No data scraping
- ❌ No rate-limit stress testing
- ❌ No destructive actions

This project follows **ethical, read-only testing principles**.

---

## 🏗️ Architecture Overview

```
┌────────────────────────────┐
│   Test Scenarios Layer     │
│  (Repo, PR, Issues)       │
└────────────▲───────────────┘
             │
┌────────────┴───────────────┐
│   Test Logic Layer         │
│  (Assertions, Flows)      │
└────────────▲───────────────┘
             │
┌────────────┴───────────────┐
│   API & UI Client Layer   │
│  (GitHub REST + UI)       │
└────────────▲───────────────┘
             │
┌────────────┴───────────────┐
│   Config & CI Layer       │
│  (Env, Tokens, Pipelines)│
└────────────────────────────┘
```

---

## 🛠️ Tech Stack

- Java  
- Selenium 4  
- REST Assured  
- TestNG  
- Maven  
- GitHub Actions  
- Allure  

---

## 🔄 CI/CD Integration

Designed to run:
- On every PR
- On scheduled regression runs
- As a **quality gate** before release

---

## 🧠 Engineering Mindset

This project demonstrates:
- API-first validation over brittle UI-only tests
- Quality gates instead of pass/fail test lists
- Confidence-based release decisions

> _Quality engineering is not about finding bugs late.  
> It’s about building systems that make failures obvious — early._

---

## 👤 Author

**Roshan Singh**  
Senior QA / Automation Engineer

## 📚 Documentation
- Quickstart: `QUICKSTART.md`
- Consolidated guide: `docs/PROJECT_GUIDE.md`


## ✅ Recommended Optimization Approach
For your prompt ("make it product-grade" + reduce markdown clutter), the best approach is:
1. Keep **3 active docs** only: `README.md`, `QUICKSTART.md`, `docs/PROJECT_GUIDE.md`.
2. Merge overlapping content into `docs/PROJECT_GUIDE.md` instead of maintaining many partial summaries.
3. Keep build configuration production-ready (test/report plugins enabled) while removing brittle dependency BOM usage.

This gives lower maintenance cost without losing usability for onboarding and execution.

