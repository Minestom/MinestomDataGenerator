package net.minestom.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minestom.datagen.DataGenerator;

public class BannerPatternGenerator extends DataGenerator {

    @Override
    public JsonElement generate() throws Exception {
        JsonObject patternTypes = new JsonObject();
        for (BannerPattern bannerPattern : Registry.BANNER_PATTERN) {
            JsonObject pattern = new JsonObject();
            pattern.addProperty("id", Registry.BANNER_PATTERN.getId(bannerPattern));
            pattern.addProperty("identifier", bannerPattern.getHashname());
            patternTypes.add(Registry.BANNER_PATTERN.getKey(bannerPattern).toString(), pattern);
        }
        return patternTypes;
    }

}
