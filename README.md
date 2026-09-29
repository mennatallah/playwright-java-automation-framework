# Playwright Java Automation Framework

[![Automated Tests](https://github.com/mennatallah/playwright-java-automation-framework/actions/workflows/tests.yml/badge.svg)](https://github.com/mennatallah/playwright-java-automation-framework/actions/workflows/tests.yml)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Playwright](https://img.shields.io/badge/Playwright-Java-2EAD33?logo=playwright&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)

A maintainable **UI + API test automation framework** built with **Playwright, Java 17, JUnit 5 and Maven**, running automatically on **GitHub Actions**.

It is designed around one goal: **tests that stay green when the application changes**, and that tell you exactly what went wrong when they don't.

| Layer | Application under test |
|---|---|
| UI | [Sauce Demo](https://www.saucedemo.com), a demo e-commerce web app |
| API | [JSONPlaceholder](https://jsonplaceholder.typicode.com), a demo REST API |

---

## Key design decisions

**1. Stable selectors**
Every element is located by its `data-test` attribute (`page.getByTestId(...)`), never by text, CSS layout classes or XPath positions. Label or layout changes don't break the tests.

**2. Page Object Model**
Tests describe business flows (`login → add to cart → checkout`). All UI details live in page classes, so a UI change is fixed in one place.

**3. Configuration-driven forms**
The checkout form is described as data in [`checkout-information.json`](src/test/resources/testdata/forms/checkout-information.json): fields, types, values and validation rules.
- A generic [`FormFiller`](src/main/java/io/github/mennatallah/framework/forms/FormFiller.java) fills *any* form from its definition, with one handler per field type.
- Validation tests are **generated from the definition**: one test per required field. Add a required field to the JSON and it gets test coverage automatically, with no code changes.

**4. API layer**
A reusable [`ApiClient`](src/main/java/io/github/mennatallah/framework/api/ApiClient.java) built on Playwright's `APIRequestContext`, used for API tests and ready for fast test-data setup in UI tests.

**5. Test isolation**
One browser per test class and a fresh browser context per test (clean cookies and storage), so tests never depend on each other.

**6. Failure diagnostics**
Every UI test is traced. When a test fails, a **full-page screenshot** and a **Playwright trace** (DOM snapshots, network, console, step-by-step timeline) are saved and uploaded from CI. Failures can be diagnosed without re-running anything.

**7. CI/CD**
Tests run on every push and pull request, weekly on a schedule, and on demand. Results are published as a test report in the GitHub Actions summary.

---

## Test coverage

| Suite | What it covers |
|---|---|
| `LoginTest` | Valid login, locked-out user, data-driven invalid credentials |
| `InventoryTest` | Sorting by price (both directions), cart badge updates |
| `CheckoutTest` | Full end-to-end purchase, config-driven required-field validation |
| `PostsApiTest` | GET (single, list, filter), POST, PUT, DELETE, 404 handling, response contract checks |

---

## Project structure

```
src
├── main/java/io/github/mennatallah/framework
│   ├── api/        ApiClient: REST calls + JSON helpers
│   ├── config/     Config: properties with command-line overrides
│   ├── core/       BrowserFactory: chromium / firefox / webkit
│   ├── forms/      FormDefinition + FormFiller: config-driven forms
│   └── pages/      Page Objects
└── test
    ├── java/io/github/mennatallah/tests
    │   ├── base/   BaseUiTest, BaseApiTest, failure screenshots & traces
    │   ├── ui/     UI test suites
    │   └── api/    API test suites
    └── resources
        ├── config.properties
        └── testdata/forms/   form definitions (JSON)
```

---

## Running the tests

**Requirements:** Java 17+ and Maven 3.9+

```bash
# 1. Install the browser (first time only)
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"

# 2. Run everything
mvn test
```

**Useful options**

```bash
mvn test -Dgroups=api                      # API tests only
mvn test -Dgroups=ui                       # UI tests only
mvn test -Dtest=CheckoutTest               # a single test class
mvn test -Dheadless=false                  # watch the browser
mvn test -Dbrowser=firefox                 # another browser (install it first)
```

**Open the trace of a failed test**

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace target/artifacts/traces/<test-name>.zip"
```

---

## Tech stack

Java 17 · Playwright for Java · JUnit 5 · Maven · Jackson · GitHub Actions

---

## Author

**Menna**, Senior QA Automation Engineer with 10 years of experience in manual and automated testing.

[![Hire me on Upwork](https://img.shields.io/badge/Hire_me_on-Upwork-6FDA44?style=for-the-badge&logo=upwork&logoColor=white)](https://www.upwork.com/freelancers/~014f75c81689319474)
