package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minestom.datagen.DataGenerator;

import java.util.Set;

public final class FeatureFlagGenerator extends DataGenerator {
    @Override
    public JsonObject generate() throws Exception {
        JsonObject flags = new JsonObject();

        Set<Identifier> featureFlags = FeatureFlags.REGISTRY.toNames(FeatureFlags.REGISTRY.allFlags());
        int idCounter = 0;
        for (Identifier namespace : featureFlags) {
            JsonObject flag = new JsonObject();
            flag.addProperty("id", idCounter++);
            flags.add(namespace.toString(), flag);
        }

        return flags;
    }
}
