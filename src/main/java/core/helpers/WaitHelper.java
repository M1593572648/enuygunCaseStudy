package core.helpers;

import config.ConfigManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class WaitHelper {

    private static final Logger log = LoggerFactory.getLogger(WaitHelper.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int maxWait;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.maxWait = ConfigManager.getInt("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(maxWait));
    }
    public WebElement waitForVisibleInsideShadow(WebElement shadowHost, String cssSelector, String keyName) {
        WebElement element = null;
        try {
            element = new WebDriverWait(driver, Duration.ofSeconds(maxWait))
                    .until(driver -> shadowHost.getShadowRoot().findElement(By.cssSelector(cssSelector)));
            log.info("✔ '{}' Shadow DOM içindeki element görünür durumda.", keyName);
        } catch (TimeoutException e) {
            log.error("❌ '{}' Shadow DOM içindeki element {} saniye içinde görünür olmadı!", keyName, maxWait);
            throw e;
        }
        return element;
    }

    public WebElement waitForVisible(By locator, String keyName) {
        log.info("➡ '{}' elementinin görünür olması bekleniyor...", keyName);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            log.info("✔ '{}' elementi görünür durumda.", keyName);
            return element;
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementi {} saniye içinde görünür olmadı!", keyName, maxWait);
            throw e;
        }
    }
    public void waitForVisibleLog(WebElement element, String keyName) {
        log.info("➡ '{}' elementinin görünür olması bekleniyor...", keyName);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            log.info("✔ '{}' elementi artık görünür durumda.", keyName);
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementi {} saniye içinde görünür olmadı!", keyName, maxWait);
            throw e;
        }
    }


    public WebElement waitForClickable(By locator, String keyName) {
        log.info("➡ '{}' elementinin tıklanabilir olması bekleniyor...", keyName);
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            log.info("✔ '{}' elementi tıklanabilir durumda.", keyName);
            return element;
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementi {} saniye içinde tıklanabilir olmadı!", keyName, maxWait);
            throw e;
        }
    }

    public List<WebElement> waitForAllVisible(By locator, String keyName) {
        log.info("➡ '{}' elementlerinin görünür olması bekleniyor...", keyName);
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            log.info("✔ '{}' elementleri görünür durumda. Toplam: {}", keyName, elements.size());
            return elements;
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementleri {} saniye içinde görünür olmadı!", keyName, maxWait);
            throw e;
        }
    }

    public List<WebElement> waitForAllPresent(By locator, String keyName) {
        log.info("➡ '{}' elementlerinin DOM'da bulunması bekleniyor...", keyName);
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            log.info("✔ '{}' elementleri DOM'da mevcut. Toplam: {}", keyName, elements.size());
            return elements;
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementleri {} saniye içinde DOM'da bulunamadı!", keyName, maxWait);
            throw e;
        }
    }

    public void waitForInvisible(By locator, String keyName) {
        log.info("➡ '{}' elementinin görünmez olması bekleniyor...", keyName);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            log.info("✔ '{}' elementi artık görünmez durumda.", keyName);
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementi {} saniye içinde görünmez olmadı!", keyName, maxWait);
            throw e;
        }
    }

    public WebElement waitForPresence(By locator, String keyName) {
        log.info("➡ '{}' elementinin DOM'da varlığı bekleniyor...", keyName);
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            log.info("✔ '{}' elementi DOM'da mevcut.", keyName);
            return element;
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementi {} saniye içinde DOM'da bulunamadı!", keyName, maxWait);
            throw e;
        }
    }

    public boolean waitUntilTextExists(By locator, String text, String keyName) {
        log.info("➡ '{}' elementinde '{}' metninin görünmesi bekleniyor...", keyName, text);
        try {
            boolean result = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            log.info("✔ '{}' elementinde '{}' metni mevcut.", keyName, text);
            return result;
        } catch (TimeoutException e) {
            log.error("❌ '{}' elementinde '{}' metni {} saniye içinde görünmedi!", keyName, text, maxWait);
            throw e;
        }
    }
    /**
     * Basit 200ms bekleme fonksiyonu
     */
    public void waitFor1Sec() {
        log.info("⏱ 200ms bekleniyor...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn("⚠ Bekleme sırasında hata oluştu: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
        log.info("✔ 200ms bekleme tamamlandı.");
    }


    public boolean waitUntilUrlContains(String text) {
        log.info("➡ URL'nin '{}' içermesi bekleniyor...", text);
        try {
            boolean result = wait.until(ExpectedConditions.urlContains(text));
            log.info("✔ URL '{}' metnini içeriyor.", text);
            return result;
        } catch (TimeoutException e) {
            log.error("❌ URL '{}' metnini {} saniye içinde içermedi!", text, maxWait);
            throw e;
        }
    }
}
