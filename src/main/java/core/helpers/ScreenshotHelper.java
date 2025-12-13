package core.helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import java.io.File;

public class ScreenshotHelper {

    private final WebDriver driver;

    public ScreenshotHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void takeScreenshot(String name) {
        try {
            // Screenshot al
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // logs klasörünü oluştur (yoksa)
            File logsDir = new File("logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }

            // Dosya yolu logs klasörüne
            File dest = new File(logsDir, name + ".png");

            // Dosyayı kopyala
            FileHandler.copy(src, dest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
