package core.drivers;

import config.ConfigManager;
import core.managers.LoggerManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;

import java.io.File;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger log = LoggerManager.getLogger(DriverFactory.class);

    public static WebDriver getDriver() {

        if (driver.get() == null) {

            String browser = ConfigManager.get("browser");
            boolean checkLocal = Boolean.parseBoolean(ConfigManager.get("driver.checkLocal"));

            log.info("Browser seçildi: {}", browser);

            switch (browser.toLowerCase()) {

                case "chrome":
                    driver.set(initChromeDriver(checkLocal));
                    break;

                case "firefox":
                    driver.set(initFirefoxDriver(checkLocal));
                    break;

                default:
                    log.error("Desteklenmeyen browser: {}", browser);
                    throw new RuntimeException("Unsupported browser: " + browser);
            }
        }

        return driver.get();
    }

    // ====================================================================
    // CHROME
    // ====================================================================
    private static WebDriver initChromeDriver(boolean checkLocal) {

        String localDriverPath = ConfigManager.get("chrome.driver.path");
        File driverFile = new File(localDriverPath);

        ChromeOptions options = new ChromeOptions();
        // Headless veya window objesi olmayan durumlar için
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");

        log.debug("ChromeDriver path (config): {}", localDriverPath);
        log.debug("ChromeDriver exists: {}", driverFile.exists());

        if (checkLocal && driverFile.exists()) {
            log.info("✔ Local ChromeDriver kullanılacak → {}", localDriverPath);
            System.setProperty("webdriver.chrome.driver", localDriverPath);
            return new ChromeDriver(options);
        }

        log.warn("⚠ Local ChromeDriver bulunamadı → WebDriverManager devrede");
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    // ====================================================================
    // FIREFOX
    // ====================================================================
    private static WebDriver initFirefoxDriver(boolean checkLocal) {

        String localDriverPath = ConfigManager.get("firefox.driver.path");
        File driverFile = new File(localDriverPath);

        FirefoxOptions options = new FirefoxOptions();
        // Headless veya window objesi olmayan durumlar için
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        log.debug("GeckoDriver path (config): {}", localDriverPath);
        log.debug("GeckoDriver exists: {}", driverFile.exists());

        if (checkLocal && driverFile.exists()) {
            log.info("✔ Local GeckoDriver kullanılacak → {}", localDriverPath);
            System.setProperty("webdriver.gecko.driver", localDriverPath);
            return new FirefoxDriver(options);
        }

        log.warn("⚠ Local GeckoDriver bulunamadı → WebDriverManager devrede");
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }

    // ====================================================================
    // QUIT
    // ====================================================================
    public static void quitDriver() {
        WebDriver localDriver = driver.get();
        if (localDriver != null) {
            try {
                log.info("Driver kapatılıyor...");
                localDriver.quit();
            } catch (Exception e) {
                log.error("Driver quit sırasında hata: ", e);
            } finally {
                driver.remove();
            }
        } else {
            log.warn("Driver zaten null, quit edilemiyor");
        }
    }

}
