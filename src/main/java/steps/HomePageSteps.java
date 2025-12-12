package steps;

import core.drivers.DriverFactory;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.ResultsPage;

public class HomePageSteps {

    WebDriver driver = DriverFactory.getDriver();
    HomePage homePage = new HomePage(driver);
    ResultsPage resultsPage = new ResultsPage(driver);

    @Given("user is on the home page")
    public void user_is_on_home_page() {
        homePage.navigateToBaseUrl();
    }
    @When("user accept to cookies")
    public void user_accept_to_cookies(){
        homePage.clickPopUp();
    }
    @And("user enters {string} to from field")
    public void user_enters_from(String from) {
        homePage.enterFrom(from);
    }

    @When("user enters {string} to destination field")
    public void user_enters_to(String to) {
        homePage.enterTo(to);
    }

    @When("user selects departure date {string}")
    public void user_selects_departure_date(String date) {
        homePage.enterFrom(date);
    }

    @When("user selects return date {string}")
    public void user_selects_return_date(String date) {
        homePage.enterTo(date);
    }

    @When("user clicks search button")
    public void user_clicks_search_button() {
        homePage.clickSearch();
    }
    @Then("flight results should be displayed")
    public void flight_results_should_be_displayed() {
        resultsPage.verifyResults();
    }
}
