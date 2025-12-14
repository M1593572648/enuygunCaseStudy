package steps;

import core.drivers.DriverFactory;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import pages.HomePage;


public class HomePageSteps {

    WebDriver driver = DriverFactory.getDriver();
    HomePage homePage = new HomePage(driver);


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

}
