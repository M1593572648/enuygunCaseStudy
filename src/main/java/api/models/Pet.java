package api.models;

import java.util.List;

public class Pet {

    public long id;
    public String name;
    public String status;
    public List<String> photoUrls;

    public Pet() {
    }

    public Pet(long id, String name, String status, List<String> photoUrls) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photoUrls = photoUrls;
    }
}
