package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.generators.common.DataGeneratorCommon;

public final class EnchantmentGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject enchantments = new JsonObject();
        for (var entry : Registry.ENCHANTMENT.entrySet()) {
            final var location = entry.getKey().location();
            final var enchantment = entry.getValue();

            JsonObject enchantmentJson = new JsonObject();
            enchantmentJson.addProperty("id", Registry.ENCHANTMENT.getId(enchantment));
            enchantmentJson.addProperty("translationKey", enchantment.getDescriptionId());
            enchantmentJson.addProperty("maxLevel", enchantment.getMaxLevel());
            enchantmentJson.addProperty("minLevel", enchantment.getMinLevel());
            enchantmentJson.addProperty("rarity", enchantment.getRarity().toString());
            enchantmentJson.addProperty("curse", enchantment.isCurse());
            enchantmentJson.addProperty("discoverable", enchantment.isDiscoverable());
            enchantmentJson.addProperty("tradeable", enchantment.isTradeable());
            enchantmentJson.addProperty("treasureOnly", enchantment.isTreasureOnly());
            enchantmentJson.addProperty("category", enchantment.category.name());

            enchantments.add(location.toString(), enchantmentJson);
        }
        return enchantments;
    }
}
