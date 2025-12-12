package core.helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import java.io.File;

public class ScreenshotHelper {

    private final WebDriver driver;

    public ScreenshotHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("screenshots/" + name + ".png");
            FileHandler.copy(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
