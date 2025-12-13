package runners.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/features/api/petstore/pet_crud.feature",
        glue = "api.steps",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json"
        },
        monochrome = true,
        // Feature dosyasındaki en üst seviye etiketleri kullanın.
        // Bu, bu feature dosyasındaki tüm senaryoları bulacaktır.
        tags = "@crud"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}