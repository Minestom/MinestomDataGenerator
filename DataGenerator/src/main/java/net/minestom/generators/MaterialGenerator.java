package net.minestom.generators;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minestom.datagen.DataGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Base64;

public final class MaterialGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject items = new JsonObject();
        var registry = BuiltInRegistries.ITEM;
        var blockRegistry = BuiltInRegistries.BLOCK;
        for (var item : registry) {
            final var location = registry.getKey(item);

            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", registry.getId(item));
            itemJson.addProperty("translationKey", item.getDescriptionId());

            // Corresponding block
            Block block = Block.byItem(item);
            if (block != Blocks.AIR) { // Default = no block
                itemJson.addProperty("correspondingBlock", blockRegistry.getKey(block).toString());
            }

            //todo CLEAN ME UP
            var components = new JsonObject();
            for (var component : item.components()) {
                var key = Util.getRegisteredName(BuiltInRegistries.DATA_COMPONENT_TYPE, component.type());
                if ("minecraft:tool".equals(key)) {
                    continue; //todo bring me back
                }
                var baos = new ByteArrayOutputStream();
                try {
                    Tag t = unwrap(component.encodeValue(NbtOps.INSTANCE));
                    NbtIo.writeAnyTag(t, new DataOutputStream(baos));
                } catch (Exception e) {
                    throw new RuntimeException("serialize failed on " + key, e);
                }

                var s = Base64.getEncoder().encodeToString(baos.toByteArray());
                components.addProperty(key, s);
            }
            itemJson.add("components", components);
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
