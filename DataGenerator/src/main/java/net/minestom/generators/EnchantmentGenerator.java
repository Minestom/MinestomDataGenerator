package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class EnchantmentGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> enchantmentRLs = Registry.ENCHANTMENT.keySet();
        JsonObject enchantments = new JsonObject();

        for (ResourceLocation enchantmentRL : enchantmentRLs) {
            Enchantment e = Registry.ENCHANTMENT.get(enchantmentRL);
            if (e == null) {
                continue;
            }
            JsonObject enchantment = new JsonObject();

            enchantment.addProperty("id", Registry.ENCHANTMENT.getId(e));
            enchantment.addProperty("translationKey", e.getDescriptionId());
            enchantment.addProperty("maxLevel", e.getMaxLevel());
            enchantment.addProperty("minLevel", e.getMinLevel());
            enchantment.addProperty("rarity", e.getRarity().toString());
            enchantment.addProperty("curse", e.isCurse());
            enchantment.addProperty("discoverable", e.isDiscoverable());
            enchantment.addProperty("tradeable", e.isTradeable());
            enchantment.addProperty("treasureOnly", e.isTreasureOnly());
            enchantment.addProperty("category", e.category.name());

            enchantments.add(enchantmentRL.toString(), enchantment);
        }
        return enchantments;
    }
}
