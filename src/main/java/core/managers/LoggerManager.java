package core.managers;

// SLF4J API importları
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // SLF4J'de manager yerine Factory kullanılır

public class LoggerManager {
    public static Logger getLogger(Class<?> cls) {
        return LoggerFactory.getLogger(cls);
    }
}
