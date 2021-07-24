package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.generators.common.DataGeneratorCommon;

public final class VillagerTypeGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject villagerTypes = new JsonObject();
        for (var entry : Registry.VILLAGER_TYPE.entrySet()) {
            final var location = entry.getKey().location();
            final var villagerType = entry.getValue();
            JsonObject villagerTypeJson = new JsonObject();
            villagerTypeJson.addProperty("id", Registry.VILLAGER_TYPE.getId(villagerType));
            villagerTypes.add(location.toString(), villagerTypeJson);
        }
        return villagerTypes;
    }
}
