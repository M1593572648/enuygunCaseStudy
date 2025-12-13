package core.helpers;

import core.managers.LoggerManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;

public class InteractionHelper {

    private final WebDriver driver;
    private final Actions actions;
    private final Logger log;

    public InteractionHelper(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.log = LoggerManager.getLogger(InteractionHelper.class);
    }

    public void clearAndType(WebElement element, String value, String keyName) {
        log.info("➡ '{}' elementine '{}' değeri yazılacak...", keyName, value);
        try {
            element.clear();
            element.sendKeys(value);
            log.info("✔ '{}' elementine '{}' değeri başarıyla yazıldı.", keyName, value);
        } catch (Exception e) {
            log.error("❌ '{}' elementine '{}' değeri yazılamadı! Hata: {}", keyName, value, e.getMessage());
            throw new RuntimeException("clearAndType failed for: " + keyName, e);
        }
    }

    public void type(WebElement element, String value, String keyName) {
        log.info("➡ '{}' elementine '{}' değeri yazılacak...", keyName, value);
        try {
            element.sendKeys(value);
            log.info("✔ '{}' elementine '{}' değeri başarıyla yazıldı.", keyName, value);
        } catch (Exception e) {
            log.error("❌ '{}' elementine '{}' değeri yazılamadı! Hata: {}", keyName, value, e.getMessage());
            throw new RuntimeException("clearAndType failed for: " + keyName, e);
        }
    }
    /**
     * Elemente Enter tuşuna basma işlemi
     */
    public void pressEnterKey(WebElement element, String keyName) {
        log.info("➡ '{}' elementine Enter tuşuna basılıyor...", keyName);
        try {
            element.sendKeys(Keys.ENTER);
            log.info("✔ '{}' elementine Enter tuşuna basıldı.", keyName);
        } catch (Exception e) {
            log.error("❌ '{}' elementine Enter tuşu basılamadı! Hata: {}", keyName, e.getMessage());
            throw new RuntimeException("pressEnterKey failed for: " + keyName, e);
        }
    }
    public void scrollTo(WebElement element, String keyName) {
        log.info("➡ '{}' elementine scroll ediliyor...", keyName);
        try {
            actions.moveToElement(element).perform();
            log.info("✔ '{}' elementine scroll başarılı.", keyName);
        } catch (Exception e) {
            log.error("❌ '{}' elementine scroll yapılamadı! Hata: {}", keyName, e.getMessage());
            throw new RuntimeException("scrollTo failed for: " + keyName, e);
        }
    }

