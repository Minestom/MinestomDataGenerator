package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.equipment.ArmorType;
import net.minestom.datagen.DataGenerator;

public final class ArmorTypeGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        var armorTypes = new JsonArray();
        var id = 0;

        for (var armorType : ArmorType.values()) {
            var armorTypeJson = new JsonObject();
            armorTypeJson.addProperty("id", id++);
            armorTypeJson.addProperty("name", armorType.name());
            armorTypeJson.addProperty("serializedName", armorType.getName());
            armorTypeJson.addProperty("slot", armorType.getSlot().getName());
            armorTypeJson.addProperty("unitDurability", armorType.getDurability(1));
            armorTypes.add(armorTypeJson);
        }

        return armorTypes;
    }
}
