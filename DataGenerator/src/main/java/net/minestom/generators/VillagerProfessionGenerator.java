package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minestom.datagen.DataGenerator;

public final class VillagerProfessionGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject villagerProfessions = new JsonObject();
        for (var villagerProfession : Registry.VILLAGER_PROFESSION) {
            final var location = Registry.VILLAGER_PROFESSION.getKey(villagerProfession);
            JsonObject villagerProfessionJson = new JsonObject();
            villagerProfessionJson.addProperty("id", Registry.VILLAGER_PROFESSION.getId(villagerProfession));
            SoundEvent workSound = villagerProfession.getWorkSound();
            if (workSound != null) {
                ResourceLocation workSoundRL = Registry.SOUND_EVENT.getKey(workSound);
                if (workSoundRL != null) {
                    villagerProfessionJson.addProperty("workSound", workSoundRL.toString());
                }
            }
            villagerProfessions.add(location.toString(), villagerProfessionJson);
        }
        return villagerProfessions;
    }
}
