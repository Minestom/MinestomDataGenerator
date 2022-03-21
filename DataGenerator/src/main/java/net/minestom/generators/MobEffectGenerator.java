package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class MobEffectGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject effects = new JsonObject();
        for (var mobEffect : Registry.MOB_EFFECT) {
            final var location = Registry.MOB_EFFECT.getKey(mobEffect);
            JsonObject effect = new JsonObject();
            effect.addProperty("id", Registry.MOB_EFFECT.getId(mobEffect));
            effect.addProperty("translationKey", mobEffect.getDescriptionId());
            effect.addProperty("color", mobEffect.getColor());
            effect.addProperty("instantaneous", mobEffect.isInstantenous());
            effects.add(location.toString(), effect);
        }
        return effects;
    }
}
