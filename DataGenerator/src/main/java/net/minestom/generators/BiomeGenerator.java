package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.world.level.biome.*;
import net.minestom.datagen.DataGenerator;
import net.minestom.utils.ResourceUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public final class BiomeGenerator extends DataGenerator {

    private static final String BIOMES_DIR = "data/minecraft/worldgen/biome/";
    private static final Gson gson = new Gson();

    @Override
    public JsonObject generate() throws Exception {
        var biomesJson = new JsonObject();
        var biomes = readBiomes();

        for (var entry : biomes.entrySet()) {
            var biome = entry.getValue();
            JsonObject biomeJson = new JsonObject();

            biomeJson.addProperty("humid", biome.isHumid());
//            biomeJson.addProperty("scale", biome.dep);
//            biomeJson.addProperty("depth", biome.getDepth());
            biomeJson.addProperty("temperature", biome.getBaseTemperature());
            biomeJson.addProperty("downfall", biome.getDownfall());
            biomeJson.addProperty("precipitation", biome.getPrecipitation().getSerializedName());
            // Colors
            biomeJson.addProperty("fogColor", biome.getFogColor());
            biomeJson.addProperty("waterColor", biome.getWaterColor());
            biomeJson.addProperty("waterFogColor", biome.getWaterFogColor());
            biomeJson.addProperty("skyColor", biome.getSkyColor());
            biomeJson.addProperty("foliageColor", biome.getFoliageColor());
            biomeJson.addProperty("foliageColorOverride", biome.getSpecialEffects().getFoliageColorOverride().orElse(null));
            biomeJson.addProperty("grassColorOverride", biome.getSpecialEffects().getGrassColorOverride().orElse(null));
            biomeJson.addProperty("grassColorModifier", biome.getSpecialEffects().getGrassColorModifier().getSerializedName());
            biomesJson.add(entry.getKey(), biomeJson);
        }

        return biomesJson;
    }

    private Map<String, Biome> readBiomes() throws URISyntaxException, IOException {
        // get all files from the biomes directory
        var files = ResourceUtils.getResourceListing(
                net.minecraft.server.MinecraftServer.class, BIOMES_DIR);

        Map<String, Biome> biomesJson = new HashMap<>();
        for (String fileName : files) {
            var file = net.minecraft.server.MinecraftServer.class
                    .getClassLoader()
                    .getResourceAsStream(BIOMES_DIR + fileName);
            var scanner = new Scanner(file);
            var content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            scanner.close();

            // only collect valid biome files
            if (content.length() > 0 && fileName.endsWith(".json")) {
                var biomeKey = "minecraft:" + fileName.substring(0, fileName.length() - 5);
                var jsonObject = gson.fromJson(content.toString(), JsonObject.class);
                biomesJson.put(biomeKey, jsonToBiome(jsonObject));
            }
        }

        return biomesJson;
    }

    private Biome jsonToBiome(JsonObject json) {
        var effectsJson = json.get("effects").getAsJsonObject();
        var effects = new BiomeSpecialEffects.Builder()
                .fogColor(effectsJson.get("fog_color").getAsInt())
                .waterColor(effectsJson.get("water_color").getAsInt())
                .waterFogColor(effectsJson.get("water_fog_color").getAsInt())
                .skyColor(effectsJson.get("sky_color").getAsInt());

        if (effectsJson.has("foliage_color")) {
            effects = effects.foliageColorOverride(effectsJson.get("foliage_color").getAsInt());
        }

        if (effectsJson.has("grass_color")) {
            effects = effects.grassColorOverride(
                    effectsJson.get("grass_color").getAsInt()
            );
        }

        if (effectsJson.has("grass_color_modifier")) {
            effects = effects.grassColorModifier(
                    BiomeSpecialEffects.GrassColorModifier.valueOf(effectsJson.get("grass_color_modifier").getAsString().toUpperCase())
            );
        }

        return new Biome.BiomeBuilder()
                .temperature(json.get("temperature").getAsFloat())
                .downfall(json.get("downfall").getAsFloat())
                .precipitation(Biome.Precipitation.valueOf(json.get("precipitation").getAsString().toUpperCase()))
                .specialEffects(effects.build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
