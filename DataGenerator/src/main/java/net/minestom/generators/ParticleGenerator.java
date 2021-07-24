package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minestom.generators.common.DataGeneratorCommon;

import java.util.Set;

public final class ParticleGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        Set<ResourceLocation> particleRLs = Registry.PARTICLE_TYPE.keySet();
        JsonObject particles = new JsonObject();

        for (ResourceLocation particleRL : particleRLs) {
            ParticleType<?> pt = Registry.PARTICLE_TYPE.get(particleRL);
            if (pt == null) {
                continue;
            }
            JsonObject particle = new JsonObject();

            particle.addProperty("id", Registry.PARTICLE_TYPE.getId(pt));
            particles.add(particleRL.toString(), particle);
        }
        return particles;
    }
}
