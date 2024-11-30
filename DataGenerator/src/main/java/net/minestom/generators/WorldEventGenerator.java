package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.block.LevelEvent;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Modifier;

public final class WorldEventGenerator extends DataGenerator {
    @Override
    public JsonArray generate() {
        JsonArray worldEvents = new JsonArray();

        try {
            for (var field : LevelEvent.class.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) continue;
                if (!field.getType().equals(int.class)) continue;
                if (!field.getName().equals(field.getName().toUpperCase())) continue;

                int id = field.getInt(null);
                String name = field.getName().toLowerCase();
                JsonObject worldEvent = new JsonObject();
                worldEvent.addProperty("name", name);
                worldEvent.addProperty("id", id);
                worldEvents.add(worldEvent);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return worldEvents;
    }
}
