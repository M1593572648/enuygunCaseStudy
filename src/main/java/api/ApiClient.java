package api;

import config.ConfigManager;
import core.managers.LoggerManager;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;


public class ApiClient {

    private static final Logger logger =
            LoggerManager.getLogger(ApiClient.class);

    static {
        String baseUrl = ConfigManager.get("api.base.url");
        RestAssured.baseURI = baseUrl;

        logger.info("API Base URL initialized: {}", baseUrl);
    }

    public static RequestSpecification baseRequest() {
        logger.debug("Creating base API request");

        return RestAssured
                .given()
                .contentType("application/json")
                .accept("application/json");
    }
}
