package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class MobEffectGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> effectRLs = Registry.MOB_EFFECT.keySet();
        JsonObject effects = new JsonObject();

        for (ResourceLocation effectRL : effectRLs) {
            MobEffect me = Registry.MOB_EFFECT.get(effectRL);

            JsonObject effect = new JsonObject();
            // Null safety check.
            if (me == null) {
                continue;
            }
            effect.addProperty("id", Registry.MOB_EFFECT.getId(me));
            effect.addProperty("translationKey", me.getDescriptionId());
            effect.addProperty("color", me.getColor());
            effect.addProperty("instantaneous", me.isInstantenous());

            effects.add(effectRL.toString(), effect);
        }
        return effects;
    }
}
