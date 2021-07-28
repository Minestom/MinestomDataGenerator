package net.minestom.generators.tags;

import com.google.gson.JsonObject;
import net.minestom.datagen.DataGenerator;

public final class ItemTagGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        return mergePath(TAGS_FOLDER.resolve("items"));
    }
}
