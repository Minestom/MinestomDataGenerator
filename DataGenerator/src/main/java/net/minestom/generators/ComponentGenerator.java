package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public class ComponentGenerator extends DataGenerator {
    @Override
    public JsonObject generate() throws Exception {
        JsonObject result = new JsonObject();
        var registry = BuiltInRegistries.DATA_COMPONENT_TYPE;
        for (var componentType : registry) {
            var name = registry.getKey(componentType);
            var id = registry.getId(componentType);
            var componentJson = new JsonObject();
            componentJson.addProperty("id", id);
            componentJson.addProperty("transient", componentType.isTransient());
            componentJson.addProperty("persistent", componentType.codec() != null);
            result.add(name.toString(), componentJson);
        }
        return result;
    }
}
