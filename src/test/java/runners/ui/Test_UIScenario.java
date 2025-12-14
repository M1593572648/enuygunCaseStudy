package runners.ui;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/features/ui/",
        glue = {"steps"},
        plugin = {
                "pretty",
                "html:target/ui-cucumber-reports.html",
                "json:target/ui-cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        tags = "@Regression"
)
public class Test_UIScenario extends AbstractTestNGCucumberTests {
}
