# Quickstart

## Prerequisites
- Java 17+
- Maven 3.9+

## Install
```bash
git clone <repository-url>
cd GitHub-Automation
```

## Run checks
```bash
mvn -q -DskipTests compile
mvn test
```

## Run focused suites
```bash
mvn test -Dtest=APITest
mvn test -Dtest=SearchTest
```

## Main docs
- `README.md`
- `docs/PROJECT_GUIDE.md`
