package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ToolMaterial;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public final class ToolMaterialGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        var toolMaterials = new JsonArray();
        var id = 0;

        var fields = Arrays.stream(ToolMaterial.class.getDeclaredFields())
                .filter(field -> field.getType() == ToolMaterial.class)
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .toList();

        try {
            for (var field : fields) {
                var material = (ToolMaterial) field.get(null);
                var materialJson = new JsonObject();

                materialJson.addProperty("id", id++);
                materialJson.addProperty("name", field.getName());
                materialJson.addProperty("incorrectBlocksForDrops", material.incorrectBlocksForDrops().location().toString());
                materialJson.addProperty("durability", material.durability());
                materialJson.addProperty("speed", material.speed());
                materialJson.addProperty("attackDamageBonus", material.attackDamageBonus());
                materialJson.addProperty("enchantmentValue", material.enchantmentValue());
                materialJson.addProperty("repairItems", material.repairItems().location().toString());

                toolMaterials.add(materialJson);
            }
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to read vanilla tool materials", exception);
        }

        return toolMaterials;
    }
}
