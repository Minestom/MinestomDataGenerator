package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minestom.datagen.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public final class EntityAttributeGenerator extends DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityAttributeGenerator.class);

    @Override
    public JsonObject generate() {
        JsonObject entityAttributes = new JsonObject();
        var entityRegistry = BuiltInRegistries.ENTITY_TYPE;

        for (var entityType : entityRegistry) {
            if (!DefaultAttributes.hasSupplier(entityType)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            EntityType<? extends LivingEntity> livingEntityType = (EntityType<? extends LivingEntity>) entityType;
            AttributeSupplier supplier = DefaultAttributes.getSupplier(livingEntityType);

            if (supplier == null) {
                continue;
            }

            // AttributeSupplier hides its instances map; use reflection to fetch it
            Map<Holder<Attribute>, AttributeInstance> instances = getAttributeInstances(supplier);

            if (instances == null || instances.isEmpty()) {
                continue;
            }

            JsonObject attributes = new JsonObject();

            for (Map.Entry<Holder<Attribute>, AttributeInstance> entry : instances.entrySet()) {
                Holder<Attribute> attributeHolder = entry.getKey();
                AttributeInstance instance = entry.getValue();
                String attributeKey = attributeHolder.getRegisteredName();
                double baseValue = instance.getBaseValue();
                attributes.addProperty(attributeKey, baseValue);
            }

            if (!attributes.isEmpty()) {
                final var location = entityRegistry.getKey(entityType);
                entityAttributes.add(location.toString(), attributes);
            }
        }

        return entityAttributes;
    }

    @SuppressWarnings("unchecked")
    private Map<Holder<Attribute>, AttributeInstance> getAttributeInstances(AttributeSupplier supplier) {
        try {
            Field instancesField = AttributeSupplier.class.getDeclaredField("instances");
            instancesField.setAccessible(true);
            return (Map<Holder<Attribute>, AttributeInstance>) instancesField.get(supplier);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Failed to access instances field from AttributeSupplier", e);
            return null;
        }
    }
}
