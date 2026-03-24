package net.minestom.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Field;

public class GameRuleGenerator extends DataGenerator {

    @Override
    public JsonElement generate() throws Exception {
        JsonObject markers = new JsonObject();
        for (var gameRule : BuiltInRegistries.GAME_RULE) {
            var ruleJson = new JsonObject();
            ruleJson.addProperty("id", BuiltInRegistries.GAME_RULE.getId(gameRule));
            ruleJson.addProperty("default", String.valueOf(gameRule.defaultValue()));
            ruleJson.addProperty("type", gameRule.gameRuleType().getSerializedName());
            markers.add(gameRule.getIdentifier().toShortString(), ruleJson);
        }
        return markers;
    }
}
