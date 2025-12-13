package api;

import api.models.Pet;
import io.restassured.response.Response;
import org.slf4j.Logger;
import core.managers.LoggerManager;

public class PetRequests {

    private static final Logger logger = LoggerManager.getLogger(PetRequests.class);

    public static Response createPet(Pet pet) {
        logger.info("Creating pet: {}", pet);
        return ApiClient.baseRequest()
                .body(pet)
                .post("/pet");
    }

    public static Response getPetById(long id) {
        logger.info("Getting pet by ID: {}", id);
        return ApiClient.baseRequest().get("/pet/" + id);
    }

    public static Response updatePet(Pet pet) {
        logger.info("Updating pet ID: {}", pet.id);
        return ApiClient.baseRequest()
                .body(pet)
                .put("/pet");
    }

    public static Response deletePet(long id) {
        logger.info("Deleting pet ID: {}", id);
        return ApiClient.baseRequest().delete("/pet/" + id);
    }
}
