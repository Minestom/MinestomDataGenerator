package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minestom.datagen.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public final class EntityGenerator extends DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityGenerator.class);
    private static final Map<EntityType<?>, Class<?>> entityClasses = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject generate() {
        for (Field declaredField : EntityType.class.getDeclaredFields()) {
            if (!EntityType.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                EntityType<?> et = (EntityType<?>) declaredField.get(null);
                // Field name
                entityClasses.put(et, (Class<?>) ((ParameterizedType) declaredField.getGenericType()).getActualTypeArguments()[0]);
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map entity naming system.", e);
            }
        }
        JsonObject entities = new JsonObject();
        for (var entry : Registry.ENTITY_TYPE.entrySet()) {
            final var location = entry.getKey().location();
            final var entityType = entry.getValue();

            // Complicated but we need to get the Entity class of EntityType.
            // E.g. EntityType<T> we need to get T and check what classes T implements.
            final Class<?> entityClass = entityClasses.get(entityType);
            String packetType;
            if (Player.class.isAssignableFrom(entityClass)) {
                packetType = "PLAYER";
            } else if (LivingEntity.class.isAssignableFrom(entityClass)) {
                packetType = "LIVING";
            } else if (Painting.class.isAssignableFrom(entityClass)) {
                packetType = "PAINTING";
            } else if (ExperienceOrb.class.isAssignableFrom(entityClass)) {
                packetType = "EXPERIENCE_ORB";
            } else {
                packetType = "BASE";
            }

            JsonObject entity = new JsonObject();
            entity.addProperty("id", Registry.ENTITY_TYPE.getId(entityType));
            entity.addProperty("translationKey", entityType.getDescriptionId());
            entity.addProperty("packetType", packetType);
            entity.addProperty("fireImmune", entityType.fireImmune());
            entity.addProperty("height", entityType.getHeight());
            entity.addProperty("width", entityType.getWidth());
            entity.addProperty("clientTrackingRange", entityType.clientTrackingRange());
            entities.add(location.toString(), entity);
        }
        return entities;
    }
}
