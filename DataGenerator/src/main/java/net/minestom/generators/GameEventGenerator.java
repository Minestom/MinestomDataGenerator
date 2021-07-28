package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class GameEventGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject gameEvents = new JsonObject();
        for (var entry : Registry.GAME_EVENT.entrySet()) {
            final var location = entry.getKey().location();
            final var gameEvent = entry.getValue();
            JsonObject gameEventJson = new JsonObject();
            gameEventJson.addProperty("id", Registry.GAME_EVENT.getId(gameEvent));
            gameEventJson.addProperty("notificationRadius", gameEvent.getNotificationRadius());
            gameEvents.add(location.toString(), gameEventJson);
        }
        return gameEvents;
    }
}
