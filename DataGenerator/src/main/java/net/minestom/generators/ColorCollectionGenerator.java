package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minestom.datagen.DataGenerator;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public final class ColorCollectionGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        var collections = new JsonObject();
        collections.add("blocks", generateCollections(BuiltInRegistries.BLOCK));
        collections.add("items", generateCollections(BuiltInRegistries.ITEM));
        return collections;
    }

    private <T> JsonArray generateCollections(Registry<T> registry) {
        var candidates = new TreeMap<String, Map<String, Identifier>>();

        for (var identifier : registry.keySet()) {
            for (var color : DyeColor.values()) {
                var prefix = color.getName() + "_";
                if (!identifier.getPath().startsWith(prefix)) continue;

                var alias = identifier.getPath().substring(prefix.length());
                candidates.computeIfAbsent(alias, ignored -> new TreeMap<>()).put(color.name(), identifier);
                break;
            }
        }

        var collections = new JsonArray();

        for (var entry : candidates.entrySet()) {
            var alias = entry.getKey();
            var values = entry.getValue();

            if (values.size() != DyeColor.values().length) {
                continue;
            }

            var collection = new JsonObject();
            collection.addProperty("name", alias.toUpperCase(Locale.ROOT).replace('-', '_'));
            var valueJson = new JsonObject();
            values.forEach((color, identifier) -> valueJson.addProperty(color, identifier.toString()));
            collection.add("values", valueJson);
            collections.add(collection);
        }

        return collections;
    }
}
