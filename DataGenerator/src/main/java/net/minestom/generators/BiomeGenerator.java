package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.data.BuiltinRegistries;
import net.minestom.generators.common.DataGeneratorCommon;

public final class BiomeGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject biomes = new JsonObject();
        for (var entry : BuiltinRegistries.BIOME.entrySet()) {
            final var location = entry.getKey().location();
            final var biome = entry.getValue();
            JsonObject biomeJson = new JsonObject();
            biomeJson.addProperty("humid", biome.isHumid());
            biomeJson.addProperty("scale", biome.getScale());
            biomeJson.addProperty("depth", biome.getDepth());
            biomeJson.addProperty("temperature", biome.getBaseTemperature());
            biomeJson.addProperty("downfall", biome.getDownfall());
            biomeJson.addProperty("precipitation", biome.getPrecipitation().name());
            biomeJson.addProperty("category", biome.getBiomeCategory().name());
            // Colors
            biomeJson.addProperty("fogColor", biome.getFogColor());
            biomeJson.addProperty("waterColor", biome.getWaterColor());
            biomeJson.addProperty("waterFogColor", biome.getWaterFogColor());
            biomeJson.addProperty("skyColor", biome.getSkyColor());
            biomeJson.addProperty("foliageColor", biome.getFoliageColor());
            biomeJson.addProperty("foliageColorOverride", biome.getSpecialEffects().getFoliageColorOverride().orElse(null));
            biomeJson.addProperty("grassColorOverride", biome.getSpecialEffects().getGrassColorOverride().orElse(null));
            biomeJson.addProperty("grassColorModifier", biome.getSpecialEffects().getGrassColorModifier().name());
            biomes.add(location.toString(), biomeJson);
        }
        return biomes;
    }
}
