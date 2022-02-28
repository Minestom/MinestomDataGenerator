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

    private static final double DEFAULT_DRAG = 0.02;
    private static final double DEFAULT_ACCELERATION = 0.08;

    @Override
    public JsonObject generate() {
        Map<EntityType<?>, Class<?>> entityClasses = new HashMap<>();
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
        for (var entityType : Registry.ENTITY_TYPE) {
            final var location = Registry.ENTITY_TYPE.getKey(entityType);
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
            addDefaultable(entity, "fireImmune", entityType.fireImmune(), false);
            entity.addProperty("height", entityType.getHeight());
            entity.addProperty("width", entityType.getWidth());
            addDefaultable(entity, "drag", findDrag(entityType), DEFAULT_DRAG);
            addDefaultable(entity, "acceleration", findAcceleration(entityType), DEFAULT_ACCELERATION);
            entity.addProperty("clientTrackingRange", entityType.clientTrackingRange());
            entities.add(location.toString(), entity);
        }
        return entities;
    }

    private double findDrag(EntityType<?> entityType) {
        if (entityType == EntityType.BOAT) return 0;

        if (entityType == EntityType.LLAMA_SPIT) return 0.01;
        if (entityType == EntityType.ENDER_PEARL) return 0.01;
        if (entityType == EntityType.POTION) return 0.01;
        if (entityType == EntityType.SNOWBALL) return 0.01;
        if (entityType == EntityType.EGG) return 0.01;
        if (entityType == EntityType.TRIDENT) return 0.01;
        if (entityType == EntityType.SPECTRAL_ARROW) return 0.01;
        if (entityType == EntityType.ARROW) return 0.01;

        if (entityType == EntityType.MINECART) return 0.05;

        if (entityType == EntityType.FISHING_BOBBER) return 0.08;

        return DEFAULT_DRAG;
    }

    private double findAcceleration(EntityType<?> entityType) {
        if (entityType == EntityType.ITEM_FRAME) return 0;

        if (entityType == EntityType.EGG) return 0.03;
        if (entityType == EntityType.FISHING_BOBBER) return 0.03;
        if (entityType == EntityType.EXPERIENCE_BOTTLE) return 0.03;
        if (entityType == EntityType.ENDER_PEARL) return 0.03;
        if (entityType == EntityType.POTION) return 0.03;
        if (entityType == EntityType.SNOWBALL) return 0.03;

        if (entityType == EntityType.BOAT) return 0.04;
        if (entityType == EntityType.TNT) return 0.04;
        if (entityType == EntityType.FALLING_BLOCK) return 0.04;
        if (entityType == EntityType.ITEM) return 0.04;
        if (entityType == EntityType.MINECART) return 0.04;

        if (entityType == EntityType.ARROW) return 0.05;
        if (entityType == EntityType.SPECTRAL_ARROW) return 0.05;
        if (entityType == EntityType.TRIDENT) return 0.05;

        if (entityType == EntityType.LLAMA_SPIT) return 0.06;

        if (entityType == EntityType.FIREBALL) return 0.1;
        if (entityType == EntityType.WITHER_SKULL) return 0.1;
        if (entityType == EntityType.DRAGON_FIREBALL) return 0.1;

        return DEFAULT_ACCELERATION;
    }
}
