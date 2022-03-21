package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class CustomStatisticGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject customStatistics = new JsonObject();
        for (var stat : Registry.CUSTOM_STAT) {
            final var location = Registry.CUSTOM_STAT.getKey(stat);
            JsonObject customStatistic = new JsonObject();
            customStatistic.addProperty("id", Registry.CUSTOM_STAT.getId(stat));
            customStatistics.add(location.toString(), customStatistic);
        }
        return customStatistics;
    }
}
