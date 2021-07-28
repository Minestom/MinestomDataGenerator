package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minestom.datagen.DataGenerator;

public final class ParticleGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject particles = new JsonObject();
        for (var entry : Registry.PARTICLE_TYPE.entrySet()) {
            final var location = entry.getKey().location();
            final var particleType = entry.getValue();
            JsonObject particle = new JsonObject();
            particle.addProperty("id", Registry.PARTICLE_TYPE.getId(particleType));
            particles.add(location.toString(), particle);
        }
        return particles;
    }
}
