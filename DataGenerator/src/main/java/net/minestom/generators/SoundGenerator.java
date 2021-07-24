package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class SoundGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> soundRLs = Registry.SOUND_EVENT.keySet();
        JsonObject sounds = new JsonObject();

        for (ResourceLocation soundRL : soundRLs) {
            SoundEvent se = Registry.SOUND_EVENT.get(soundRL);
            if (se == null) {
                continue;
            }
            JsonObject sound = new JsonObject();

            sound.addProperty("id", Registry.SOUND_EVENT.getId(se));

            sounds.add(soundRL.toString(), sound);
        }
        return sounds;
    }
}
