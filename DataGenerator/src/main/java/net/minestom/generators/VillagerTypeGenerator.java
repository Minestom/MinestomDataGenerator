package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class VillagerTypeGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject villagerTypes = new JsonObject();
        for (var villagerType : Registry.VILLAGER_TYPE) {
            final var location = Registry.VILLAGER_TYPE.getKey(villagerType);
            JsonObject villagerTypeJson = new JsonObject();
            villagerTypeJson.addProperty("id", Registry.VILLAGER_TYPE.getId(villagerType));
            villagerTypes.add(location.toString(), villagerTypeJson);
        }
        return villagerTypes;
    }
}
