package net.minestom.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ClockTimeMarkers;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Field;

public class ClockTimeMarkerGenerator extends DataGenerator {

    @Override
    @SuppressWarnings("unchecked")
    public JsonElement generate() throws Exception {
        JsonObject markers = new JsonObject();
        for (Field declaredField : ClockTimeMarkers.class.getDeclaredFields()) {
            if (declaredField.getType() != ResourceKey.class) continue;
            ResourceKey<ClockTimeMarker> marker = (ResourceKey<ClockTimeMarker>) declaredField.get(null);
            // I'm expecting this to become a dynamic registry at some point.
            markers.add(marker.identifier().toString(), new JsonObject());
        }
        return markers;
    }
}
