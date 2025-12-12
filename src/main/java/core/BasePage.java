package core;

import config.ConfigManager;
import core.helpers.*;
import core.helpers.WaitHelper;
import core.managers.FileManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BasePage {

    protected final WebDriver driver;
    private final FileManager fileManager;
    private static final Logger log = LoggerFactory.getLogger(BasePage.class);
    // ---- Helpers ----
    public ClickHelper clickHelper;
    public InteractionHelper interactionHelper;
    public AssertHelper assertHelper;
    public ScreenshotHelper screenshotHelper;
    public WaitHelper waitHelper;

    public BasePage(WebDriver driver, String jsonFileName) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
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
        WebElement element = waitHelper.waitForVisible(getLocator(key), key);

        // Shadow DOM kontrolü
        if (interactionHelper.isElementInsideShadowDom(element)) {
            log.info("'{}' elementinin Shadow DOM içinde olduğu tespit edildi.", key);
            // Shadow DOM içindeki elemente eriş, ör: input içinde #child-element
            element = interactionHelper.getElementFromShadowDom(element, "#child-element");
        }

        return element;
    }

    // ---- JSON → List<WebElement> ----
    public List<WebElement> findAll(String key) {
        By locator = getLocator(key);
        waitHelper.waitForAllPresent(locator, key);
        List<WebElement> elements = waitHelper.waitForAllVisible(locator, key);

        for (int i = 0; i < elements.size(); i++) {
            WebElement e = elements.get(i);
            if (interactionHelper.isElementInsideShadowDom(e)) {
                log.info("'{}' elementinin Shadow DOM içinde olduğu tespit edildi.", key);
                // Örn: Shadow DOM içindeki elementin selector’ü #child-element
                elements.set(i, interactionHelper.getElementFromShadowDom(e, "#child-element"));
            }
        }

        return elements;
    }


}
