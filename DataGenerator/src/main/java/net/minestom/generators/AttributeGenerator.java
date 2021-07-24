package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minestom.generators.common.DataGeneratorCommon;

public final class AttributeGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject attributes = new JsonObject();
        for (var entry : Registry.ATTRIBUTE.entrySet()) {
            final var location = entry.getKey().location();
            final var attribute = entry.getValue();

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
