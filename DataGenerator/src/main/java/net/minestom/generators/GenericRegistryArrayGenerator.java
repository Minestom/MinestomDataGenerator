package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;
import org.jetbrains.annotations.NotNull;

public class GenericRegistryArrayGenerator<T> extends DataGenerator {
    private final Registry<T> registry;

    public GenericRegistryArrayGenerator(@NotNull Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public JsonArray generate() {
        JsonArray output = new JsonArray();

        for (T entry : registry) {
            JsonObject result = new JsonObject();

            result.addProperty("id", registry.getId(entry));
            //noinspection DataFlowIssue We got `entry` from the registry, it is not null.
            result.addProperty("name", registry.getKey(entry).toString());
            appendEntry(result, entry);

            output.add(result);
        }

        return output;
    }

    protected void appendEntry(JsonObject object, T entry) {
    }
}
