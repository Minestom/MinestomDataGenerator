package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.Main;
import net.minecraft.world.level.biome.*;
import net.minestom.datagen.DataGenerator;
import net.minestom.utils.ResourceUtils;
import oshi.util.tuples.Pair;

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
            var json = entry.getValue().getA();
            JsonObject biomeJson = new JsonObject();

            biomeJson.add("temperature", json.get("temperature"));
            biomeJson.add("downfall", json.get("downfall"));
            biomeJson.add("has_precipitation", json.get("has_precipitation"));

            // Colors
            var effects = json.getAsJsonObject("effects");
            biomeJson.add("fogColor", effects.get("fog_color"));
            biomeJson.add("waterColor", effects.get("water_color"));
            biomeJson.add("waterFogColor", effects.get("water_fog_color"));
            biomeJson.add("skyColor", effects.get("sky_color"));
            biomeJson.add("foliageColor", effects.get("foliage_color"));
            biomeJson.add("grassColor", effects.get("grass_color"));
            biomesJson.add(entry.getKey(), biomeJson);
        }

        return biomesJson;
    }

    private Map<String, Pair<JsonObject, Biome>> readBiomes() throws URISyntaxException, IOException {
        // get all files from the biomes directory
        var files = ResourceUtils.getResourceListing(
                net.minecraft.server.MinecraftServer.class, BIOMES_DIR);

        Map<String, Pair<JsonObject, Biome>> biomesJson = new HashMap<>();
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
                biomesJson.put(biomeKey, new Pair<>(jsonObject, jsonToBiome(jsonObject)));
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
                .specialEffects(effects.build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
