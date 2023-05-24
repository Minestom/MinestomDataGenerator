package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class PotionGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject potions = new JsonObject();
        var registry = BuiltInRegistries.POTION;
        for (var potion : registry) {
            final var location = registry.getKey(potion);
            JsonObject effect = new JsonObject();
            effect.addProperty("id", registry.getId(potion));
            // TODO add effects
            potions.add(location.toString(), effect);
        }
        return potions;
    }
}
