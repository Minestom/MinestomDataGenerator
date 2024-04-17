package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Instrument;
import net.minestom.datagen.DataGenerator;

public final class InstrumentGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject instruments = new JsonObject();

        Registry<Instrument> registry = BuiltInRegistries.INSTRUMENT;
        for (Instrument instrument : registry) {
            instruments.add(registry.getKey(instrument).toString(), new JsonObject());
        }

        return instruments;
    }
}
