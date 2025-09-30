package net.minestom.generators;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minestom.datagen.DataGenerator;
import org.jetbrains.annotations.NotNull;

public final class MaterialGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject items = new JsonObject();
        var registry = BuiltInRegistries.ITEM;
        var blockRegistry = BuiltInRegistries.BLOCK;
        var entityTypeRegistry = BuiltInRegistries.ENTITY_TYPE;

        var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var registryJsonOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        for (var item : registry) {
            final var location = registry.getKey(item);

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", registry.getId(item));
            itemJson.addProperty("translationKey", item.getDescriptionId());

            // Component prototype
            var components = new JsonObject();
            for (var component : item.components()) {
                var key = Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, component.type());
                components.add(key, unwrap(component.encodeValue(registryJsonOps)));
            }
            itemJson.add("components", components);

            // Corresponding block
            Block block = Block.byItem(item);
            if (block != Blocks.AIR) { // Default = no block
                itemJson.addProperty("correspondingBlock", blockRegistry.getKey(block).toString());
            }

            // Spawn egg properties
            if (item instanceof SpawnEggItem spawnEggItem) {
                JsonObject spawnEggProperties = new JsonObject();
                spawnEggProperties.addProperty("entityType", entityTypeRegistry.getKey(spawnEggItem.getType(ItemStack.EMPTY)).toString());
                itemJson.add("spawnEggProperties", spawnEggProperties);
            }

            items.add(location.toString(), itemJson);
        }
        return items;
    }

    public static <T> @NotNull T unwrap(@NotNull DataResult<T> result) {
        if (result.result().isPresent()) {
            return result.result().get();
        } else if (result.error().isPresent()) {
            throw new RuntimeException(result.error().get().message());
        } else {
            throw new RuntimeException("Unknown error");
        }
    }
}
