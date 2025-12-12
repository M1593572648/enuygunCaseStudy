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
