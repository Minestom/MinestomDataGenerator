package net.minestom.generators;

import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.world.level.material.MaterialColor;
import net.minestom.datagen.DataGenerator;

public final class MapColorGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject mapColors = new JsonObject();
        Map<MaterialColor, String> colors = new HashMap<>();
        try {
            List<Field> fields = Arrays.stream(MaterialColor.class.getDeclaredFields()).filter(field -> field.getType().equals(MaterialColor.class)).toList();
            for (Field f : fields) {
                f.setAccessible(true);
                MaterialColor c = (MaterialColor) f.get(null);
                colors.put(c, f.getName());
            }
        } catch (IllegalAccessException e) {
            return mapColors;
        }

        for (Map.Entry<MaterialColor, String> mc : colors.entrySet()) {
            JsonObject mapColor = new JsonObject();
            mapColor.addProperty("id", mc.getKey().id);
            mapColor.addProperty("color", mc.getKey().col);
            mapColors.add("minecraft:" + mc.getValue().toLowerCase(Locale.ROOT), mapColor);
        }
        return mapColors;
    }
}
