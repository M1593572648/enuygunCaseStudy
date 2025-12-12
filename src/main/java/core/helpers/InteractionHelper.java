package core.helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class InteractionHelper {

    private final WebDriver driver;
    private final Actions actions;

    public InteractionHelper(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    public void hover(WebElement element) {
        actions.moveToElement(element).perform();
    }

    public void dragAndDrop(WebElement src, WebElement target) {
        actions.dragAndDrop(src, target).perform();
    }

    public void doubleClick(WebElement element) {
        actions.doubleClick(element).perform();
    }

    public void rightClick(WebElement element) {
        actions.contextClick(element).perform();
    }
    public void clearAndType(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public void scrollTo(WebElement element) {
        actions.moveToElement(element).perform();
    }
}
