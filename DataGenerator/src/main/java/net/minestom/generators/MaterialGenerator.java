package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class MaterialGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> itemRLs = Registry.ITEM.keySet();
        JsonObject items = new JsonObject();

        for (ResourceLocation itemRL : itemRLs) {
            Item i = Registry.ITEM.get(itemRL);

            JsonObject item = new JsonObject();
            item.addProperty("id", Registry.ITEM.getId(i));
            item.addProperty("translationKey", i.getDescriptionId());
            item.addProperty("depletes", i.canBeDepleted());
            item.addProperty("maxStackSize", i.getMaxStackSize());
            item.addProperty("maxDamage", i.getMaxDamage());
            // item.addProperty("complex", i.isComplex()); basically useless
            item.addProperty("edible", i.isEdible());
            item.addProperty("fireResistant", i.isFireResistant());
            item.addProperty("blockId", Registry.BLOCK.getKey(Block.byItem(i)).toString());
            ResourceLocation eatingSound = Registry.SOUND_EVENT.getKey(i.getEatingSound());
            if (eatingSound != null) {
                item.addProperty("eatingSound", eatingSound.toString());
            }
            ResourceLocation drinkingSound = Registry.SOUND_EVENT.getKey(i.getDrinkingSound());
            if (drinkingSound != null) {
                item.addProperty("drinkingSound", drinkingSound.toString());
            }
            // Food Properties
            if (i.isEdible() && i.getFoodProperties() != null) {
                FoodProperties fp = i.getFoodProperties();

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
                item.add("foodProperties", foodProperties);
            }
            // Armor properties
            if (i instanceof ArmorItem) {
                ArmorItem ai = (ArmorItem) i;

                JsonObject armorProperties = new JsonObject();
                armorProperties.addProperty("defense", ai.getDefense());
                armorProperties.addProperty("toughness", ai.getToughness());
                armorProperties.addProperty("slot", ai.getSlot().getName());

                item.add("armorProperties", armorProperties);
            }
            // SpawnEgg properties
            if (i instanceof SpawnEggItem) {
                SpawnEggItem sei = (SpawnEggItem) i;

                JsonObject spawnEggProperties = new JsonObject();
                spawnEggProperties.addProperty("entityType", Registry.ENTITY_TYPE.getKey(sei.getType(null)).toString());

                item.add("spawnEggProperties", spawnEggProperties);
            }

            items.add(itemRL.toString(), item);
        }
        return items;
    }
}
