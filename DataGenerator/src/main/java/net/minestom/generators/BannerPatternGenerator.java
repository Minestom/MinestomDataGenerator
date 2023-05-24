package net.minestom.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minestom.datagen.DataGenerator;

public class BannerPatternGenerator extends DataGenerator {

    @Override
    public JsonElement generate() throws Exception {
        JsonObject patternTypes = new JsonObject();
        var registry = BuiltInRegistries.BANNER_PATTERN;
        for (BannerPattern bannerPattern : registry) {
            JsonObject pattern = new JsonObject();
            pattern.addProperty("id", registry.getId(bannerPattern));
            pattern.addProperty("identifier", bannerPattern.getHashname());
            patternTypes.add(registry.getKey(bannerPattern).toString(), pattern);
        }
        return patternTypes;
    }

}
