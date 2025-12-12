package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode readJson(String path) {
        try {
            return mapper.readTree(new File(path));
        } catch (Exception e) {
            throw new RuntimeException("JSON dosyası okunamadı: " + path, e);
        }
    }
}
