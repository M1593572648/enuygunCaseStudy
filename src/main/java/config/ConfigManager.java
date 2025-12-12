package config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties props;

    static {
        try {
            props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            props.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config!", e);
        }
    }
    public static int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
