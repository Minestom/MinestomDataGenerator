package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class BiomeGenerator extends DataGeneratorCommon {

    @Override
    public JsonObject generate() {
        Set<ResourceLocation> biomeRLs = BuiltinRegistries.BIOME.keySet();
        JsonObject biomes = new JsonObject();

        for (ResourceLocation biomeRL : biomeRLs) {
            Biome b = BuiltinRegistries.BIOME.get(biomeRL);
            if (b == null) {
                continue;
            }
            JsonObject biome = new JsonObject();
            biome.addProperty("humid", b.isHumid());
            biome.addProperty("scale", b.getScale());
            biome.addProperty("depth", b.getDepth());
            biome.addProperty("temperature", b.getBaseTemperature());
            biome.addProperty("downfall", b.getDownfall());
            biome.addProperty("precipitation", b.getPrecipitation().name());
            biome.addProperty("category", b.getBiomeCategory().name());
            // Colors
            biome.addProperty("fogColor", b.getFogColor());
            biome.addProperty("waterColor", b.getWaterColor());
            biome.addProperty("waterFogColor", b.getWaterFogColor());
            biome.addProperty("skyColor", b.getSkyColor());
            biome.addProperty("foliageColor", b.getFoliageColor());
            biome.addProperty("foliageColorOverride", b.getSpecialEffects().getFoliageColorOverride().orElse(null));
            biome.addProperty("grassColorOverride", b.getSpecialEffects().getGrassColorOverride().orElse(null));
            biome.addProperty("grassColorModifier", b.getSpecialEffects().getGrassColorModifier().name());

            biomes.add(biomeRL.toString(), biome);
        }
        return biomes;
    }
}
