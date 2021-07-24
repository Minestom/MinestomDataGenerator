package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minestom.generators.common.DataGeneratorCommon;

public final class VillagerProfessionGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject villagerProfessions = new JsonObject();
        for (var entry : Registry.VILLAGER_PROFESSION.entrySet()) {
            final var location = entry.getKey().location();
            final var villagerProfession = entry.getValue();

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
