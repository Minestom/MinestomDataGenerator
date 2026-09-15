package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public final class ArmorMaterialGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        var armorMaterials = new JsonArray();
        var id = 0;

        var fields = Arrays.stream(ArmorMaterials.class.getDeclaredFields())
                .filter(field -> field.getType() == ArmorMaterial.class)
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .toList();

        try {
            for (var field : fields) {
                var material = (ArmorMaterial) field.get(null);
                var materialJson = new JsonObject();
                var defense = new JsonObject();

                materialJson.addProperty("id", id++);
                materialJson.addProperty("name", field.getName());
                materialJson.addProperty("durability", material.durability());
                materialJson.addProperty("enchantmentValue", material.enchantmentValue());
                materialJson.addProperty("equipSound", material.equipSound().getRegisteredName());
                materialJson.addProperty("toughness", material.toughness());
                materialJson.addProperty("knockbackResistance", material.knockbackResistance());
                materialJson.addProperty("repairIngredient", material.repairIngredient().location().toString());
                materialJson.addProperty("assetId", material.assetId().identifier().toString());

                for (var armorType : ArmorType.values()) {
                    var value = material.defense().get(armorType);

                    if (value != null) {
                        defense.addProperty(armorType.name(), value);
                    }
                }

                materialJson.add("defense", defense);
                armorMaterials.add(materialJson);
            }
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to read vanilla armor materials", exception);
        }

        return armorMaterials;
    }
}
