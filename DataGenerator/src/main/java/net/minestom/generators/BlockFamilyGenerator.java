package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minestom.datagen.DataGenerator;

import java.util.Comparator;

public final class BlockFamilyGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        var families = new JsonArray();

        for (var family : BlockFamilies.getAllFamilies()
                .sorted(Comparator.comparingInt(family -> BuiltInRegistries.BLOCK.getId(family.getBaseBlock())))
                .toList()) {
            var base = BuiltInRegistries.BLOCK.getKey(family.getBaseBlock());
            var familyJson = new JsonObject();

            familyJson.addProperty("id", BuiltInRegistries.BLOCK.getId(family.getBaseBlock()));
            familyJson.addProperty("name", base.toString());

            var variants = new JsonObject();

            for (var entry : family.getVariants().entrySet().stream()
                    .sorted(Comparator.comparingInt(entry -> entry.getKey().ordinal()))
                    .toList()) {
                variants.addProperty(entry.getKey().name(), BuiltInRegistries.BLOCK.getKey(entry.getValue()).toString());
            }

            familyJson.add("variants", variants);
            families.add(familyJson);
        }

        return families;
    }
}
