package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class VillagerTypeGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject villagerTypes = new JsonObject();
        var registry = BuiltInRegistries.VILLAGER_TYPE;
        for (var villagerType : registry) {
            final var location = registry.getKey(villagerType);
            JsonObject villagerTypeJson = new JsonObject();
            villagerTypeJson.addProperty("id", registry.getId(villagerType));
            villagerTypes.add(location.toString(), villagerTypeJson);
        }
        return villagerTypes;
    }
}
