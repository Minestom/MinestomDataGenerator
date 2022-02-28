package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class SoundGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject sounds = new JsonObject();
        for (var soundEvent : Registry.SOUND_EVENT) {
            final var location = Registry.SOUND_EVENT.getKey(soundEvent);
            JsonObject sound = new JsonObject();
            sound.addProperty("id", Registry.SOUND_EVENT.getId(soundEvent));
            sounds.add(location.toString(), sound);
        }
        return sounds;
    }
}
