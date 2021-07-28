package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.generators.common.DataGeneratorCommon;

public final class CustomStatisticGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject customStatistics = new JsonObject();
        for (var entry : Registry.CUSTOM_STAT.entrySet()) {
            final var location = entry.getKey().location();
            final var stat = entry.getValue();
            JsonObject customStatistic = new JsonObject();
            customStatistic.addProperty("id", Registry.CUSTOM_STAT.getId(stat));
            customStatistics.add(location.toString(), customStatistic);
        }
        return customStatistics;
    }
}
