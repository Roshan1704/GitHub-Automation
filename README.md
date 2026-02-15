# Stripe Test Automation Framework

Production-grade Java 17 automation framework for Stripe test-mode workflows covering UI, API, and webhook validation.

## Tech Stack
- Java 17+
- Maven
- Selenium WebDriver
- REST Assured
- TestNG
- Cucumber BDD + POM
- Jackson
- SLF4J + Logback
- Allure reporting
- GitHub Actions CI/CD

## Folder Structure
```text
src
├── main/java/com/stripe/automation
│   ├── api
│   ├── base
│   ├── config
│   ├── drivers
│   ├── model
│   ├── ui/pages
│   ├── utils
│   └── webhook
└── test
    ├── java/com/stripe/automation
    │   ├── hooks
    │   ├── listeners
    │   ├── runners
    │   ├── stepdefinitions
    │   └── tests
    └── resources
        ├── config
        ├── testdata/features/ui
        ├── testdata/api/schemas
        └── testng.xml
```

## Stripe Coverage
### UI
- Login to Stripe dashboard
- Validate payments page
- Search payment
- Validate refund status
- Validate filters/pagination

### API
- Create payment intent
- Confirm payment
- Failed payment scenario
- Full/partial and duplicate refund validation
- Idempotency validation
- Invalid/expired key negative test
- Rate limit handling

### Webhook
- Java HTTP receiver endpoint
- Signature verification
- Duplicate event idempotency handling
- Replay/retry validation hooks

## Prerequisites
Set environment variables:
- `STRIPE_SECRET_KEY`
- `STRIPE_UI_EMAIL`
- `STRIPE_UI_PASSWORD`
- `STRIPE_WEBHOOK_SECRET`

## Run Commands
```bash
mvn clean test
mvn -Ptest -Dgroups=api test
mvn -Ptest -Dgroups=webhook test
mvn -Ptest -Dbrowser=firefox -Dheadless=true test
mvn allure:serve
```

## Stripe CLI webhook testing
```bash
stripe login
stripe listen --forward-to localhost:9090/webhook
stripe trigger payment_intent.succeeded
stripe events resend <event_id>
```

## CI/CD
Pipeline at `.github/workflows/ci.yml` performs build, API tests, webhook tests, UI headless execution, and artifact uploads.
