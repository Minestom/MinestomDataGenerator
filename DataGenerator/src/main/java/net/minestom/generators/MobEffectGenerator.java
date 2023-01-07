package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class MobEffectGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject effects = new JsonObject();
        var registry = BuiltInRegistries.MOB_EFFECT;
        for (var mobEffect : registry) {
            final var location = registry.getKey(mobEffect);
            JsonObject effect = new JsonObject();
            effect.addProperty("id", registry.getId(mobEffect));
            effect.addProperty("translationKey", mobEffect.getDescriptionId());
            effect.addProperty("color", mobEffect.getColor());
            effect.addProperty("instantaneous", mobEffect.isInstantenous());
            effects.add(location.toString(), effect);
        }
        return effects;
    }
}
