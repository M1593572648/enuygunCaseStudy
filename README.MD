# 🧪 enuygunCaseStudy

- **Project Overview**

This repository contains **UI automation tests** for the website **www.enuygun.com**, along with **Python scripts used for test data analysis and result evaluation**.

In addition, this repository includes **CRUD-level API tests** for the **Swagger Petstore API**, implemented using **Cucumber + TestNG + RestAssured**.

The test architecture is built using:
- **Feature files (Gherkin)**
- **Step Definitions**
- **TestNG Runner**
- **Allure Report integration**

The project follows a **Cucumber + TestNG + Page Object Model (POM) architecture**, ensuring a **clean, modular, and maintainable test automation structure**.

# 📸 Logging & Screenshot Support

- **The framework also includes a step-level logging and screenshot mechanism:**
- **Automatic screenshots are captured at critical steps and on failures**
- **Detailed logs are generated during step execution**
- **Screenshots and logs are attached to Allure reports, enabling easier debugging and root cause analysis**
- **This structure improves test traceability, observability, and failure analysis**
---
### Included Libraries
- **pandas** – Data manipulation and analysis
- **matplotlib** – Data visualization
- **seaborn** – Advanced statistical plots

---

### ⚡ Automatic Installation (One Command)

To automatically install all required Python libraries, run the following command:

```bash
pip install -r analytics/requirements.txt
```

---

## 🛠️ Technologies Used

| Technology | Purpose | Version |
|-----------|--------|---------|
| Java | Core automation framework | 17 |
| Maven | Build and dependency management | 3.x |
| Selenium WebDriver | UI automation | 4.21.0 |
| WebDriverManager | Browser driver management | 5.8.0 |
| Cucumber | BDD-based test automation | 7.14.0 |
| TestNG | Test execution framework | 7.10.2 |
| RestAssured | API testing library | 5.4.0 |
| JSON Schema Validator | API response schema validation | 5.4.0 |
| Allure | Advanced test reporting | 2.26.0 |
| Jackson Databind | JSON serialization/deserialization | 2.17.1 |
| SLF4J | Logging facade | 2.0.9 |
| Log4j2 | Logging implementation | 2.23.1 |
| Python | Test data analysis and result evaluation scripts | 3.x |
| Pandas | Data manipulation and analysis | latest (via requirements.txt) |
| Matplotlib | Data visualization | latest (via requirements.txt) |
| Seaborn | Statistical data visualization | latest (via requirements.txt) |
| Page Object Model (POM) | UI test design pattern for maintainability | Design Pattern |


## 🧬 API Test Features

✔ **Petstore Pet CRUD** API tests (Create, Read, Update, Delete)  
✔ Both **positive** and **negative** test scenarios  
✔ **BDD approach** using Gherkin feature files  
✔ **Allure report integration** for detailed test execution results  
✔ Easy execution and integration using **Maven**

## 🖥️ UI Automation Features

✔ End-to-end **flight search automation** for www.enuygun.com  
✔ **Round-trip flight search** scenarios with dynamic city and date inputs  
✔ **Time range filtering** for departure and return flights  
✔ **Airline-based filtering** (Turkish Airlines selection)  
✔ **Price sorting validation** (ascending order verification)  
✔ **Critical path testing** including flight selection and URL change validation  
✔ **Regression test coverage** using Cucumber tags (`@Regression`, `@Case1`, `@Case2`, etc.)  
✔ **CSV extraction** of flight search results  
✔ **Data analysis and visualization** using Python scripts (graphs and categorization)  
✔ BDD-based UI automation using **Cucumber + TestNG + Page Object Model (POM)**  
✔ Integrated execution and reporting with **Allure**

---

## 📌 How to Run

First, clone the repository:

```bash
git clone https://github.com/M1593572648/enuygunCaseStudy.git
cd enuygunCaseStudy
```
🔹 Running Tests via Feature Files (Scenario-Based)

All test scenarios can be executed directly through the feature files located under the features directory.
```- API Test Runner feature
src/features/api/petstore/pet_crud.feature 
```
``` UI Test Runner feature
src/features/ui/petstore/basicHomePageTest.feature 
```
This allows running tests scenario by scenario or by Cucumber tags.
Feature files include: UI automation scenarios and API test scenarios
Regression and case-based executions

🔹 Running Tests via Runner Classes
Tests can also be executed using the TestNG runner classes, which provide more controlled and structured execution.
Runner classes are located at:

```- API Test Runners
src/test/java/runners/api/ApiTestRunner.java
```
```- UI Test Runners
src/test/java/runners/ui/Test_UIScenario.java
```
Each runner is configured to:
Execute specific feature sets
Filter scenarios using Cucumber tags (e.g. @Regression)
Integrate with Allure reporting
This approach is recommended for CI/CD pipelines and regression executions.