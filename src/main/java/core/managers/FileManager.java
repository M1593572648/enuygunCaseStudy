package core.managers;

import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.By;
import utils.JsonUtils;

public class FileManager {

    private final JsonNode jsonNode;

    public FileManager(String fileName) {
        this.jsonNode = JsonUtils.readJson("src/main/resources/locators/" + fileName);
    }
    /**
     * JSON'dan alınan locator'u string olarak döndürür
     * (örneğin XPath ise value direkt alınır)
     */
    public String getLocatorString(String key) {
        try {
            JsonNode node = jsonNode.get(key);
            if (node == null) throw new RuntimeException("JSON key bulunamadı: " + key);

            String type = node.get("type").asText();
            String value = node.get("value").asText();
            return value; // sadece string döndürüyoruz, By değil
        } catch (Exception e) {
            throw new RuntimeException("JSON key okunamadı: " + key, e);
        }
    }
    public By getLocator(String key) {
        try {
            JsonNode node = jsonNode.get(key);

            String type = node.get("type").asText();
            String value = node.get("value").asText();

            return switch (LocatorType.valueOf(type)) {
                case id -> By.id(value);
                case name -> By.name(value);
                case xpath -> By.xpath(value);
                case css -> By.cssSelector(value);
                case className -> By.className(value);
                case tagName -> By.tagName(value);
                case linkText -> By.linkText(value);
                case partialLinkText -> By.partialLinkText(value);
            };

        } catch (Exception e) {
            throw new RuntimeException("JSON key bulunamadı: " + key, e);
        }
    }
}
