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
    protected final FileManager fileManager;
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
        WebElement element;
        By locator = getLocator(key);

        try {
            // Önce direkt DOM elementini bekle
            element = waitHelper.waitForVisible(locator, key);

            // Shadow DOM kontrolü
            if (interactionHelper.isElementInsideShadowDom(element)) {
                log.info("'{}' elementinin Shadow DOM içinde olduğu tespit edildi.", key);
                // Shadow root içindeki gerçek elemente eriş
                element = interactionHelper.getElementFromShadowDom(element, "#child-element");
                // Shadow DOM içindeki elementin görünür olmasını bekle
                element = waitHelper.waitForVisibleInsideShadow(element, "#child-element", key);
            }

        } catch (Exception e) {
            log.error("❌ '{}' elementi bulunamadı! Hata: {}", key, e.getMessage());
            throw e;
        }

        return element;
    }

    /**
     * JSON key ile locator alır, WaitHelper ile DOM kontrolü yapar.
     * Element DOM'da var ise döndürür, yoksa retry ile bekler.
     *
     * @param key JSON key
     * @return WebElement
     */
    public WebElement findByDomMax(String key) {
        By locator = getLocator(key);
        int totalTimeoutS = 30;      // 30 saniye toplam bekleme
        int pollIntervalMs = 500;    // 500 ms aralıklarla retry

        return waitHelper.findWithIframeAndShadowJs(locator, key, totalTimeoutS);
    }
    // ---- JSON → WebElement, DOM kontrolü, 30 saniye bekler ----
    public WebElement findByDom(String key) {
        By locator = getLocator(key);
        try {
            // 1. DOM'da var mı kontrol et
            if (waitHelper.isPresent(locator, key)) {
                log.info("'{}' elementi DOM'da bulundu. Görünür olması bekleniyor...", key);
                // 2. Görünür olmasını bekle
                return waitHelper.waitForVisible(locator, key);
            } else {
                log.warn("'{}' elementi DOM'da bulunamadı!", key);
                return null;
            }
        } catch (Exception e) {
            log.error("❌ '{}' elementi DOM'da bulunurken hata oluştu! Hata: {}", key, e.getMessage());
            return null;
        }
    }


    // ---- JSON → List<WebElement> ----
    public List<WebElement> findAll(String key) {
        By locator = getLocator(key);
        waitHelper.waitForAllPresent(locator, key);
        List<WebElement> elements = waitHelper.waitForAllVisible(locator, key);

        for (int i = 0; i < elements.size(); i++) {
            WebElement e = elements.get(i);
            if (interactionHelper.isElementInsideShadowDom(e)) {
                log.info("'{}' elementinin Shadow DOM içinde olduğu tespit edildi...", key);
                // Örn: Shadow DOM içindeki elementin selector’ü #child-element
                elements.set(i, interactionHelper.getElementFromShadowDom(e, "#child-element"));
            }
        }

        return elements;
    }


}
