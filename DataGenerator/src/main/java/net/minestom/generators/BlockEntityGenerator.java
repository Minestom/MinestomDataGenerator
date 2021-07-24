package net.minestom.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minestom.generators.common.DataGeneratorCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

public final class BlockEntityGenerator extends DataGeneratorCommon {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockEntityGenerator.class);

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject generate() {
        Set<ResourceLocation> blockEntityRLs = Registry.BLOCK_ENTITY_TYPE.keySet();
        JsonObject blockEntities = new JsonObject();

        for (ResourceLocation blockEntityRL : blockEntityRLs) {
            BlockEntityType<?> bet = Registry.BLOCK_ENTITY_TYPE.get(blockEntityRL);

            JsonObject blockEntity = new JsonObject();

            // Use reflection to get valid blocks
            {
                JsonArray beBlocks = new JsonArray();
                try {
                    Field fcField = BlockEntityType.class.getDeclaredField("validBlocks");

                    fcField.setAccessible(true);

                    Set<Block> validBlocks = (Set<Block>) fcField.get(bet);
                    for (Block validBlock : validBlocks) {
                        JsonObject beBlock = new JsonObject();
                        beBlock.addProperty("id", Registry.BLOCK.getKey(validBlock).toString());

                        beBlocks.add(beBlock);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    LOGGER.error("Failed to get block-entity blocks, skipping block-entity with ID '" + blockEntityRL + "'.", e);
                    continue;
                }
                blockEntity.add("blocks", beBlocks);
            }
            blockEntities.add(blockEntityRL.toString(), blockEntity);
        }
        return blockEntities;
    }
}
