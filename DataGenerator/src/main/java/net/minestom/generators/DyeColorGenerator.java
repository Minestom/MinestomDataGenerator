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
            dyeColor.addProperty("textureDiffuseColor", convertTextureDiffuseColors(dC.getTextureDiffuseColors()));
            dyeColor.addProperty("textColor", dC.getTextColor());
            dyeColor.addProperty("fireworkColor", dC.getFireworkColor());
            dyeColor.addProperty("mapColorId", dC.getMapColor().id);
            dyeColors.add(dyeColor);
        }
        return dyeColors;
    }


    private static int convertTextureDiffuseColors(float[] colorArray) {
        int red = Math.round(colorArray[0] * 255.0f) << 16;
        int green = Math.round(colorArray[1] * 255.0f) << 8;
        int blue = Math.round(colorArray[2] * 255.0f);
        return red + green + blue;
    }
}
