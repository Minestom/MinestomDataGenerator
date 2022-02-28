package net.minestom.generators;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minestom.datagen.DataGenerator;

public final class DimensionTypeGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        Registry<DimensionType> dimensionTypeRegistry = RegistryAccess.builtinCopy()
                .ownedRegistry(Registry.DIMENSION_TYPE_REGISTRY).orElseThrow();
        JsonObject dimensionTypes = new JsonObject();
        for (var dimensionType : dimensionTypeRegistry) {
            final var location = dimensionTypeRegistry.getKey(dimensionType);
            JsonObject dimensionTypeJson = new JsonObject();
            dimensionTypeJson.addProperty("bedWorks", dimensionType.bedWorks());
            dimensionTypeJson.addProperty("coordinateScale", dimensionType.coordinateScale());
            dimensionTypeJson.addProperty("ceiling", dimensionType.hasCeiling());
            dimensionTypeJson.addProperty("fixedTime", dimensionType.hasFixedTime());
            dimensionTypeJson.addProperty("raids", dimensionType.hasRaids());
            dimensionTypeJson.addProperty("skyLight", dimensionType.hasSkyLight());
            dimensionTypeJson.addProperty("piglinSafe", dimensionType.piglinSafe());
            dimensionTypeJson.addProperty("logicalHeight", dimensionType.logicalHeight());
            dimensionTypeJson.addProperty("natural", dimensionType.natural());
            dimensionTypeJson.addProperty("ultraWarm", dimensionType.ultraWarm());
            dimensionTypeJson.addProperty("respawnAnchorWorks", dimensionType.respawnAnchorWorks());
            dimensionTypeJson.addProperty("minY", dimensionType.minY());
            dimensionTypeJson.addProperty("height", dimensionType.height());
            dimensionTypes.add(location.toString(), dimensionTypeJson);
        }
        return dimensionTypes;
    }
}
