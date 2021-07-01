package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerType;
import net.minestom.generators.common.DataGeneratorCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

public final class VillagerTypeGenerator extends DataGeneratorCommon<VillagerType> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerTypeGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : VillagerType.class.getDeclaredFields()) {
            if (!VillagerType.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                VillagerType vt = (VillagerType) declaredField.get(null);
                names.put(vt, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map villager type naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        Set<ResourceLocation> villagerTypeRLs = Registry.VILLAGER_TYPE.keySet();
        JsonObject villagerTypes = new JsonObject();

        for (ResourceLocation villagerTypeRL : villagerTypeRLs) {
            VillagerType vt = Registry.VILLAGER_TYPE.get(villagerTypeRL);

            JsonObject villagerType = new JsonObject();
            villagerType.addProperty("id", Registry.VILLAGER_TYPE.getId(vt));
            villagerType.addProperty("mojangName", names.get(vt));

            villagerTypes.add(villagerTypeRL.toString(), villagerType);
        }
        return villagerTypes;
    }
}
