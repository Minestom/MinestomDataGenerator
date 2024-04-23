package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class EnchantmentGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject enchantments = new JsonObject();
        var registry = BuiltInRegistries.ENCHANTMENT;
        for (var enchantment : registry) {
            final var location = registry.getKey(enchantment);
            JsonObject enchantmentJson = new JsonObject();
            enchantmentJson.addProperty("id", registry.getId(enchantment));
            enchantmentJson.addProperty("translationKey", enchantment.getDescriptionId());
            enchantmentJson.addProperty("maxLevel", enchantment.getMaxLevel());
            addDefaultable(enchantmentJson, "curse", enchantment.isCurse(), false);
            addDefaultable(enchantmentJson, "discoverable", enchantment.isDiscoverable(), true);
            addDefaultable(enchantmentJson, "tradeable", enchantment.isTradeable(), true);
            addDefaultable(enchantmentJson, "treasureOnly", enchantment.isTreasureOnly(), false);
            enchantments.add(location.toString(), enchantmentJson);
        }
        return enchantments;
    }
}
