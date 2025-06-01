package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class FluidGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject fluids = new JsonObject();
        var registry = BuiltInRegistries.FLUID;
        var itemRegistry = BuiltInRegistries.ITEM;
        for (var fluid : registry) {
            final var location = registry.getKey(fluid);
            JsonObject fluidJson = new JsonObject();
            fluidJson.addProperty("id", registry.getId(fluid));
            fluidJson.addProperty("bucketId", itemRegistry.getKey(fluid.getBucket()).toString());
            fluids.add(location.toString(), fluidJson);
        }
        return fluids;
    }
}
