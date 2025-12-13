package core.helpers;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CSVReader;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class AssertHelper {

    private static final Logger log = LoggerFactory.getLogger(AssertHelper.class);
    private final WebDriver driver;

    public AssertHelper(WebDriver driver) {
        this.driver = driver;
    }

    // --------------------------------------------------
    // CSV ASSERTS
    // --------------------------------------------------

    /** CSV boş mu */
    public void verifyCsvNotEmpty(String csvPath) {
        List<Map<String, String>> rows = CSVReader.read(csvPath);

        assertFalse(rows.isEmpty(), "❌ CSV boş, uçuş bulunamadı");
        log.info("✅ CSV boş değil ({} satır)", rows.size());
    }

    /** Saat aralığı kontrolü */
    public void verifyDepartureTimesBetween(
            String csvPath,
            String startTime,
            String endTime
    ) {
        List<Map<String, String>> rows = CSVReader.read(csvPath);

        LocalTime min = LocalTime.parse(startTime);
        LocalTime max = LocalTime.parse(endTime);

        for (Map<String, String> row : rows) {
            String timeStr = row.get("DepartureTime");
            assertNotNull(timeStr, "❌ DepartureTime boş");

            LocalTime time = LocalTime.parse(timeStr);

            assertTrue(
                    !time.isBefore(min) && !time.isAfter(max),
                    "❌ Saat aralık dışı: " + time
            );
        }

        log.info("✅ Tüm uçuş saatleri {} - {} aralığında", startTime, endTime);
    }

    /** İstanbul → Ankara mı */
    public void verifyRouteIstanbulToAnkara(String csvPath) {
        List<Map<String, String>> rows = CSVReader.read(csvPath);

        for (Map<String, String> row : rows) {
            String route = row.get("Route");
            assertNotNull(route, "❌ Route boş");

            assertTrue(
                    route.endsWith("ESB") &&
                            (route.startsWith("IST") || route.startsWith("SAW")),
                    "❌ Hatalı rota: " + route
            );
        }

        log.info("✅ Tüm uçuşlar İstanbul → Ankara");
    }

    /** Sadece Türk Hava Yolları mı */
    public void verifyOnlyTurkishAirlinesFlights(String csvPath) {
        List<Map<String, String>> rows = CSVReader.read(csvPath);

        for (Map<String, String> row : rows) {
            String airline = row.get("Airline");
            assertNotNull(airline, "❌ Airline boş");

            assertEquals(
                    airline,
                    "Türk Hava Yolları",
                    "❌ THY dışı uçuş bulundu: " + airline
            );
        }

        log.info("✅ Listelenen tüm uçuşlar Türk Hava Yolları");
    }

    /** Fiyatlar artan sırada mı */
    public void verifyPricesSortedAscending(String csvPath) {
        List<Map<String, String>> rows = CSVReader.read(csvPath);

        double previousPrice = 0;

        for (Map<String, String> row : rows) {
            String priceStr = row.get("Price");
            assertNotNull(priceStr, "❌ Price boş");

            double currentPrice = Double.parseDouble(priceStr);

            assertTrue(
                    currentPrice >= previousPrice,
                    "❌ Fiyat sıralaması hatalı. Önceki: "
                            + previousPrice + " Şimdi: " + currentPrice
            );

            previousPrice = currentPrice;
        }

        log.info("✅ Fiyatlar artan sırada (ascending)");
    }

    // --------------------------------------------------
    // URL ASSERTS
    // --------------------------------------------------

    /** URL search parametreleri */
    public void verifySearchParamsInUrl() {
        String url = driver.getCurrentUrl();

        assertTrue(url.contains("gidis="),
                "❌ URL içinde gidiş parametresi yok → " + url);

        assertTrue(url.contains("donus="),
                "❌ URL içinde dönüş parametresi yok → " + url);

        assertTrue(url.contains("trip=domestic"),
                "❌ URL domestic trip değil → " + url);

        log.info("✅ URL parametreleri (gidis, donus, trip) doğrulandı");
    }
}
