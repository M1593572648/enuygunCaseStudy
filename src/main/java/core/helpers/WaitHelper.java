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
    /**
     * Element görünür mü değilse shadow element içerisine bak ve görünür olasıya kadar bekle
     * */
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
    /**
     * Elementi görünür olasıya kadar bekle
     * */
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
    /**
     * Basit 1s bekleme fonksiyonu
     */
    public void waitFor1Sec() {
        log.info("⏱ 1 saniye bekleniyor...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn("⚠ Bekleme sırasında hata oluştu: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
        log.info("✔ 1 saniye bekleme tamamlandı.");
    }
    /**
     * Sayfanın URL'sinin değişip değişmediğini kontrol eder.
     *
     * @param initialUrl Kontrol edilecek başlangıç URL'si
     * @param timeoutS Maksimum bekleme süresi (saniye)
     * @param pollIntervalMs Kontrol aralığı (ms)
     * @return true: URL değişti, false: timeout içinde değişmedi
     */
    public boolean waitForUrlChange(String initialUrl, int timeoutS, int pollIntervalMs) {
        log.info("➡ Sayfanın URL'si '{}' ile başlayıp değişip değişmediği kontrol ediliyor...", initialUrl);
        long endTime = System.currentTimeMillis() + timeoutS * 1000L;

        while (System.currentTimeMillis() < endTime) {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.equals(initialUrl)) {
                log.info("✔ Sayfanın URL'si değişti: '{}'", currentUrl);
                return true;
            }
            try {
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("⚠ URL değişim kontrolü sırasında bekleme kesildi.");
                return false;
            }
        }

        log.warn("⚠ Sayfanın URL'si '{}' süresi boyunca değişmedi.", initialUrl);
        return false;
    }
    /**
     * Sayfanın tamamen yüklenmesini bekler (document.readyState = complete)
     * @param timeoutS Maksimum bekleme süresi (saniye)
     */
    public void waitForPageLoad(int timeoutS) {
        long endTime = System.currentTimeMillis() + timeoutS * 1000L;

        while (System.currentTimeMillis() < endTime) {
            try {
                String readyState = (String) ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState");
                if ("complete".equals(readyState)) {
                    log.info("✔ Sayfa tamamen yüklendi.");
                    return;
                }
                Thread.sleep(500); // yarım saniye bekle ve tekrar kontrol et
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Page load wait interrupted", e);
            }
        }

        throw new RuntimeException("❌ Sayfa yüklenmedi (timeout: " + timeoutS + "s)");
    }
    /**
     * Belirtilen milisaniye kadar bekleme fonksiyonu
     * @param milisaniye Beklenecek süre (milisaniye cinsinden).
     */
    public void waitFor(long milisaniye) {
        // Loglama: milisaniye değişkenini kullanarak ne kadar bekleneceğini belirtir
        log.info("⏱ {}ms bekleniyor...", milisaniye);
        try {
            // Parametreden gelen milisaniye değerini kullanır
            Thread.sleep(milisaniye);
        } catch (InterruptedException e) {
            // Hata durumunda uyarı verir
            log.warn("⚠ Bekleme sırasında hata oluştu: {}", e.getMessage());
            // Mevcut thread'in kesilme durumunu tekrar ayarlar
            Thread.currentThread().interrupt();
        }
        // Loglama: Beklemenin tamamlandığını belirtir
        log.info("✔ {}ms bekleme tamamlandı.", milisaniye);
    }


    /**
     * Aşağıdaki kodlar belki kullanılır diye bırakıldı
     */
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
    /**
     * DOM'da var mı diye kontrol ederek, görünür olmasını bekler.
     * 5 saniye boyunca retry yapar. Bulamazsa iframe kontrolü yapar.
     *
     * @param locator Aranacak elementin By locator'ı
     * @param keyName JSON key veya açıklama
     * @param pollIntervalMs Deneme aralığı (milisaniye)
     * @return WebElement
     */
    public WebElement findWithRetryAndIframe(By locator, String keyName, int pollIntervalMs) {
        int initialTimeoutS = 5;
        long endTime = System.currentTimeMillis() + initialTimeoutS * 1000L;

        // 1️⃣ Önce direkt DOM'da dene
        while (System.currentTimeMillis() < endTime) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    log.info("✔ '{}' elementi DOM'da bulundu ve görünür.", keyName);
                    return elements.get(0);
                }
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted for element: " + keyName, e);
            }
        }

        // 2️⃣ Eğer bulunamadıysa iframe var mı kontrol et
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        if (!iframes.isEmpty()) {
            log.info("➡ Iframe bulundu, '{}' elementini iframe içinde arayacağız...", keyName);
            for (WebElement frame : iframes) {
                try {
                    driver.switchTo().frame(frame);
                    List<WebElement> elementsInFrame = driver.findElements(locator);
                    if (!elementsInFrame.isEmpty() && elementsInFrame.get(0).isDisplayed()) {
                        log.info("✔ '{}' elementi iframe içinde bulundu ve görünür.", keyName);
                        return elementsInFrame.get(0);
                    }
                } finally {
                    driver.switchTo().defaultContent(); // iframe’den çık
                }
            }
        } else {
            log.info("⚠ Iframe bulunamadı. Element iframe içinde değil.");
        }

        throw new TimeoutException("'" + keyName + "' elementi DOM'da veya iframe içinde görünür hale gelmedi.");
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
     * DOM'da var mı diye kontrol ederek, görünür olmasını bekler.
     * Element DOM'a eklenene kadar veya iframe içinde JS ile görünür yapılarak retry yapılır.
     *
     * @param locator Aranacak elementin By locator'ı
     * @param keyName JSON key veya açıklama
     * @param pollIntervalMs Deneme aralığı (milisaniye)
     * @return WebElement
     */
    public WebElement findWithRetryAndIframeJs(By locator, String keyName, int pollIntervalMs) {
        int initialTimeoutS = 5;
        long endTime = System.currentTimeMillis() + initialTimeoutS * 1000L;

        // 1️⃣ Önce direkt DOM'da dene
        while (System.currentTimeMillis() < endTime) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    log.info("✔ '{}' elementi DOM'da bulundu ve görünür.", keyName);
                    return elements.get(0);
                }
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted for element: " + keyName, e);
            }
        }

        // 2️⃣ Eğer bulunamadıysa iframe var mı kontrol et
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        if (!iframes.isEmpty()) {
            log.info("➡ Iframe bulundu, '{}' elementini iframe içinde arayacağız...", keyName);
            for (WebElement frame : iframes) {
                try {
                    driver.switchTo().frame(frame);
                    List<WebElement> elementsInFrame = driver.findElements(locator);
                    if (!elementsInFrame.isEmpty()) {
                        WebElement element = elementsInFrame.get(0);
                        // JS ile görünür yap
                        ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView({block:'center', inline:'center'});" +
                                        "arguments[0].style.visibility='visible';" +
                                        "arguments[0].style.display='block';", element
                        );
                        if (element.isDisplayed()) {
                            log.info("✔ '{}' elementi iframe içinde JS ile görünür hale getirildi.", keyName);
                            return element;
                        }
                    }
                } finally {
                    driver.switchTo().defaultContent(); // iframe’den çık
                }
            }
        } else {
            log.info("⚠ Iframe bulunamadı. Element iframe içinde değil.");
        }

        throw new TimeoutException("'" + keyName + "' elementi DOM'da veya iframe içinde görünür hale gelmedi.");
    }
    public WebElement findWithIframeAndShadowJs(By locator, String keyName, int pollIntervalMs) {
        int initialTimeoutS = 5;
        long endTime = System.currentTimeMillis() + initialTimeoutS * 1000L;

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1️⃣ Önce direkt DOM'da dene
        while (System.currentTimeMillis() < endTime) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    log.info("✔ '{}' elementi DOM'da bulundu ve görünür.", keyName);
                    return elements.get(0);
                }
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted for element: " + keyName, e);
            }
        }

        // 2️⃣ Iframe varsa iframe içinde dene
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement frame : iframes) {
            try {
                driver.switchTo().frame(frame);
                long iframeEndTime = System.currentTimeMillis() + 5000; // iframe içinde 5s dene
                while (System.currentTimeMillis() < iframeEndTime) {
                    List<WebElement> elementsInFrame = driver.findElements(locator);
                    if (!elementsInFrame.isEmpty()) {
                        WebElement element = elementsInFrame.get(0);

                        // JS ile scroll ve görünür yap
                        js.executeScript(
                                "arguments[0].scrollIntoView({block:'center', inline:'center'});" +
                                        "arguments[0].style.visibility='visible';" +
                                        "arguments[0].style.display='block';" +
                                        "arguments[0].style.opacity='1';" +
                                        "arguments[0].style.transform='none';", element
                        );

                        // Shadow DOM var mı kontrolü ve erişim
                        WebElement shadowElement = null;
                        try {
                            shadowElement = (WebElement) js.executeScript(
                                    "return arguments[0].shadowRoot ? arguments[0].shadowRoot.querySelector('#child-element') : null;",
                                    element
                            );
                        } catch (Exception ignored) {}

                        if (shadowElement != null) {
                            // Shadow element görünür yap
                            js.executeScript(
                                    "arguments[0].scrollIntoView({block:'center', inline:'center'});" +
                                            "arguments[0].style.visibility='visible';" +
                                            "arguments[0].style.display='block';" +
                                            "arguments[0].style.opacity='1';" +
                                            "arguments[0].style.transform='none';", shadowElement
                            );
                            if (shadowElement.isDisplayed()) {
                                log.info("✔ '{}' Shadow DOM elementi iframe içinde görünür hale getirildi.", keyName);
                                return shadowElement;
                            }
                        } else {
                            if (element.isDisplayed()) {
                                log.info("✔ '{}' elementi iframe içinde JS ile görünür hale getirildi.", keyName);
                                return element;
                            }
                        }
                    }
                    Thread.sleep(pollIntervalMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                driver.switchTo().defaultContent();
            }
        }

        throw new TimeoutException("'" + keyName + "' elementi DOM, iframe veya Shadow DOM içinde görünür hale gelmedi.");
    }
    public boolean isPresent(By locator, String keyName) {
        try {
            driver.findElement(locator);
            log.info("ℹ️ '{}' elementi DOM'da bulundu.", keyName);
            return true;
        } catch (NoSuchElementException e) {
            log.warn("⚠ '{}' elementi DOM'da bulunamadı.", keyName);
            return false;
        }
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
