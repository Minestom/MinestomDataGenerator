package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.level.material.MaterialColor;
import net.minestom.generators.common.DataGeneratorCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public final class MapColorGenerator extends DataGeneratorCommon<MaterialColor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapColorGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : MaterialColor.class.getDeclaredFields()) {
            if (declaredField.getName().equals("MATERIAL_COLORS") || declaredField.getType() != MaterialColor.class) {
                continue;
            }
            try {
                MaterialColor mc = (MaterialColor) declaredField.get(null);
                names.put(mc, declaredField.getName());
            } catch (IllegalAccessException e) {
                // Just stop I guess
                LOGGER.error("Failed to access map color naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonArray generate() {
        JsonArray mapColors = new JsonArray();

        for (MaterialColor mc : MaterialColor.MATERIAL_COLORS) {
            if (mc == null) {
                continue;
            }
            JsonObject mapColor = new JsonObject();

            mapColor.addProperty("id", mc.id);
            mapColor.addProperty("mojangName", names.get(mc));
            mapColor.addProperty("color", mc.col);

            mapColors.add(mapColor);
        }
        return mapColors;
    }
}
