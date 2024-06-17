package net.minestom.generators;

import com.google.gson.JsonArray;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minestom.datagen.DataGenerator;

import java.util.Set;

public final class FeatureFlagGenerator extends DataGenerator {
    @Override
    public JsonArray generate() throws Exception {
        JsonArray flags = new JsonArray();

        Set<ResourceLocation> featureFlags = FeatureFlags.REGISTRY.toNames(FeatureFlags.REGISTRY.allFlags());
        for (ResourceLocation namespace : featureFlags) {
            flags.add(namespace.toString());
        }

        return flags;
    }
}
