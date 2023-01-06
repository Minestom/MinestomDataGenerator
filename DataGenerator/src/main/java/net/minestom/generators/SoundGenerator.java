package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class SoundGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject sounds = new JsonObject();
        var registry = BuiltInRegistries.SOUND_EVENT;
        for (var soundEvent : registry) {
            final var location = registry.getKey(soundEvent);
            JsonObject sound = new JsonObject();
            sound.addProperty("id", registry.getId(soundEvent));
            sounds.add(location.toString(), sound);
        }
        return sounds;
    }
}
