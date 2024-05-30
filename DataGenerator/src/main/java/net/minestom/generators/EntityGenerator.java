package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minestom.datagen.DataGenerator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
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
        var registry = BuiltInRegistries.ENTITY_TYPE;
        for (var entityType : registry) {
            final var location = registry.getKey(entityType);
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
            entity.addProperty("id", registry.getId(entityType));
            entity.addProperty("translationKey", entityType.getDescriptionId());
            entity.addProperty("packetType", packetType);
            addDefaultable(entity, "fireImmune", entityType.fireImmune(), false);

            {   // Dimensions
                EntityDimensions dimensions = entityType.getDimensions();
                entity.addProperty("width", dimensions.width());
                entity.addProperty("height", dimensions.height());
                entity.addProperty("eyeHeight", dimensions.eyeHeight());

                // Get the defined attachment points for entities
                Map<EntityAttachment, List<Vec3>> attachments = getAttachmentMap(dimensions.attachments());
                JsonObject attachs = new JsonObject();
                for (var entry : attachments.entrySet()) {
                    List<Vec3> vecs = entry.getValue();

                    // Create the default fallback points for this attachement and exclude this entry if they are
                    // the same. No point in including this we can just recompute it.
                    var fallbacks = entry.getKey().createFallbackPoints(dimensions.width(), dimensions.height());
                    if (vecs.equals(fallbacks)) {
                        continue;
                    }

                    JsonArray points = new JsonArray();
                    for (Vec3 vec : vecs) {
                        JsonArray vecJson = new JsonArray();
                        vecJson.add(vec.x());
                        vecJson.add(vec.y());
                        vecJson.add(vec.z());
                        points.add(vecJson);
                    }
                    attachs.add(entry.getKey().name(), points);
                }
                if (!attachs.isEmpty()) {
                    entity.add("attachments", attachs);
                }
            }

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
        if (entityType == EntityType.ENDER_PEARL) return 0.03;
        if (entityType == EntityType.SNOWBALL) return 0.03;

        if (entityType == EntityType.BOAT) return 0.04;
        if (entityType == EntityType.TNT) return 0.04;
        if (entityType == EntityType.FALLING_BLOCK) return 0.04;
        if (entityType == EntityType.ITEM) return 0.04;
        if (entityType == EntityType.MINECART) return 0.04;

        if (entityType == EntityType.ARROW) return 0.05;
        if (entityType == EntityType.SPECTRAL_ARROW) return 0.05;
        if (entityType == EntityType.TRIDENT) return 0.05;
        if (entityType == EntityType.POTION) return 0.05;

        if (entityType == EntityType.LLAMA_SPIT) return 0.06;
        
        if (entityType == EntityType.EXPERIENCE_BOTTLE) return 0.07;

        if (entityType == EntityType.FIREBALL) return 0.1;
        if (entityType == EntityType.WITHER_SKULL) return 0.1;
        if (entityType == EntityType.DRAGON_FIREBALL) return 0.1;

        return DEFAULT_ACCELERATION;
    }

    private @NotNull Map<EntityAttachment, List<Vec3>> getAttachmentMap(@NotNull EntityAttachments attachments) {
        try {
            var field = EntityAttachments.class.getDeclaredField("attachments");
            field.setAccessible(true);
            return (Map<EntityAttachment, List<Vec3>>) field.get(attachments);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
