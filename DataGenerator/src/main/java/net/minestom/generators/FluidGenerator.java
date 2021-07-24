package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class FluidGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> fluidRLs = Registry.FLUID.keySet();
        JsonObject fluids = new JsonObject();

        for (ResourceLocation fluidRL : fluidRLs) {
            Fluid f = Registry.FLUID.get(fluidRL);

            JsonObject fluid = new JsonObject();
            fluid.addProperty("bucketId", Registry.ITEM.getKey(f.getBucket()).toString());

            fluids.add(fluidRL.toString(), fluid);
        }
        return fluids;
    }
}
