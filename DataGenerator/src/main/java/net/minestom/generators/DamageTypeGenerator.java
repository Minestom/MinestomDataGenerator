package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.datagen.DataGenerator;
import net.minestom.utils.ResourceUtils;

import java.util.Scanner;

public class DamageTypeGenerator extends DataGenerator {

    private static final String DAMAGE_TYPE_DIR = "data/minecraft/damage_type/";
    private static final Gson gson = new Gson();

    @Override
    public JsonElement generate() throws Exception {
        var damageTypesJson = new JsonObject();

        // get all files from the biomes directory
        var files = ResourceUtils.getResourceListing(
                net.minecraft.server.MinecraftServer.class, DAMAGE_TYPE_DIR);

        for (String fileName : files) {
            var file = net.minecraft.server.MinecraftServer.class
                    .getClassLoader()
                    .getResourceAsStream(DAMAGE_TYPE_DIR + fileName);
            var scanner = new Scanner(file);
            var content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            scanner.close();

            // only collect valid files
            if (content.length() > 0 && fileName.endsWith(".json")) {
                var biomeKey = "minecraft:" + fileName.substring(0, fileName.length() - 5);
                var jsonObject = gson.fromJson(content.toString(), JsonObject.class);
                damageTypesJson.add(biomeKey, jsonObject);
            }
        }

        return damageTypesJson;
    }
}
