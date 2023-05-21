package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class CustomStatisticGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject customStatistics = new JsonObject();
        var registry = BuiltInRegistries.CUSTOM_STAT;
        for (var stat : registry) {
            final var location = registry.getKey(stat);
            JsonObject customStatistic = new JsonObject();
            customStatistic.addProperty("id", registry.getId(stat));
            customStatistics.add(location.toString(), customStatistic);
        }
        return customStatistics;
    }
}
