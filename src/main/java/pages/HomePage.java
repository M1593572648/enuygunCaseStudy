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
        clickHelper.click(find("fromInput"), from);
        interactionHelper.clearAndType(find("fromInput"), from);
    }

    public void enterTo(String to) {
        clickHelper.click(find("toInput"), to);
        interactionHelper.clearAndType(find("toInput"), to);
    }

    public void clickSearch() {
        clickHelper.jsClick(find("searchButton"));
    }
}
