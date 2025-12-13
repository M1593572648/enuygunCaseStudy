package core.helpers;

import core.managers.LoggerManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;

import java.util.List;

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
     * Iframe ve Shadow DOM içindeki bir elementi görünür hale getirir.
     * StaleElementReferenceException veya DOM'da bulunamama durumunda shadow DOM içinde arama yapılır.
     * Her adım loglanır.
     *
     * @param locator Aranacak elementin By locator'ı
     * @param keyName Log için element adı
     * @param timeoutS Maksimum bekleme süresi (saniye)
     * @param pollIntervalMs Deneme aralığı (ms)
     * @return Görünür hale getirilmiş WebElement
     */
    public WebElement makeVisibleInsideIframeAndShadow(By locator, String keyName, int timeoutS, int pollIntervalMs) {
        long endTime = System.currentTimeMillis() + timeoutS * 1000L;
        int attempt = 0;

        String cssSelector = locatorToCss(locator); // JS querySelector için string

        while (System.currentTimeMillis() < endTime) {
            attempt++;
            try {
                log.info("🔄 [{}] Deneme {}: '{}' elementi DOM'da aranıyor...", attempt, attempt, keyName);

                // 1️⃣ Normal DOM + görünürlük kontrolü
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    WebElement element = elements.get(0);
                    element = fetchFromShadowWithLog(element, keyName, attempt);
                    makeElementVisibleJsWithLog(element, keyName, attempt);
                    if (element.isDisplayed()) {
                        log.info("✔ [{}] Deneme {}: '{}' elementi DOM'da görünür hale getirildi.", attempt, attempt, keyName);
                        return element;
                    }
                }

                // 2️⃣ Iframe kontrolü
                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                for (WebElement frame : iframes) {
                    try {
                        driver.switchTo().frame(frame);
                        WebElement found = findElementInDomOrShadow(locator, keyName, attempt);
                        if (found != null) {
                            makeElementVisibleJsWithLog(found, keyName, attempt);
                            if (found.isDisplayed()) {
                                log.info("✔ [{}] Deneme {}: '{}' elementi iframe içinde görünür hale getirildi.", attempt, attempt, keyName);
                                return found;
                            }
                        }
                    } finally {
                        driver.switchTo().defaultContent();
                    }
                }

                // 3️⃣ Shadow DOM global arama
                WebElement shadowFound = findElementInShadowGlobally(locator, keyName, attempt);
                if (shadowFound != null) {
                    makeElementVisibleJsWithLog(shadowFound, keyName, attempt);
                    if (shadowFound.isDisplayed()) {
                        log.info("✔ [{}] Deneme {}: '{}' elementi shadow DOM içinde görünür hale getirildi.", attempt, attempt, keyName);
                        return shadowFound;
                    }
                }

                Thread.sleep(pollIntervalMs); // ⏳ bekleme

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                log.warn("⚠ [{}] Deneme {}: '{}' elementi stale oldu, shadow DOM içinde tekrar aranacak...", attempt, attempt, keyName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted for element: " + keyName, e);
            }
        }

        throw new RuntimeException("'" + keyName + "' elementi görünür hale getirilemedi (timeout: " + timeoutS + "s)");
    }
    /** DOM veya shadow içinde arama (iframe recursive dahil) */
    private WebElement findElementInDomOrShadow(By locator, String keyName, int attempt) {
        List<WebElement> elements = driver.findElements(locator);
        if (!elements.isEmpty()) {
            WebElement element = elements.get(0);
            return fetchFromShadowWithLog(element, keyName, attempt);
        }
        // DOM’da yoksa shadow root’ları kontrol et
        List<WebElement> shadowHosts = driver.findElements(By.cssSelector("*"));
        for (WebElement host : shadowHosts) {
            Boolean isShadow = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].shadowRoot != null;", host);
            if (isShadow) {
                WebElement shadowElem = getElementFromShadowDom(host, locatorToCss(locator));
                if (shadowElem != null) return shadowElem;
            }
        }
        return null;
    }
    /** Shadow DOM içinde global arama (stale veya DOM'da yoksa) */
    private WebElement findElementInShadowGlobally(By locator, String keyName, int attempt) {
        log.info("➡ [{}] Deneme {}: '{}' elementi shadow DOM içinde global aranıyor...", attempt, attempt, keyName);
        List<WebElement> shadowHosts = driver.findElements(By.cssSelector("*"));
        for (WebElement host : shadowHosts) {
            Boolean isShadow = (Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].shadowRoot != null;", host);
            if (isShadow) {
                WebElement shadowElem = getElementFromShadowDom(host, locatorToCss(locator));
                if (shadowElem != null) {
                    log.info("✅ [{}] Deneme {}: '{}' elementi shadow DOM içinde bulundu.", attempt, attempt, keyName);
                    return shadowElem;
                }
            }
        }
        log.info("ℹ [{}] Deneme {}: '{}' shadow DOM içinde bulunamadı.", attempt, attempt, keyName);
        return null;
    }
    /** By locator’ı CSS selector string’e çevir (sadece basit id veya class için) */
    private String locatorToCss(By locator) {
        String locatorStr = locator.toString();
        if (locatorStr.startsWith("By.id:")) {
            return "#" + locatorStr.replace("By.id:", "").trim();
        } else if (locatorStr.startsWith("By.className:")) {
            return "." + locatorStr.replace("By.className:", "").trim();
        } else if (locatorStr.startsWith("By.cssSelector:")) {
            return locatorStr.replace("By.cssSelector:", "").trim();
        } else {
            log.warn("⚠ Locator tipini JS querySelector için uyarlamak gerekebilir: {}", locatorStr);
            return "*"; // fallback
        }
    }
    /** Shadow DOM kontrolü + log */
    private WebElement fetchFromShadowWithLog(WebElement element, String keyName, int attempt) {
        Boolean isShadow = (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot != null;", element);
        if (isShadow) {
            log.info("➡ [{}] Deneme {}: '{}' elementinin Shadow DOM içinde olduğu tespit edildi, gerçek element alınacak...", attempt, attempt, keyName);
            element = getElementFromShadowDom(element, "*");
            log.info("✔ [{}] Deneme {}: '{}' Shadow DOM içindeki element DOM üzerinden alındı.", attempt, attempt, keyName);
        } else {
            log.info("ℹ [{}] Deneme {}: '{}' Shadow DOM içinde değil.", attempt, attempt, keyName);
        }
        return element;
    }
    /** JS ile görünür yap + log */
    private void makeElementVisibleJsWithLog(WebElement element, String keyName, int attempt) {
        log.info("➡ [{}] Deneme {}: '{}' elementi JS ile görünür hale getiriliyor...", attempt, attempt, keyName);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center', inline:'center'});" +
                        "arguments[0].style.visibility='visible';" +
                        "arguments[0].style.display='block';", element
        );
        log.info("✔ [{}] Deneme {}: '{}' elementi JS ile görünür hale getirildi.", attempt, attempt, keyName);
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
        log.info("➡ '{}' kaydırıcısı, Min: {} (dk) ve Max: {} (dk) değerlerine JS Konum ve Actions Tıklama ile ayarlanıyor...", handleName, targetMinValue, targetMaxValue);

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // --- Sol Kol (Min Handle) İşlemi ---
            double minTargetPercentage = calculateCssPercentage(targetMinValue, "Sol Kol (Min)");
            log.info("  -> Sol Kol %{} pozisyonuna JS ile taşınıyor...", String.format("%.2f", minTargetPercentage));

            // JS: Pozisyonu ve değeri ayarla
            String minHandleScript =
                    "arguments[0].style.left = arguments[1] + '%';" +
                            "arguments[0].setAttribute('aria-valuenow', arguments[2]);";

            js.executeScript(minHandleScript, minHandleElement, minTargetPercentage, targetMinValue);

            // Actions: Tıklama ile filtrenin tetiklenmesini sağla
            actions.click(minHandleElement).perform();
            log.info("  -> Sol Kol (Min) {} ayarlandı ve üzerine tıklandı (Actions).", targetMinValue);

            Thread.sleep(2000);

            // --- Sağ Kol (Max Handle) İşlemi ---
            double maxTargetPercentage = calculateCssPercentage(targetMaxValue, "Sağ Kol (Max)");
            log.info("  -> Sağ Kol %{} pozisyonuna JS ile taşınıyor...", String.format("%.2f", maxTargetPercentage));

            // JS: Pozisyonu ve değeri ayarla
            String maxHandleScript =
                    "arguments[0].style.left = arguments[1] + '%';" +
                            "arguments[0].setAttribute('aria-valuenow', arguments[2]);";

            js.executeScript(maxHandleScript, maxHandleElement, maxTargetPercentage, targetMaxValue);

            // Actions: Tıklama ile filtrenin tetiklenmesini sağla
            // Sağ kolda Stale Element hatası alma riskine rağmen, Actions'ı deniyoruz çünkü en doğru olayı tetikliyor.
            actions.click(maxHandleElement).perform();
            log.info("  -> Sağ Kol (Max) {} ayarlandı ve üzerine tıklandı (Actions).", targetMaxValue);

            // Değerlerin güncellenmesi ve filtrenin uygulanması için DOM'a bekleme
            Thread.sleep(2000);

            // Son Kontrol
            log.info("✔ '{}' kaydırıcısı başarıyla ayarlandı ve tetiklendi. Min:{}, Max:{}",
                    handleName, getCurrentSliderValue(minHandleElement), getCurrentSliderValue(maxHandleElement));

        } catch (Exception e) {
            // Sadece loglamak yerine hata fırlatmaya devam edin, ancak özel Stale Element hatası yakalanabilir.
            log.error("❌ '{}' kaydırıcısı ayarlanamadı veya tetiklenirken hata oluştu! Hata: {}", handleName, e.getMessage());
            throw new RuntimeException("slideRangeSlider failed for: " + handleName, e);
        }
    }
    /**
     * Kaydırıcı kolunun, mevcut değerden hedef değere ulaşması için kaç piksel
     * yatayda hareket etmesi gerektiğini hesaplar.
     *
     * @param handleElement Kaydırıcı kolu (WebElement)
     * @param targetValue Hedef değer (dakika)
     * @param handleLabel Log etiketi
     * @return Hareket ettirilmesi gereken piksel sayısı.
     */
    private int calculatePixelsToMove(WebElement handleElement, int targetValue, String handleLabel) {
        // Toplam değer aralığı (0'dan 1439'a, yani 1440 birim)
        final int MAX_SLIDER_VALUE = 1439;

        // 1. Mevcut Değeri Al (aria-valuenow)
        int currentValue = getCurrentSliderValue(handleElement);

        // 2. Elementin bulunduğu kaydırıcı konteynırını bul (Genişliği almak için)
        // rc-slider-handle-1/2 elementinin bir üst ebeveyni rc-slider div'idir.
        WebElement sliderContainer = handleElement.findElement(By.xpath("./..")); // Parent elementi bul
        int sliderWidth = sliderContainer.getSize().getWidth();

        // 3. Hedef değerin toplam aralık içindeki yüzdesini hesapla
        double targetRatio = (double) targetValue / MAX_SLIDER_VALUE;
        double currentRatio = (double) currentValue / MAX_SLIDER_VALUE;

        // 4. Hedef konumun (piksel cinsinden) mevcut konumdan farkını bul
        double targetPixelPosition = sliderWidth * targetRatio;
        double currentPixelPosition = sliderWidth * currentRatio;

        int pixelsToMove = (int) Math.round(targetPixelPosition - currentPixelPosition);

        log.info("  -> {} Kaydırıcı Genişliği: {}px. Mevcut Değer: {} ({}), Hedef Değer: {} ({})",
                handleLabel, sliderWidth, currentValue, currentPixelPosition, targetValue, targetPixelPosition);
        log.info("  -> {} Toplam hareket mesafesi: {} piksel.", handleLabel, pixelsToMove);

        return pixelsToMove;
    }
    /**
     * Kaydırıcı kolunun hedef değere ulaşması için ayarlanması gereken
     * CSS 'left' veya 'right' yüzdesini hesaplar.
     *
     * @param targetValue Hedef değer (dakika)
     * @param handleLabel Log etiketi
     * @return 0.0 ile 100.0 arasında hedef yüzde değeri.
     */
    private double calculateCssPercentage(int targetValue, String handleLabel) {
        // Toplam değer aralığı (0'dan 1439'a, yani 1440 birim)
        final int MAX_SLIDER_VALUE = 1439;

        // Hedef değerin toplam aralık içindeki oranını hesapla
        // 0 dk = 0%, 1439 dk = ~100%
        double targetRatio = (double) targetValue / MAX_SLIDER_VALUE;
        double targetPercentage = targetRatio * 100;

        log.info("  -> {} Hedef Değer: {} dk. Hesaplanan CSS Yüzdesi (left): {}%",
                handleLabel, targetValue, String.format("%.2f", targetPercentage));

        return targetPercentage;
    }
    /**
     * Bir kaydırıcı kolunu hedef değere taşır.
     * Klavye tuş vuruşlarını Actions sınıfı ile daha kararlı hale getirir.
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

            // 1. Odağı Actions ile kesinleştirme (bazı sistemlerde sadece click yetmez)
            actions.click(handleElement).perform();

            // 2. Tuş vuruşları zincirini Actions ile gönderme
            Actions keyActions = new Actions(driver);
            for (int i = 0; i < steps; i++) {
                keyActions.sendKeys(keyToPress);
            }
            keyActions.build().perform(); // Tüm zinciri çalıştır

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
    /** Eğer element Shadow DOM içindeyse, gerçek DOM elementine güvenli erişim */
    public WebElement getElementFromShadowDom(WebElement shadowHost, String cssSelectorInsideShadow) {
        try {
            String script = "return arguments[0].shadowRoot.querySelector(arguments[1]);";
            WebElement element = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript(script, shadowHost, cssSelectorInsideShadow);
            if (element != null) {
                log.info("✔ Shadow DOM içindeki element erişildi: {}", cssSelectorInsideShadow);
            } else {
                log.info("ℹ Shadow DOM içindeki element bulunamadı: {}", cssSelectorInsideShadow);
            }
            return element; // null dönebilir
        } catch (Exception e) {
            log.warn("⚠ Shadow DOM içindeki element erişilemedi: {}. Hata: {}", cssSelectorInsideShadow, e.getMessage());
            return null;
        }
    }
}
