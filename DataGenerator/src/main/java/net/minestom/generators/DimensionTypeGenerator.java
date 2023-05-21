package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minestom.datagen.DataGenerator;
import net.minestom.utils.ResourceUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class DimensionTypeGenerator extends DataGenerator {

    private static final String DIMENSION_TYPES_DIR = "data/minecraft/dimension_type/";
    private static final Gson gson = new Gson();

    @Override
    public JsonObject generate() throws Exception {
        var dimensionTypesJson = new JsonObject();
        var dimensionTypes = readDimensionTypes();
        for (var entry : dimensionTypes.entrySet()) {
            dimensionTypesJson.add(entry.getKey(), entry.getValue());
        }
        return dimensionTypesJson;
    }

    private Map<String, JsonObject> readDimensionTypes() throws URISyntaxException, IOException {
        // get all files from the dimension types directory
        var files = ResourceUtils.getResourceListing(
                net.minecraft.server.MinecraftServer.class, DIMENSION_TYPES_DIR);

        Map<String, JsonObject> dimensionTypesJson = new HashMap<>();
        for (String fileName : files) {
            var file = net.minecraft.server.MinecraftServer.class
                    .getClassLoader()
                    .getResourceAsStream(DIMENSION_TYPES_DIR + fileName);
            var scanner = new Scanner(file);
            var content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            scanner.close();

            // only collect valid dimension type files
            if (content.length() > 0 && fileName.endsWith(".json")) {
                var biomeKey = "minecraft:" + fileName.substring(0, fileName.length() - 5);
                var json = gson.fromJson(content.toString(), JsonObject.class);
                var dimensionType = new JsonObject();
                dimensionType.addProperty("bedWorks", json.get("bed_works").getAsBoolean());
                dimensionType.addProperty("coordinateScale", json.get("coordinate_scale").getAsDouble());
                dimensionType.addProperty("ceiling", json.get("has_ceiling").getAsBoolean());
                dimensionType.addProperty("fixedTime", json.has("fixed_time"));
                dimensionType.addProperty("raids", json.get("has_raids").getAsBoolean());
                dimensionType.addProperty("skyLight", json.get("has_skylight").getAsBoolean());
                dimensionType.addProperty("piglinSafe", json.get("piglin_safe").getAsBoolean());
                dimensionType.addProperty("logicalHeight", json.get("logical_height").getAsInt());
                dimensionType.addProperty("natural", json.get("natural").getAsBoolean());
                dimensionType.addProperty("ultraWarm", json.get("ultrawarm").getAsBoolean());
                dimensionType.addProperty("respawnAnchorWorks", json.get("respawn_anchor_works").getAsBoolean());
                dimensionType.addProperty("minY", json.get("min_y").getAsInt());
                dimensionType.addProperty("height", json.get("height").getAsInt());
                dimensionTypesJson.put(biomeKey, dimensionType);
            }
        }

        return dimensionTypesJson;
    }
}
