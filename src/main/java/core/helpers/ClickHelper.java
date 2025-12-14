package core.helpers;

import config.ConfigManager;
import core.managers.LoggerManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;

public class ClickHelper {

    private final WebDriver driver;
    private final Logger log;
    private final int maxWait;
    private final Actions actions;

    public ClickHelper(WebDriver driver) {
        this.driver = driver;
        this.log = LoggerManager.getLogger(ClickHelper.class);
        this.maxWait = ConfigManager.getInt("explicit.wait");
        this.actions = new Actions(driver);
    }

    public void click(WebElement element, String keyName) {
        int elapsed = 0;
        log.info("➡ '{}' elementine tıklanmaya çalışılıyor...", keyName);

        while (elapsed < maxWait) {
            try {
                element.click();
                log.info("✔ '{}' elementi bulundu. Tıklama başarılı.", keyName);
                return;
            } catch (Exception e) {
                try {
                    log.warn("⚠ '{}' elementi bulunamadı. {} saniye bekleniyor...", keyName, 1);
                    Thread.sleep(1000);
                    elapsed++;
                } catch (InterruptedException ignored) {}
            }
        }

        log.error("❌ '{}' elementi {} saniyede bulunamadı! Tıklama başarısız.", keyName, maxWait);
        throw new RuntimeException("Element not clickable: " + keyName);
    }

    /**
     * Elementi kontrol ederek güvenli Javascript Click işlemi yapar.
     * @param element Tıklanacak WebElement
     * @param keyName Loglarda görünecek element adı
     */
    public void jsClick(WebElement element, String keyName) {
        // Elementin teknik tanımını (toString) alalım, null ise belirtelim
        String elementDescription = (element != null) ? element.toString() : "Tanımsız (NULL)";

        log.info("➡ '{}' elementine JS Click işlemi deneniyor... (Detay: {})", keyName, elementDescription);

        if (element != null) {
            try {
                // JavascriptExecutor'a cast edip tıklama işlemini yapıyoruz
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", element);

                log.info("✔ '{}' elementine JS Click başarıyla tamamlandı.", keyName);
            } catch (Exception e) {
                // JS hatası veya StaleElementReference gibi durumlarda burası çalışır
                log.error("❌ '{}' elementine JS Click sırasında hata oluştu! Hata: {}", keyName, e.getMessage());
            }
        } else {
            // Element null ise işlem yapma ve uyarı ver
            log.warn("⚠ '{}' elementi NULL olduğu için JS Click işlemi İPTAL EDİLDİ!", keyName);
        }
    }

    public void doubleClick(WebElement element, String keyName) {
        log.info("➡ '{}' elementine çift tıklama yapılıyor...", keyName);
        try {
            actions.doubleClick(element).perform();
            log.info("✔ '{}' elementine çift tıklama başarılı.", keyName);
        } catch (Exception e) {
            log.error("❌ '{}' elementine çift tıklama yapılamadı! Hata: {}", keyName, e.getMessage());
            throw new RuntimeException("doubleClick failed for: " + keyName, e);
        }
    }
    public void clickByXpath(String xpath, String keyName) {
        log.info("➡ '{}' elementine XPath üzerinden tıklanıyor: {}", keyName, xpath);
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            element.click();
            log.info("✔ '{}' elementi tıklandı.", keyName);
        } catch (Exception e) {
            log.error("❌ '{}' elementine tıklanamadı! Hata: {}", keyName, e.getMessage());
            throw new RuntimeException("clickByXpath failed for: " + keyName, e);
        }
    }

    public void rightClick(WebElement element, String keyName) {
        log.info("➡ '{}' elementine sağ tıklama yapılıyor...", keyName);
        try {
            actions.contextClick(element).perform();
            log.info("✔ '{}' elementine sağ tıklama başarılı.", keyName);
        } catch (Exception e) {
            log.error("❌ '{}' elementine sağ tıklama yapılamadı! Hata: {}", keyName, e.getMessage());
            throw new RuntimeException("rightClick failed for: " + keyName, e);
        }
    }
    /**
     * Container içindeki elementlerin ilkini (index 0) tıklar
     * @param container Div veya container element
     * @param keyName Loglarda kullanılacak element adı
     */
    public void clickFirstChild(WebElement container, String keyName) {
        int elapsed = 0;
        int maxAttempts = maxWait; // maxWait saniye kadar deneme

        log.info("➡ '{}' container içindeki 1. element tıklanmaya çalışılıyor...", keyName);

        while (elapsed < maxAttempts) {
            try {
                WebElement firstChild;

                // Shadow DOM kontrolü ve erişimi JS ile doğrudan
                JavascriptExecutor js = (JavascriptExecutor) driver;
                Boolean hasShadowRoot = (Boolean) js.executeScript("return arguments[0].shadowRoot != null;", container);

                if (hasShadowRoot) {
                    log.info("⚡ '{}' container Shadow DOM içinde, child elemente erişiliyor...", keyName);
                    firstChild = (WebElement) js.executeScript(
                            "return arguments[0].shadowRoot.querySelector(':scope > *:first-child');",
                            container
                    );
                } else {
                    firstChild = container.findElements(By.xpath("./*")).get(0);
                }

                firstChild.click();
                log.info("✔ '{}' container içindeki 1. element başarıyla tıklandı.", keyName);
                return;

            } catch (IndexOutOfBoundsException e) {
                log.error("❌ '{}' container içinde hiç element bulunamadı!", keyName);
                throw new RuntimeException("No child elements found in container: " + keyName, e);
            } catch (Exception e) {
                elapsed++;
                log.warn("⚠ '{}' container içindeki element tıklanamadı. {}. deneme, 1 saniye bekleniyor...",
                        keyName, elapsed);
                sleep(1000);
            }
        }

        log.error("❌ '{}' container içindeki 1. element {} saniye içinde tıklanamadı!", keyName, maxAttempts);
        throw new RuntimeException("clickFirstChild failed for: " + keyName);
    }



    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {}
    }
}
