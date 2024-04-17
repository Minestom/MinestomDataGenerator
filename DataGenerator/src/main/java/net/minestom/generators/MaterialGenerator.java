package net.minestom.generators;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minestom.datagen.DataGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public final class MaterialGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject items = new JsonObject();
        var registry = BuiltInRegistries.ITEM;
        var blockRegistry = BuiltInRegistries.BLOCK;
        var entityTypeRegistry = BuiltInRegistries.ENTITY_TYPE;

        var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var registryNbtOps = RegistryOps.create(NbtOps.INSTANCE, registryAccess);

        for (var item : registry) {
            final var location = registry.getKey(item);

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", registry.getId(item));
            itemJson.addProperty("translationKey", item.getDescriptionId());

            // Component prototype
            var components = new JsonObject();
            for (var component : item.components()) {
                var key = Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, component.type());
                Tag t = unwrap(component.encodeValue(registryNbtOps));
                var result = new SnbtPrinterTagVisitor("", 0, new ArrayList<>()).visit(t);
                components.addProperty(key, result);
            }
            itemJson.add("components", components);

            // Corresponding block
            Block block = Block.byItem(item);
            if (block != Blocks.AIR) { // Default = no block
                itemJson.addProperty("correspondingBlock", blockRegistry.getKey(block).toString());
            }

            // Armor properties (which aren't components still??)
            if (item instanceof ArmorItem armorItem) {
                JsonObject armorProperties = new JsonObject();
                armorProperties.addProperty("defense", armorItem.getDefense());
                armorProperties.addProperty("toughness", armorItem.getToughness());
                armorProperties.addProperty("slot", armorItem.getEquipmentSlot().getName());
                itemJson.add("armorProperties", armorProperties);
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
