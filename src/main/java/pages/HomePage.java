package pages;

import core.BasePage;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver, "homePage.json");
    }

    public void clickPopUp() {
        clickHelper.click(find("acceptButton"), "acceptButton");
    }


    public void enterFrom(String from) {
        clickHelper.click(find("fromInput"), "fromInput");
        interactionHelper.type(find("fromInput"), from, "fromInput");
        interactionHelper.pressEnterKey(find("fromInput"), "fromInput");
    }


    public void enterToWhere(String where) {
        clickHelper.click(find("whereInput"), "whereInput");
        interactionHelper.type(find("whereInput"), where, "whereInput");
        waitHelper.waitFor1Sec();
        interactionHelper.pressEnterKey(find("whereInput"),"whereInput");
    }

    public void clickToDate(){
        clickHelper.click(find("dateInput"), "dateInput");
    }
    public void clickRoundTrip(){
        clickHelper.doubleClick(find("roundTrip"),"roundTrip");
    }
    public void clickSearch() {
        clickHelper.click(find("searchButton"), "searchButton");
    }
}
