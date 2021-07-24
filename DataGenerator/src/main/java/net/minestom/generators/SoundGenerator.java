package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.generators.common.DataGeneratorCommon;

public final class SoundGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject sounds = new JsonObject();
        for (var entry : Registry.SOUND_EVENT.entrySet()) {
            final var location = entry.getKey().location();
            final var soundEvent = entry.getValue();
            JsonObject sound = new JsonObject();
            sound.addProperty("id", Registry.SOUND_EVENT.getId(soundEvent));
            sounds.add(location.toString(), sound);
        }
        return sounds;
    }
}
