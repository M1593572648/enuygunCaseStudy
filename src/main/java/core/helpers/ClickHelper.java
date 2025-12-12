package core.helpers;

import config.ConfigManager;
import core.managers.LoggerManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ClickHelper {

    private final WebDriver driver;
    private final org.slf4j.Logger log;
    private final int maxWait;

    public ClickHelper(WebDriver driver) {
        this.driver = driver;
        this.log = LoggerManager.getLogger(ClickHelper.class);
        this.maxWait = ConfigManager.getInt("explicit.wait"); // ör: 30 saniye
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
                    log.warn("⚠ '{}' elementi bulunamadı. {} saniye bekleniyor...",
                            keyName, 1);

                    Thread.sleep(1000);
                    elapsed++;

                } catch (InterruptedException ignored) {}
            }
        }

        log.error("❌ '{}' elementi {} saniyede bulunamadı! Tıklama başarısız.",
                keyName, maxWait);

        throw new RuntimeException("Element not clickable: " + keyName);
    }

    // Gecikmeli tıklama
    public void delayedClick(WebElement element, long millis) {
        sleep(millis);
        element.click();
    }

    // JS Click (engel aşma)
    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Elementi highlight edip tıkla
    public void highlightAndClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.border='3px solid red'", element);
        sleep(200);
        element.click();
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {}
    }
}
