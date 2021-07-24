package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerType;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class VillagerTypeGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> villagerTypeRLs = Registry.VILLAGER_TYPE.keySet();
        JsonObject villagerTypes = new JsonObject();

        for (ResourceLocation villagerTypeRL : villagerTypeRLs) {
            VillagerType vt = Registry.VILLAGER_TYPE.get(villagerTypeRL);

            JsonObject villagerType = new JsonObject();
            villagerType.addProperty("id", Registry.VILLAGER_TYPE.getId(vt));

            villagerTypes.add(villagerTypeRL.toString(), villagerType);
        }
        return villagerTypes;
    }
}
