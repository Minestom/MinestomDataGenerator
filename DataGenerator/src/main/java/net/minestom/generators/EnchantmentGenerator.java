package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minestom.datagen.DataGenerator;

public final class EnchantmentGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject enchantments = new JsonObject();
        var registry = BuiltInRegistries.ENCHANTMENT;
        for (Enchantment enchantment : registry) {
            final var location = registry.getKey(enchantment);
            JsonObject enchantmentJson = new JsonObject();
            enchantmentJson.addProperty("id", registry.getId(enchantment));
            enchantmentJson.addProperty("translationKey", enchantment.getDescriptionId());
            enchantmentJson.addProperty("maxLevel", enchantment.getMaxLevel());
            enchantmentJson.addProperty("rarity", enchantment.getRarity().toString());
            addDefaultable(enchantmentJson, "curse", enchantment.isCurse(), false);
            addDefaultable(enchantmentJson, "discoverable", enchantment.isDiscoverable(), true);
            addDefaultable(enchantmentJson, "tradeable", enchantment.isTradeable(), true);
            addDefaultable(enchantmentJson, "treasureOnly", enchantment.isTreasureOnly(), false);
            enchantmentJson.addProperty("matchTag", enchantment.getMatch().location().toString());
            enchantments.add(location.toString(), enchantmentJson);
        }
        return enchantments;
    }
}
