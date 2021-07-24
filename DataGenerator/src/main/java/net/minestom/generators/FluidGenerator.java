package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.generators.common.DataGeneratorCommon;

public final class FluidGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject fluids = new JsonObject();
        for (var entry : Registry.FLUID.entrySet()) {
            final var location = entry.getKey().location();
            final var fluid = entry.getValue();
            JsonObject fluidJson = new JsonObject();
            fluidJson.addProperty("bucketId", Registry.ITEM.getKey(fluid.getBucket()).toString());
            fluids.add(location.toString(), fluidJson);
        }
        return fluids;
    }
}
