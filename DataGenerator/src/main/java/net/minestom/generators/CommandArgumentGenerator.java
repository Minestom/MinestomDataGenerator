package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class CommandArgumentGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject arguments = new JsonObject();
        var registry = BuiltInRegistries.COMMAND_ARGUMENT_TYPE;
        for (var argument : registry) {
            final var location = registry.getKey(argument);
            JsonObject object = new JsonObject();
            object.addProperty("id", registry.getId(argument));
            arguments.add(location.toString(), object);
        }
        return arguments;
    }
}
