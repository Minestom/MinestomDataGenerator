package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class VillagerProfessionGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> villagerProfessionRLs = Registry.VILLAGER_PROFESSION.keySet();
        JsonObject villagerProfessions = new JsonObject();

        for (ResourceLocation villagerProfessionRL : villagerProfessionRLs) {
            VillagerProfession vp = Registry.VILLAGER_PROFESSION.get(villagerProfessionRL);

            JsonObject villagerProfession = new JsonObject();

            villagerProfession.addProperty("id", Registry.VILLAGER_PROFESSION.getId(vp));

            SoundEvent workSound = vp.getWorkSound();
            if (workSound != null) {
                ResourceLocation workSoundRL = Registry.SOUND_EVENT.getKey(workSound);
                if (workSoundRL != null) {
                    villagerProfession.addProperty("workSound", workSoundRL.toString());
                }
            }

            villagerProfessions.add(villagerProfessionRL.toString(), villagerProfession);
        }
        return villagerProfessions;
    }
}
