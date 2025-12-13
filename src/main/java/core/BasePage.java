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

import static org.testng.Assert.assertNotEquals;

public class BasePage {

    protected WebDriver driver;
    protected final FileManager fileManager;
    private static final Logger log = LoggerFactory.getLogger(BasePage.class);
    // ---- Helpers ----
    public ClickHelper clickHelper;
    public InteractionHelper interactionHelper;
    public AssertHelper assertHelper;
    public ScreenshotHelper screenshotHelper;
    public WaitHelper waitHelper;
    public DataExtractHelper dataExtractHelper;

    /**
     * BasePage constructor, çoklu JSON desteği ve tüm helper’ları başlatır.
     *
     * @param driver    WebDriver instance
     * @param jsonFiles JSON dosyaları (bir veya birden fazla)
     */
    public BasePage(WebDriver driver, String... jsonFiles) {
        this.driver = driver;

        // ---- Helpers ----
        this.waitHelper = new WaitHelper(driver);
        this.clickHelper = new ClickHelper(driver);
        this.interactionHelper = new InteractionHelper(driver);
        this.assertHelper = new AssertHelper(driver);
        this.screenshotHelper = new ScreenshotHelper(driver);
        this.dataExtractHelper = new DataExtractHelper(driver);

        // ---- JSON FileManager ----
        if (jsonFiles != null && jsonFiles.length > 0) {
            this.fileManager = new FileManager(jsonFiles[0]); // Varsayılan ilk JSON
        } else {
            this.fileManager = null;
        }

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

    public void isChangedUrl(String oldUrl) {
        String currentUrl = driver.getCurrentUrl();

        assertNotEquals(
                currentUrl,
                oldUrl,
                "❌ URL değişmedi"
        );

        log.info("✅ URL değişti: {}", currentUrl);
    }

    // --- URL Yönlendirmesinden sonra restart driver ----
    public void restartDriverWithNewUrl(String targetUrl, java.util.function.Supplier<WebDriver> driverSupplier) {
        try {
            log.info("⚠ URL değişti. Mevcut tarayıcı kapanacak ve yeni URL ile devam edilecek: {}", targetUrl);
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            log.warn("⚠ Mevcut driver kapatılamadı: {}", e.getMessage());
        }

        // Yeni driver oluştur
        WebDriver newDriver = driverSupplier.get();

        // BasePage içindeki driver ve helper'ları güncelle
        this.driver = newDriver;
        this.clickHelper = new ClickHelper(newDriver);
        this.interactionHelper = new InteractionHelper(newDriver);
        this.waitHelper = new WaitHelper(newDriver);
        this.assertHelper = new AssertHelper(newDriver);
        this.screenshotHelper = new ScreenshotHelper(newDriver);
        this.dataExtractHelper = new DataExtractHelper(newDriver);

        // Yeni URL’ye git
        newDriver.get(targetUrl);
        log.info("✔ Yeni tarayıcı başlatıldı ve '{}' URL'sine gidildi.", targetUrl);
    }

    /**
     * URL değiştiyse yeni driver başlatır ve BasePage’i günceller.
     */
    public void waitReloadPage(String expectedUrl, java.util.function.Supplier<WebDriver> driverSupplier) {
        boolean changed = waitHelper.waitForUrlChange(expectedUrl, 10, 1000);
        if (changed) {
            log.info("⚠ URL değişti, yeni driver ile devam edilecek...");
            restartDriverWithNewUrl(expectedUrl, driverSupplier);
        }
        waitHelper.waitForPageLoad(30);
    }

    // ---- JSON → WebElement ----
    public WebElement find(String key) {
        WebElement element;
        By locator = getLocator(key);

        try {
            // Önce direkt DOM elementini bekle
            element = waitHelper.waitForVisible(locator, key);
            // ✅ Başarılı da olsa screenshot al
            String screenshotName = "element_found_" + key + "_" + System.currentTimeMillis();
            screenshotHelper.takeScreenshot(screenshotName);
            log.info("📸 '{}' elementi bulundu, screenshot alındı: {}", key, screenshotName);
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

            // Hata oluşursa ekran görüntüsü al
            try {
                ScreenshotHelper screenshotHelper = new ScreenshotHelper(driver);
                String screenshotName = "element_not_found_" + key + "_" + System.currentTimeMillis();
                screenshotHelper.takeScreenshot(screenshotName);
                log.info("📸 '{}' element bulunamadığında screenshot alındı: {}", key, screenshotName);
            } catch (Exception ex) {
                log.warn("⚠ Screenshot alınamadı: {}", ex.getMessage());
            }

            throw e;
        }

        return element;
    }
}