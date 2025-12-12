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

    @Given("kullanıcı ana sayfada")
    @Given("user is on the home page")
    public void user_is_on_home_page() {
        homePage.navigateToBaseUrl();
    }

    @When("kullanıcı çerezleri kabul eder")
    @When("user accepts cookies")
    public void user_accept_to_cookies() {
        homePage.clickPopUp();
    }
    @And("kullanıcı gidis donus secer")
    @And("user click round trip")
    public void user_click_round_trip(){
        homePage.clickRoundTrip();
    }
    @And("kullanıcı {string} şehrini kalkış alanına girer")
    @And("user enters {string} to from field")
    public void user_enters_from(String fromCity) {
        homePage.enterFrom(fromCity);
    }

    @And("kullanıcı {string} şehrini varış alanına girer")
    @And("user enters {string} to destination field")
    public void user_enters_to(String toCity) {
        homePage.enterToWhere(toCity);
    }
/*
    @And("kullanıcı kalkış tarihini {string} olarak seçer")
    @And("user selects departure date {string}")
    public void user_selects_departure_date(String departureDate) {
        homePage.selectDepartureDate(departureDate);
    }

    @And("kullanıcı dönüş tarihini {string} olarak seçer")
    @And("user selects return date {string}")
    public void user_selects_return_date(String returnDate) {
        homePage.selectReturnDate(returnDate);
    }

    @And("kullanıcı uçuş arama butonuna tıklar")
    @And("user clicks search button")
    public void user_clicks_search() {
        homePage.clickSearch();
    }

    @Then("uçuş listesinde tüm kalkış saatleri 10:00 - 18:00 aralığında olmalı")
    @Then("all departure times should be between 10:00 and 18:00")
    public void verify_departure_times() {
        resultsPage.verifyDepartureTimesBetween("10:00", "18:00");
    }

    @And("uçuş listesi doğru şekilde görüntülenmeli")
    @And("flight list should be displayed correctly")
    public void verify_flight_list_display() {
        resultsPage.verifyFlightListDisplayed();
    }

    @And("arama sonuçları seçilen güzergah ile eşleşmeli")
    @And("search results should match the selected route")
    public void verify_search_results_route() {
        resultsPage.verifySearchResultsRoute();
    }

 */
}
