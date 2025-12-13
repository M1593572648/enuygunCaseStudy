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

    @And("kullanıcı gidiş tarihi alanına tıklar")
    @And("user clicks on the departure date field")
    public void user_click_departure_input(){
        homePage.clickToDate();
    }
    @And("kullanıcı gidiş tarihi {string} seçer")
    @And("user selects the departure date {string}")
    public void user_choose_departure_date(String desiredDate){
        homePage.selectDate(desiredDate);
    }
    @And("kullanıcı dönüş tarihi alanina tiklar")
    @And("user clicks the return date field")
    public void user_click_return_date_input(){
        homePage.clickReturnDate();
    }
    @And("kullanıcı dönüş tarihi {string} seçer")
    @And("user selects the return date {string}")
    public void user_choose_return_date(String desiredDate){
        homePage.selectDate(desiredDate);
    }
    @And("kullanıcı ucuz uçak bileti ara butonuna tıklar")
    @And("user clicks search cheap flight button")
    public void user_clicks_search_cheap_flight_button() {
        homePage.clickSubmitButton();
    }
    @And("kullanici gidiş dönüş tarihleri alanına tıklar")
    @And("user clicks on the departure and return dates field")
    public void departureReturnTimeFilterHeader() {
        resultsPage.clickRoundTripTime();
    }
    @And("kullanici saat aralığının başlangıcını {int} ve bitişini {int} seçer ")
    @And("user selects flight time range with start {int} and end {int}")
    public void setFlightTimeRange(int minValue, int maxValue){
        resultsPage.setFlightTimeRange(minValue,maxValue);
    }

    @And("user wait reloading page")
    public void userWaitReloadingPage() {
        resultsPage.waitReloadPage();
    }
    @And("user choose only turkish airlines")
    public void userChooseOnlyTurkishAirlines() {
        resultsPage.userChooseTurkishAirlines();
    }

    @And("user select first button in the div")
    public void selectFirstBtn(){
        resultsPage.selectFlightButton();
    }
    @Then("is ascending price and selected thy")
    public void ascendingPrice() {
        resultsPage.isAscendingPriceAndThy();
    }
    @Then("all list displayed and verify the departure time between 10.00 and 18.00")
    public void isDisplayed(){
        resultsPage.extractList();
        resultsPage.verifyDepartureTimesBetween();
        resultsPage.verifyRouteIstanbulToAnkara();
    }


    @Then("is url changed")
    public void isUrlChanged() {
        resultsPage.checkUrl();
    }


    @Then("flight list is extracted to CSV")
    public void flightListIsExtractedToCSV() {
        resultsPage.extractList();

    }

    @And("CSV data is analyzed and graphs are generated")
    public void csvDataIsAnalyzedAndGraphsAreGenerated() {
        resultsPage.runPriceAnalysis();
    }
}