    /**
     * Shadow DOM kontrolü
     */
    public boolean isElementInsideShadowDom(WebElement element) {
        try {
            String script = "return (arguments[0].shadowRoot != null);";
            Object result = ((JavascriptExecutor) driver).executeScript(script, element);
            boolean insideShadow = result instanceof Boolean && (Boolean) result;
            log.info("➡ Element Shadow DOM içinde mi?: {}", insideShadow);
            return insideShadow;
        } catch (Exception e) {
            log.warn("⚠ Shadow DOM kontrolü sırasında hata: {}", e.getMessage());
            return false;
        }
    }
    /**
     * Elementin görünür olmasını sağlar ve sayfada scroll eder.
     * Shadow DOM içindeyse içindeki gerçek elemente scroll yapılır.
     */
    public void scrollToElement(WebElement element, String keyName) {
        try {
            // Shadow DOM kontrolü
            if (isElementInsideShadowDom(element)) {
                log.info("➡ '{}' elementinin Shadow DOM içinde olduğu tespit edildi. Scroll uygulanıyor...", keyName);
                element = getElementFromShadowDom(element, "*"); // Shadow root içindeki tüm elementleri kapsayan selector
            } else {
                log.info("➡ '{}' elementine scroll uygulanıyor...", keyName);
            }

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            log.info("✔ '{}' elementine scroll başarılı.", keyName);

        } catch (Exception e) {
            log.error("❌ '{}' elementine scroll yapılamadı! Hata: {}", keyName, e.getMessage());
            throw new RuntimeException("scrollToElement failed for: " + keyName, e);
        }
    }
    /**
     * Range Slider (Min/Max kolları olan kaydırıcı) üzerinde klavye etkileşimi ile
     * hareket ettirme işlemi yapar.
     * Bu metod, rc-slider yapısındaki ARROW_LEFT/RIGHT tuşlarını kullanarak
     * kaydırıcı kolunu belirli bir değere getirir.
     *
     * @param minHandleElement Kaydırıcının sol (başlangıç) kolu (rc-slider-handle-1)
     * @param maxHandleElement Kaydırıcının sağ (bitiş) kolu (rc-slider-handle-2)
     * @param targetMinValue Hedef başlangıç değeri (dakika cinsinden, örn: 10:00 için 600)
     * @param targetMaxValue Hedef bitiş değeri (dakika cinsinden, örn: 18:00 için 1080)
     * @param handleName Loglar için kaydırıcı adı
     */
    public void slideRangeSlider(WebElement minHandleElement, WebElement maxHandleElement, int targetMinValue, int targetMaxValue, String handleName) {
        log.info("➡ '{}' kaydırıcısı, Min: {} (dk) ve Max: {} (dk) değerlerine ayarlanıyor...", handleName, targetMinValue, targetMaxValue);

        try {
            // 1. Mevcut Değerleri Alma (Başlangıç Değerini Bulma)
            int currentMinValue = getCurrentSliderValue(minHandleElement);
            int currentMaxValue = getCurrentSliderValue(maxHandleElement);

            log.info("ℹ️ Mevcut Min Değer: {} dk, Hedef Min Değer: {} dk", currentMinValue, targetMinValue);
            log.info("ℹ️ Mevcut Max Değer: {} dk, Hedef Max Değer: {} dk", currentMaxValue, targetMaxValue);

            // --- Sol Kolu (Min Handle) Ayarlama ---
            adjustSliderHandle(minHandleElement, currentMinValue, targetMinValue, "Sol Kol (Min)");

            // --- Sağ Kolu (Max Handle) Ayarlama ---
            adjustSliderHandle(maxHandleElement, currentMaxValue, targetMaxValue, "Sağ Kol (Max)");

            log.info("✔ '{}' kaydırıcısı başarıyla Min: {} ve Max: {} değerlerine ayarlandı.", handleName, targetMinValue, targetMaxValue);

        } catch (Exception e) {
            log.error("❌ '{}' kaydırıcısı ayarlanamadı! Hata: {}", handleName, e.getMessage());
            throw new RuntimeException("slideRangeSlider failed for: " + handleName, e);
        }
    }

    /**
     * Bir kaydırıcı kolunu hedef değere taşır.
     *
     * @param handleElement Kaydırıcı kolu (WebElement)
     * @param currentValue Mevcut değer (dakika)
     * @param targetValue Hedef değer (dakika)
     * @param handleLabel Log etiketi
     */
    private void adjustSliderHandle(WebElement handleElement, int currentValue, int targetValue, String handleLabel) {
        int difference = targetValue - currentValue;
        Keys keyToPress = difference > 0 ? Keys.ARROW_RIGHT : Keys.ARROW_LEFT;
        int steps = Math.abs(difference);

        if (steps > 0) {
            log.info("  -> {} {} adım hareket ettiriliyor (Hedef: {})", handleLabel, steps, targetValue);

            // Kolu tıklayarak odağı üzerine getirme
            handleElement.click();

            for (int i = 0; i < steps; i++) {
                handleElement.sendKeys(keyToPress);
            }
        } else {
            log.info("  -> {} zaten hedef değere ({}) ayarlı. Hareket ettirilmedi.", handleLabel, targetValue);
        }
    }

    /**
     * Bir kaydırıcı kolunun mevcut değerini (aria-valuenow) okur.
     */
    private int getCurrentSliderValue(WebElement handleElement) {
        String value = handleElement.getAttribute("aria-valuenow");
        if (value == null || value.isEmpty()) {
            return 0; // Varsayılan veya okunamıyorsa 0 döndür
        }
        return Integer.parseInt(value);
    }

    /**
     * Eğer element Shadow DOM içindeyse, gerçek DOM elementine ulaşır
     */
    public WebElement getElementFromShadowDom(WebElement shadowHost, String cssSelectorInsideShadow) {
        try {
            String script = "return arguments[0].shadowRoot.querySelector(arguments[1]);";
            WebElement element = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript(script, shadowHost, cssSelectorInsideShadow);
            log.info("✔ Shadow DOM içindeki element normal DOM üzerinden erişildi: {}", cssSelectorInsideShadow);
            return element;
        } catch (Exception e) {
            log.error("❌ Shadow DOM içindeki element erişilemedi: {}. Hata: {}", cssSelectorInsideShadow, e.getMessage());
            throw new RuntimeException("Shadow DOM element access failed: " + cssSelectorInsideShadow, e);
        }
    }
}
