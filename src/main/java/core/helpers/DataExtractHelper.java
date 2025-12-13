package core.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CSVWriter;

import java.util.ArrayList;
import java.util.List;

public class DataExtractHelper {

    private final WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(DataExtractHelper.class);

    public DataExtractHelper(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Uçuş listesini kolonlu şekilde CSV'ye yazar
     */
    public void extractFlightListToCSV(WebElement containerDiv, String csvFilePath) {

        List<List<String>> rows = new ArrayList<>();

        // ---- HEADER ----
        rows.add(List.of(
                "Airline",
                "Route",
                "DepartureTime",
                "ArrivalTime",
                "Duration",
                "Transit",
                "Price",
                "Currency",
                "Baggage"
        ));

        List<WebElement> flights =
                containerDiv.findElements(By.cssSelector(".flight-item"));

        log.info("✈️ Toplam {} uçuş bulundu", flights.size());

        for (int i = 0; i < flights.size(); i++) {
            WebElement flight = flights.get(i);

            String airline = getText(flight, ".summary-marketing-airlines");
            String route = getText(flight, ".summary-airports");
            String departure = getTextByTestId(flight, "departureTime");
            String arrival = getTextByTestId(flight, "arrivalTime");
            String duration = getTextByTestId(flight, "departureFlightTime");
            String transit = getTextContainsTestId(flight, "transferState");
            String baggage = getText(flight, ".summary-luggage-unit");

            WebElement priceEl = getElementByTestId(flight, "flightInfoPrice");
            String price = "";
            String currency = "";

            if (priceEl != null) {
                price = safe(priceEl.getAttribute("data-price"));
                currency = safe(priceEl.getAttribute("data-currency"));
            }

            rows.add(List.of(
                    airline,
                    route,
                    departure,
                    arrival,
                    duration,
                    transit,
                    price,
                    currency,
                    baggage
            ));

            if (i == 0) {
                log.info("🧪 Örnek satır → {} | {} | {} → {} | {} | {} | {} {} | {}",
                        airline, route, departure, arrival,
                        duration, transit, price, currency, baggage);
            }
        }

        new CSVWriter(csvFilePath).write(rows);
        log.info("✅ CSV oluşturuldu: {} ({} satır)", csvFilePath, rows.size() - 1);
    }

    // =====================================================
    // ================ INTERNAL HELPERS ===================
    // =====================================================

    private String getText(WebElement parent, String cssSelector) {
        try {
            return safe(parent.findElement(By.cssSelector(cssSelector)).getText());
        } catch (Exception e) {
            return "";
        }
    }

    private String getTextByTestId(WebElement parent, String testId) {
        try {
            return safe(parent
                    .findElement(By.cssSelector("[data-testid='" + testId + "']"))
                    .getText());
        } catch (Exception e) {
            return "";
        }
    }

    private String getTextContainsTestId(WebElement parent, String testIdPart) {
        try {
            return safe(parent
                    .findElement(By.cssSelector("[data-testid*='" + testIdPart + "']"))
                    .getText());
        } catch (Exception e) {
            return "";
        }
    }

    private WebElement getElementByTestId(WebElement parent, String testId) {
        try {
            return parent.findElement(By.cssSelector("[data-testid='" + testId + "']"));
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim().replace(",", ";");
    }
}
