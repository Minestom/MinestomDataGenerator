package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minestom.datagen.DataGenerator;

public final class GameEventGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject gameEvents = new JsonObject();
        var registry = BuiltInRegistries.GAME_EVENT;
        for (var gameEvent : registry) {
            final var location = registry.getKey(gameEvent);
            JsonObject gameEventJson = new JsonObject();
            gameEventJson.addProperty("id", registry.getId(gameEvent));
            gameEventJson.addProperty("notificationRadius", gameEvent.getNotificationRadius());
            gameEvents.add(location.toString(), gameEventJson);
        }
        return gameEvents;
    }
}
