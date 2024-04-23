package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.datagen.DataGenerator;
import net.minestom.utils.ResourceUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Scanner;

public class GenericResourceGenerator extends DataGenerator {

    private static final Gson gson = new Gson();

    private final String name;
    private final List<String> exclusions;

    public GenericResourceGenerator(@NotNull String name) {
        this(name, List.of());
    }

    public GenericResourceGenerator(@NotNull String name, @NotNull List<String> exclusions) {
        this.name = "data/minecraft/" + name + "/";
        this.exclusions = exclusions;
    }

    @Override
    public JsonElement generate() throws Exception {
        var result = new JsonObject();

        // get all files from the damage types directory
        var files = ResourceUtils.getResourceListing(net.minecraft.server.MinecraftServer.class, name);

        for (String fileName : files) {
            var file = net.minecraft.server.MinecraftServer.class
                    .getClassLoader()
                    .getResourceAsStream(name + fileName);
            var scanner = new Scanner(file);
            var content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            scanner.close();

            // only collect valid files
            if (content.length() > 0 && fileName.endsWith(".json")) {
                var key = "minecraft:" + fileName.substring(0, fileName.length() - 5);
                var jsonObject = gson.fromJson(content.toString(), JsonObject.class);
                exclusions.forEach(jsonObject::remove);
                result.add(key, jsonObject);
            }
        }

        return result;
    }
}
