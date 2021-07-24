package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minestom.generators.common.DataGeneratorCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

public final class AttributeGenerator extends DataGeneratorCommon {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeGenerator.class);

    @Override
    public JsonObject generate() {
        Set<ResourceLocation> attributeRLs = Registry.ATTRIBUTE.keySet();
        JsonObject attributes = new JsonObject();

        for (ResourceLocation attributeRL : attributeRLs) {
            Attribute a = Registry.ATTRIBUTE.get(attributeRL);

            JsonObject attribute = new JsonObject();
            if (a == null) {
                continue;
            }
            attribute.addProperty("translationKey", a.getDescriptionId());
            attribute.addProperty("defaultValue", a.getDefaultValue());
            attribute.addProperty("clientSync", a.isClientSyncable());
            if (a instanceof RangedAttribute ra) {
                // Unfortuantely get via reflection
                JsonObject range = new JsonObject();
                try {
                    Field maxV = RangedAttribute.class.getDeclaredField("maxValue");
                    maxV.setAccessible(true);
                    range.addProperty("maxValue", maxV.getDouble(ra));
                    Field minV = RangedAttribute.class.getDeclaredField("minValue");
                    minV.setAccessible(true);
                    range.addProperty("minValue", minV.getDouble(ra));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    LOGGER.error("Failed to get attribute ranges, skipping attrobite with ID '" + attributeRL + "'.", e);
                }
                attribute.add("range", range);
            }

            attributes.add(attributeRL.toString(), attribute);
        }
        return attributes;
    }
}
