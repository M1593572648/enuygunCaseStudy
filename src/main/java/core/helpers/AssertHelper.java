package core.helpers;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.*;

public class AssertHelper {

    private static final Logger log = LoggerFactory.getLogger(AssertHelper.class);

    public void verifyVisible(WebElement element) {
        assertTrue(element.isDisplayed(), "Element görünmüyor");
        log.info("Element görünür durumda");
    }

    public void verifyEquals(String actual, String expected) {
        assertEquals(actual, expected);
        log.info("Assert Passed → {} == {}", actual, expected);
    }

    public void verifyContains(String actual, String part) {
        assertTrue(actual.contains(part));
        log.info("Assert Passed → {} içinde {}", actual, part);
    }
}
