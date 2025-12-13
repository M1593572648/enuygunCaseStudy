package core.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AnalyticsHelper {

    public void runPriceAnalysis() {
        String projectRoot = System.getProperty("user.dir");
        String scriptPath = projectRoot + "/analytics/analyze_prices.py";
        String csvPath = projectRoot + "/flights.csv";

        if (!Files.exists(Paths.get(scriptPath))) {
            throw new RuntimeException(" Python script bulunamadı: " + scriptPath);
        }

        if (!Files.exists(Paths.get(csvPath))) {
            throw new RuntimeException(" CSV bulunamadı: " + csvPath);
        }

        String pythonCmd = System.getenv("PYTHON_CMD");
        if (pythonCmd == null) {
            pythonCmd = "python";
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    pythonCmd,
                    scriptPath,
                    csvPath
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines().forEach(System.out::println);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException(" Python analytics script exit code: " + exitCode);
            }

        } catch (Exception e) {
            throw new RuntimeException(" Analytics execution failed", e);
        }
    }
}
