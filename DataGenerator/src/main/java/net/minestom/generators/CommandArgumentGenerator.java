package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class CommandArgumentGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject arguments = new JsonObject();
        for (var argument : Registry.COMMAND_ARGUMENT_TYPE) {
            final var location = Registry.COMMAND_ARGUMENT_TYPE.getKey(argument);
            JsonObject object = new JsonObject();
            object.addProperty("id", Registry.COMMAND_ARGUMENT_TYPE.getId(argument));
            arguments.add(location.toString(), object);
        }
        return arguments;
    }
}
