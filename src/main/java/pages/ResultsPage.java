package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ResultsPage extends BasePage {

    public ResultsPage(WebDriver driver) {
        super(driver, "resultsPage.json","homePage.json");

    }
    public void forceQuitDrivers(){
        forceQuitDriver();
    }
    public void waitReloadPage() {
        String targetUrl = "https://www.enuygun.com/ucak-bileti/arama/istanbul-ankara-esenboga-havalimani-ista-esb/?gidis=01.01.2026&donus=10.01.2026&yetiskin=1&sinif=ekonomi&currency=TRY&save=1&ref=homepage&geotrip=domestic&trip=domestic";
        super.waitReloadPage(targetUrl, () -> new ChromeDriver());
        clickHelper.click(find("acceptButton"),"acceptButton");
    }
    public void checkUrl() {
        assertHelper.verifySearchParamsInUrl();
    }
    public void runPriceAnalysis(){
        analyticsHelper.runPriceAnalysis();
    }
    public void clickRoundTripTime() {
        interactionHelper.scrollToElement(find("baggageFilterContainer"), "baggageFilterContainer");
        clickHelper.click(find("departurePriceToggleIcon"), "departurePriceToggleIcon");
    }

    public void setFlightTimeRange(int minValue, int maxValue) {
        interactionHelper.slideRangeSlider(find("timeSliderMinHandle"), find("timeSliderMaxHandle"), minValue, maxValue, "Flight Time Slider");
    }
    public void userChooseTurkishAirlines(){
        clickHelper.click(find("airlineIcon"),"airlineIcon");
        clickHelper.click(find("thyAirlineName"),"thyAirlineName");
    }
    public void extractList(){
        dataExtractHelper.extractFlightListToCSV(find("containerDiv"), "flights.csv");
    }
    public void selectFlightButton(){
        clickHelper.click(find("selectFlightButton"),"selectFlightButton");
        clickHelper.click(find("selectAndProceedButton"),"selectAndProceedButton");
        clickHelper.click(find("returnListActionSelectButton"),"returnListActionSelectButton");
        clickHelper.click(find("ecoFlyPackageItem"),"superEkoPackageItem");

    }
    public void isAscendingPriceAndThy(){
        waitHelper.waitFor(1000);
        extractList();
        isListEmpty();
        verifyDepartureTimesBetween();
        verifyRouteIstanbulToAnkara();
        assertHelper.verifyOnlyTurkishAirlinesFlights("flights.csv");
        assertHelper.verifyPricesSortedAscending("flights.csv");
    }
    public void isListEmpty(){
        assertHelper.verifyCsvNotEmpty("flights.csv");
    }
    public void verifyDepartureTimesBetween(){
        assertHelper.verifyDepartureTimesBetween("flights.csv", "10:00", "18:00");
    }
    public void verifyRouteIstanbulToAnkara(){
        assertHelper.verifyRouteIstanbulToAnkara("flights.csv");
    }
}
