package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minestom.datagen.DataGenerator;

public final class MaterialGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject items = new JsonObject();
        var registry = BuiltInRegistries.ITEM;
        var blockRegistry = BuiltInRegistries.BLOCK;
        var soundEventRegistry = BuiltInRegistries.SOUND_EVENT;
        var mobEffectRegistry = BuiltInRegistries.MOB_EFFECT;
        var entityTypeRegistry = BuiltInRegistries.ENTITY_TYPE;
        for (var item : registry) {
            final var location = registry.getKey(item);

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", registry.getId(item));
            itemJson.addProperty("translationKey", item.getDescriptionId());
            if (item.getMaxStackSize() != 64) { // Default = 64
                itemJson.addProperty("maxStackSize", item.getMaxStackSize());
            }
            if (item.getMaxDamage() != 0) { // Default = 0 (no durability)
                itemJson.addProperty("maxDamage", item.getMaxDamage());
            }
            if (item.isFireResistant()) { // Default = false
                itemJson.addProperty("fireResistant", true);
            }
            // Corresponding block
            Block block = Block.byItem(item);
            if (block != Blocks.AIR) { // Default = no block
                itemJson.addProperty("correspondingBlock", blockRegistry.getKey(block).toString());
            }
            // Food properties
            if (item.isEdible()) { // Default = false (not edible)
                itemJson.addProperty("edible", true);
                ResourceLocation eatingSound = soundEventRegistry.getKey(item.getEatingSound());
                assert eatingSound != null;
                itemJson.addProperty("eatingSound", eatingSound.toString());
                ResourceLocation drinkingSound = soundEventRegistry.getKey(item.getDrinkingSound());
                assert drinkingSound != null;
                itemJson.addProperty("drinkingSound", drinkingSound.toString());

                FoodProperties foodProperties = item.getFoodProperties();
                if (foodProperties != null) {
                    JsonObject foodPropertiesJson = new JsonObject();
                    foodPropertiesJson.addProperty("alwaysEdible", foodProperties.canAlwaysEat());
                    foodPropertiesJson.addProperty("isFastFood", foodProperties.isFastFood());
                    foodPropertiesJson.addProperty("nutrition", foodProperties.getNutrition());
                    foodPropertiesJson.addProperty("saturationModifier", foodProperties.getSaturationModifier());
                    {
                        // Food effects
                        JsonArray effects = new JsonArray();
                        for (Pair<MobEffectInstance, Float> effectEntry : foodProperties.getEffects()) {
                            final var effect = effectEntry.getFirst();
                            final var chance = effectEntry.getSecond();
                            ResourceLocation rl = mobEffectRegistry.getKey(effect.getEffect());
                            if (rl == null) {
                                continue;
                            }
                            JsonObject foodEffect = new JsonObject();
                            foodEffect.addProperty("id", rl.toString());
                            foodEffect.addProperty("amplifier", effect.getAmplifier());
                            foodEffect.addProperty("duration", effect.getDuration());
                            foodEffect.addProperty("chance", chance);
                            effects.add(foodEffect);
                        }
                        foodPropertiesJson.add("effects", effects);
                    }
                    itemJson.add("foodProperties", foodPropertiesJson);
                }
            }
            // Armor properties
            if (item instanceof ArmorItem armorItem) {
                JsonObject armorProperties = new JsonObject();
                armorProperties.addProperty("defense", armorItem.getDefense());
                armorProperties.addProperty("toughness", armorItem.getToughness());
                armorProperties.addProperty("slot", armorItem.getEquipmentSlot().getName());
                itemJson.add("armorProperties", armorProperties);
            }
            // SpawnEgg properties
            if (item instanceof SpawnEggItem spawnEggItem) {
                JsonObject spawnEggProperties = new JsonObject();
                spawnEggProperties.addProperty("entityType", entityTypeRegistry.getKey(spawnEggItem.getType(null)).toString());
                itemJson.add("spawnEggProperties", spawnEggProperties);
            }
            items.add(location.toString(), itemJson);
        }
        return items;
    }
}
