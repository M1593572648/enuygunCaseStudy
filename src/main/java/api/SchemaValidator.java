package api;

import io.restassured.response.Response;
import io.restassured.module.jsv.JsonSchemaValidator;

import java.io.File;

public class SchemaValidator {

    public static void validatePetSchema(Response response, String schemaPath) {
        File schemaFile = new File(schemaPath);

        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
    }
}
