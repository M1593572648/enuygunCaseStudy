package steps;
import core.drivers.DriverFactory;
import pages.ResultsPage;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
public class ResultPageSteps {
    WebDriver driver = DriverFactory.getDriver();
    ResultsPage resultsPage = new ResultsPage(driver);

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
        resultsPage.forceQuitDrivers();
    }
    @Then("all list displayed and verify the departure time between 10.00 and 18.00")
    public void isDisplayed(){
        resultsPage.extractList();
        resultsPage.verifyDepartureTimesBetween();
        resultsPage.verifyRouteIstanbulToAnkara();
        resultsPage.forceQuitDrivers();

    }


    @Then("is url changed")
    public void isUrlChanged() {
        resultsPage.checkUrl();
        resultsPage.forceQuitDrivers();
    }


    @Then("flight list is extracted to CSV")
    public void flightListIsExtractedToCSV() {
        resultsPage.extractList();

    }

    @And("CSV data is analyzed and graphs are generated")
    public void csvDataIsAnalyzedAndGraphsAreGenerated() {
        resultsPage.runPriceAnalysis();
        resultsPage.forceQuitDrivers();
    }
}
