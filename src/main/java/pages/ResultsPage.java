package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class ResultsPage extends BasePage {

    public ResultsPage(WebDriver driver) {
        super(driver, "resultsPage.json");
    }

    public boolean isResultsDisplayed() {
        try {
            return find("resultsContainer").isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasFlights() {
        return findAll("flightCard").size() > 0;
    }

    public void verifyResults() {

        boolean containerVisible = isResultsDisplayed();
        boolean hasFlightItems = hasFlights();

        Assert.assertTrue(containerVisible,
                "❌ Results container görünmüyor!");

        Assert.assertTrue(hasFlightItems,
                "❌ Uçuş sonuçları listelenmedi!");

        System.out.println("✔ Uçuş sonuçları başarıyla görüntülendi.");
    }
}
