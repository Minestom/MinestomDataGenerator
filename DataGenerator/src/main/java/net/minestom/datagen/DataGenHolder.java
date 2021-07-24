package net.minestom.datagen;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public final class DataGenHolder {
    private static final Map<DataGenType, DataGenerator> generators = new HashMap<>();

    private DataGenHolder() {
    }

    public static void addGenerator(DataGenType type, DataGenerator dg) {
        generators.put(type, dg);
    }

    public static void runGenerators(JsonOutputter jsonOutputter) {
        // Run generators
        for (var entry : generators.entrySet()) {
            DataGenType type = entry.getKey();
            DataGenerator generator = entry.getValue();

            JsonElement data = generator.generate();
            jsonOutputter.output(data, type.getFileName());
        }
    }
}
