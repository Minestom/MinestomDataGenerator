package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class MobEffectGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject effects = new JsonObject();
        for (var entry : Registry.MOB_EFFECT.entrySet()) {
            final var location = entry.getKey().location();
            final var mobEffect = entry.getValue();
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
