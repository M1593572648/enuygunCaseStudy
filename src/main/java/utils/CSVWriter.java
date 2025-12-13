package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

    private final String filePath;

    public CSVWriter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Satırları CSV dosyasına yazar.
     *
     * @param rows Liste liste halinde veriler, her alt liste bir satır.
     */
    public void write(List<List<String>> rows) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (List<String> row : rows) {
                String line = String.join(",", row);
                writer.append(line).append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("CSV yazma hatası: " + e.getMessage());
        }
    }
}
