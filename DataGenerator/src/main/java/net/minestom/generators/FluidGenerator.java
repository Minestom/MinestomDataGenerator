package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class FluidGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject fluids = new JsonObject();
        for (var fluid : Registry.FLUID) {
            final var location = Registry.FLUID.getKey(fluid);
            JsonObject fluidJson = new JsonObject();
            fluidJson.addProperty("bucketId", Registry.ITEM.getKey(fluid.getBucket()).toString());
            fluids.add(location.toString(), fluidJson);
        }
        return fluids;
    }
}
