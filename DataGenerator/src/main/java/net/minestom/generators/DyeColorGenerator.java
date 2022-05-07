package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.DyeColor;
import net.minestom.datagen.DataGenerator;

public final class DyeColorGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        JsonArray dyeColors = new JsonArray();

        for (DyeColor dC : DyeColor.values()) {
            JsonObject dyeColor = new JsonObject();
            dyeColor.addProperty("id", dC.getId());
            dyeColor.addProperty("name", dC.name());
            dyeColor.addProperty("textColor", dC.getTextColor());
            dyeColor.addProperty("fireworkColor", dC.getFireworkColor());
            dyeColor.addProperty("mapColor", dC.getMaterialColor().id);
        }
        return dyeColors;
    }
}
