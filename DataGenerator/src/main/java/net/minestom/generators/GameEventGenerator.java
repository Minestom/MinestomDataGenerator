package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class GameEventGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> gameEventRLs = Registry.GAME_EVENT.keySet();
        JsonObject gameEvents = new JsonObject();

        for (ResourceLocation gameEventRL : gameEventRLs) {
            GameEvent ge = Registry.GAME_EVENT.get(gameEventRL);
            JsonObject gameEvent = new JsonObject();

            gameEvent.addProperty("id", Registry.GAME_EVENT.getId(ge));
            gameEvent.addProperty("notificationRadius", ge.getNotificationRadius());
            gameEvents.add(gameEventRL.toString(), gameEvent);
        }
        return gameEvents;
    }
}
