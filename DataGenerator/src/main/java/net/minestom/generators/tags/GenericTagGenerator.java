package net.minestom.generators.tags;

import com.google.gson.JsonObject;
import net.minestom.datagen.DataGenerator;

public class GenericTagGenerator extends DataGenerator {
    private final String path;

    public GenericTagGenerator(String path) {
        this.path = path;
    }

    @Override
    public JsonObject generate() {
        return mergePath(TAGS_FOLDER.resolve(path));
    }
}
