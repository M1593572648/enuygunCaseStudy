package core;

import config.ConfigManager;
import core.helpers.*;
import core.managers.FileManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class BasePage {

    protected final WebDriver driver;
    private final FileManager fileManager;

    // ---- Helpers ----
    public ClickHelper clickHelper;
    public InteractionHelper interactionHelper;
    public AssertHelper assertHelper;
    public ScreenshotHelper screenshotHelper;

    public BasePage(WebDriver driver, String jsonFileName) {
        this.driver = driver;
        this.fileManager = new FileManager(jsonFileName);
        this.clickHelper = new ClickHelper(driver);
        this.interactionHelper = new InteractionHelper(driver);
        this.assertHelper = new AssertHelper();
        this.screenshotHelper = new ScreenshotHelper(driver);
        PageFactory.initElements(driver, this);
    }

    // ---- Navigate ----
    public void navigateToBaseUrl() {
        String baseUrl = ConfigManager.get("base.url");
        driver.get(baseUrl);
    }

    // ---- JSON → Locator ----
    private By getLocator(String key) {
        return fileManager.getLocator(key);
    }

    // ---- JSON → WebElement ----
    public WebElement find(String key) {
        return driver.findElement(getLocator(key));
    }

    // ---- JSON → List<WebElement> ----
    public List<WebElement> findAll(String key) {
        return driver.findElements(getLocator(key));
    }
}
