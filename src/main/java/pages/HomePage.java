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
        clickHelper.click(find("departureDate"), "departureDate");
    }
    public void selectDate(String desiredDate) {
        String xpathTemplate = fileManager.getLocatorString("dateToSelect");
        String finalXpath = xpathTemplate.replace("TITLE_VALUE", desiredDate);
        clickHelper.clickByXpath(finalXpath, desiredDate + " tarihi");
    }
    public void clickReturnDate(){
        clickHelper.click(find("returnDateInput"),"returnDateInput");
    }
    public void clickSubmitButton(){
        clickHelper.click(find("searchFormTabGroup"),"searchFormTabGroup");
        clickHelper.click(find("searchSubmitButton"), "searchSubmitButton");
    }
    public void clickRoundTrip(){
        clickHelper.doubleClick(find("roundTrip"),"roundTrip");
    }

}
