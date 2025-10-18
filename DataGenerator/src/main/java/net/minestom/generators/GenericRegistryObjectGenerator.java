package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;
import org.jetbrains.annotations.NotNull;

public class GenericRegistryObjectGenerator<T> extends DataGenerator {
    private final Registry<T> registry;

    public GenericRegistryObjectGenerator(@NotNull Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public JsonObject generate() {
        JsonObject output = new JsonObject();

        for (T entry : registry) {
            JsonObject result = new JsonObject();

            result.addProperty("id", registry.getId(entry));
            appendEntry(result, entry);

            //noinspection DataFlowIssue We got `entry` from the registry, it is not null.
            output.add(registry.getKey(entry).toString(), result);
        }

        return output;
    }

    protected void appendEntry(JsonObject object, T entry) {
    }
}
