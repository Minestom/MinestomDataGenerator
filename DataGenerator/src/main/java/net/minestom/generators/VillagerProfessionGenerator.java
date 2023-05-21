package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minestom.datagen.DataGenerator;

public final class VillagerProfessionGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject villagerProfessions = new JsonObject();
        var registry = BuiltInRegistries.VILLAGER_PROFESSION;
        var soundEventRegistry = BuiltInRegistries.SOUND_EVENT;
        for (var villagerProfession : registry) {
            final var location = registry.getKey(villagerProfession);
            JsonObject villagerProfessionJson = new JsonObject();
            villagerProfessionJson.addProperty("id", registry.getId(villagerProfession));
            SoundEvent workSound = villagerProfession.workSound();
            if (workSound != null) {
                ResourceLocation workSoundRL = soundEventRegistry.getKey(workSound);
                if (workSoundRL != null) {
                    villagerProfessionJson.addProperty("workSound", workSoundRL.toString());
                }
            }
            villagerProfessions.add(location.toString(), villagerProfessionJson);
        }
        return villagerProfessions;
    }
}
