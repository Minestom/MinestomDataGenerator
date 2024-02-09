package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.material.MapColor;
import net.minestom.datagen.DataGenerator;

public final class MapColorGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        JsonArray mapColors = new JsonArray();

        Map<MapColor, String> colors = new HashMap<>();
        try {
            List<Field> fields = Arrays.stream(MapColor.class.getDeclaredFields()).filter(field -> field.getType().equals(MapColor.class)).toList();
            for (Field f : fields) {
                f.setAccessible(true);
                MapColor c = (MapColor) f.get(null);
                colors.put(c, f.getName());
            }
        } catch (IllegalAccessException e) {
            return mapColors;
        }
        for (Map.Entry<MapColor, String> entry : colors.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            JsonObject mapColor = new JsonObject();
            mapColor.addProperty("id", entry.getKey().id);
            mapColor.addProperty("color", entry.getKey().col);
            mapColor.addProperty("name", entry.getValue());
            mapColors.add(mapColor);
        }
        return mapColors;
    }
}
