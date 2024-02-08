package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minestom.datagen.DataGenerator;

public final class AttributeGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject attributes = new JsonObject();
        var registry = BuiltInRegistries.ATTRIBUTE;
        for (var attribute : registry) {
            final var location = registry.getKey(attribute);
            JsonObject attributeJson = new JsonObject();
            attributeJson.addProperty("translationKey", attribute.getDescriptionId());
            attributeJson.addProperty("defaultValue", attribute.getDefaultValue());
            attributeJson.addProperty("clientSync", attribute.isClientSyncable());
            if (attribute instanceof RangedAttribute rangedAttribute) {
                JsonObject range = new JsonObject();
                range.addProperty("maxValue", rangedAttribute.getMaxValue());
                range.addProperty("minValue", rangedAttribute.getMinValue());
                attributeJson.add("range", range);
            }
            attributes.add(location.toString(), attributeJson);
        }
        return attributes;
    }
}
