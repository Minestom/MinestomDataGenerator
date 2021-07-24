package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class PotionGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> effectRLs = Registry.POTION.keySet();
        JsonObject potions = new JsonObject();

        for (ResourceLocation effectRL : effectRLs) {
            Potion p = Registry.POTION.get(effectRL);

            JsonObject effect = new JsonObject();

            potions.add(effectRL.toString(), effect);
        }
        return potions;
    }
}
