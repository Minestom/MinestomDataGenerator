package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class PotionGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject potions = new JsonObject();
        for (var potion : Registry.POTION) {
            final var location = Registry.POTION.getKey(potion);
            JsonObject effect = new JsonObject();
            effect.addProperty("id", Registry.POTION.getId(potion));
            // TODO add effects
            potions.add(location.toString(), effect);
        }
        return potions;
    }
}
