package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.Tag;
import net.minestom.datagen.DataGenerator;
import net.minestom.utils.ResourceUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class GenericResourceGenerator extends DataGenerator {

    private static final Gson gson = new Gson();

    private final String name;
    private final List<String> exclusions;
    private final boolean snbt;

    public GenericResourceGenerator(@NotNull String name) {
        this(name, List.of(), false);
    }

    public GenericResourceGenerator(@NotNull String name, @NotNull List<String> exclusions, boolean snbt) {
        this.name = "data/minecraft/" + name + "/";
        this.exclusions = exclusions;
        this.snbt = snbt;
    }

    @Override
    public Object generate() throws Exception {
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

        if (snbt) {
            Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, result);
            return new SnbtPrinterTagVisitor("    ", 0, new ArrayList<>()).visit(tag);
        }
        return result;
    }


    private @Nullable Function<JsonObject, JsonObject> transformer() {
        if (name.contains("enchantment")) {
            return obj -> {
                JsonObject result = new JsonObject();
                Tag tag = Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, obj);
                var snbt = new SnbtPrinterTagVisitor("", 0, new ArrayList<>()).visit(tag);
                result.addProperty("raw", snbt);
                return result;
            };
        } else return null;
    }
}
