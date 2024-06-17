package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minestom.datagen.DataGenerator;

import java.util.Set;

public final class FeatureFlagGenerator extends DataGenerator {
    @Override
    public JsonObject generate() throws Exception {
        JsonObject flags = new JsonObject();

        Set<ResourceLocation> featureFlags = FeatureFlags.REGISTRY.toNames(FeatureFlags.REGISTRY.allFlags());
        int idCounter = 0;
        for (ResourceLocation namespace : featureFlags) {
            JsonObject flag = new JsonObject();
            flag.addProperty("id", idCounter++);
            flags.add(namespace.toString(), flag);
        }

        return flags;
    }
}
