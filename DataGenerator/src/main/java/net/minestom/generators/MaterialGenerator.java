package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minestom.generators.common.DataGeneratorCommon;

public final class MaterialGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject items = new JsonObject();
        for (var entry : Registry.ITEM.entrySet()) {
            final var location = entry.getKey().location();
            final var item = entry.getValue();

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", Registry.ITEM.getId(item));
            itemJson.addProperty("translationKey", item.getDescriptionId());
            itemJson.addProperty("depletes", item.canBeDepleted());
            itemJson.addProperty("maxStackSize", item.getMaxStackSize());
            itemJson.addProperty("maxDamage", item.getMaxDamage());
            itemJson.addProperty("edible", item.isEdible());
            itemJson.addProperty("fireResistant", item.isFireResistant());
            itemJson.addProperty("blockId", Registry.BLOCK.getKey(Block.byItem(item)).toString());
            ResourceLocation eatingSound = Registry.SOUND_EVENT.getKey(item.getEatingSound());
            if (eatingSound != null) {
                itemJson.addProperty("eatingSound", eatingSound.toString());
            }
            ResourceLocation drinkingSound = Registry.SOUND_EVENT.getKey(item.getDrinkingSound());
            if (drinkingSound != null) {
                itemJson.addProperty("drinkingSound", drinkingSound.toString());
            }
            // Food Properties
            if (item.getFoodProperties() != null) {
                FoodProperties fp = item.getFoodProperties();

                JsonObject foodProperties = new JsonObject();
                foodProperties.addProperty("alwaysEdible", fp.canAlwaysEat());
                foodProperties.addProperty("isFastFood", fp.isFastFood());
                foodProperties.addProperty("nutrition", fp.getNutrition());
                foodProperties.addProperty("saturationModifier", fp.getSaturationModifier());

                {
                    // Food effects
                    JsonArray effects = new JsonArray();
                    for (Pair<MobEffectInstance, Float> effect : fp.getEffects()) {
                        ResourceLocation rl = Registry.MOB_EFFECT.getKey(effect.getFirst().getEffect());
                        if (rl == null) {
                            continue;
                        }
                        JsonObject foodEffect = new JsonObject();
                        foodEffect.addProperty("id", rl.toString());
                        foodEffect.addProperty("amplifier", effect.getFirst().getAmplifier());
                        foodEffect.addProperty("duration", effect.getFirst().getDuration());
                        foodEffect.addProperty("chance", effect.getSecond());
                        effects.add(foodEffect);
                    }
                    foodProperties.add("effects", effects);
                }
                itemJson.add("foodProperties", foodProperties);
            }
            // Armor properties
            if (item instanceof ArmorItem armorItem) {
                JsonObject armorProperties = new JsonObject();
                armorProperties.addProperty("defense", armorItem.getDefense());
                armorProperties.addProperty("toughness", armorItem.getToughness());
                armorProperties.addProperty("slot", armorItem.getSlot().getName());
                itemJson.add("armorProperties", armorProperties);
            }
            // SpawnEgg properties
            if (item instanceof SpawnEggItem spawnEggItem) {
                JsonObject spawnEggProperties = new JsonObject();
                spawnEggProperties.addProperty("entityType", Registry.ENTITY_TYPE.getKey(spawnEggItem.getType(null)).toString());
                itemJson.add("spawnEggProperties", spawnEggProperties);
            }
            items.add(location.toString(), itemJson);
        }
        return items;
    }
}
