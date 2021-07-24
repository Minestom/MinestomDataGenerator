package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.level.material.MaterialColor;
import net.minestom.generators.common.DataGeneratorCommon;

public final class MapColorGenerator extends DataGeneratorCommon {
    @Override
    public JsonArray generate() {
        JsonArray mapColors = new JsonArray();
        for (MaterialColor mc : MaterialColor.MATERIAL_COLORS) {
            if (mc == null) {
                continue;
            }
            JsonObject mapColor = new JsonObject();
            mapColor.addProperty("id", mc.id);
            mapColor.addProperty("color", mc.col);
            mapColors.add(mapColor);
        }
        return mapColors;
    }
}
