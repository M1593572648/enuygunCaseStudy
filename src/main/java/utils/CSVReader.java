package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static org.testng.Assert.fail;

public class CSVReader {

    private static final Logger log = LoggerFactory.getLogger(CSVReader.class);

    public static List<Map<String, String>> read(String csvPath) {
        List<Map<String, String>> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

            String headerLine = br.readLine();
            if (headerLine == null) {
                fail("❌ CSV header bulunamadı");
            }

            String[] headers = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);

                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], i < values.length ? values[i] : "");
                }

                rows.add(row);
            }

            log.info("📄 CSV okundu → {} satır", rows.size());

        } catch (Exception e) {
            fail("❌ CSV okunamadı: " + e.getMessage());
        }

        return rows;
    }
}
