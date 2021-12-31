package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import net.minecraft.world.level.material.MaterialColor;
import net.minestom.datagen.DataGenerator;

public final class MapColorGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        JsonArray mapColors = new JsonArray();
        MaterialColor[] colors;
        try {
            Field f = MaterialColor.class.getDeclaredField("MATERIAL_COLORS");
            f.setAccessible(true);
            colors = (MaterialColor[]) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e ) {
            return mapColors;
        }

        for (MaterialColor mc : colors) {
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
