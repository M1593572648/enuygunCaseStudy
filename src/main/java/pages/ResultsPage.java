package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;


public class ResultsPage extends BasePage {

    public ResultsPage(WebDriver driver) {
        super(driver, "resultsPage.json");
    }

    public void clickRoundTripTime(){

        interactionHelper.scrollToElement(findByDomMax("departureReturnTimeFilterHeader"),"departureReturnTimeFilterHeader");
        clickHelper.click(find("departureReturnTimeFilterHeader"),"departureReturnTimeFilterHeader");
    }

    public void setFlightTimeRange(int minValue, int maxValue){
        interactionHelper.slideRangeSlider(find("departureTimeMinHandle"), find("departureTimeMaxHandle"), minValue, maxValue, "Flight Time Slider");
    }
}
