package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minestom.generators.common.DataGeneratorCommon;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class BlockGenerator extends DataGeneratorCommon {
    @Override
    public JsonObject generate() {
        JsonObject blocks = new JsonObject();
        for (var entry : Registry.BLOCK.entrySet()) {
            final var location = entry.getKey().location();
            final var block = entry.getValue();
            final var defaultBlockState = block.defaultBlockState();

            JsonObject blockJson = new JsonObject();
            blockJson.addProperty("id", Registry.BLOCK.getId(block));
            blockJson.addProperty("namespaceId", location.toString());
            blockJson.addProperty("translationKey", block.getDescriptionId());
            blockJson.addProperty("explosionResistance", block.getExplosionResistance());
            blockJson.addProperty("friction", block.getFriction());
            blockJson.addProperty("speedFactor", block.getSpeedFactor());
            blockJson.addProperty("jumpFactor", block.getJumpFactor());
            blockJson.addProperty("defaultStateId", Block.BLOCK_STATE_REGISTRY.getId(defaultBlockState));
            blockJson.addProperty("gravity", block instanceof FallingBlock);
            blockJson.addProperty("canRespawnIn", block.isPossibleToRespawnInThis());
            // Corresponding item
            Item correspondingItem = Item.BY_BLOCK.get(block);
            if (correspondingItem != null) {
                blockJson.addProperty("correspondingItem", Registry.ITEM.getKey(correspondingItem).toString());
            }
            // Default values
            blockJson.addProperty("hardness", defaultBlockState.getDestroySpeed(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
            blockJson.addProperty("lightEmission", defaultBlockState.getLightEmission());
            blockJson.addProperty("pushReaction", defaultBlockState.getPistonPushReaction().name());
            blockJson.addProperty("mapColorId", defaultBlockState.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).id);
            blockJson.addProperty("occludes", defaultBlockState.canOcclude());
            blockJson.addProperty("blocksMotion", defaultBlockState.getMaterial().blocksMotion());
            blockJson.addProperty("flammable", defaultBlockState.getMaterial().isFlammable());
            blockJson.addProperty("air", defaultBlockState.isAir());
            blockJson.addProperty("liquid", defaultBlockState.getMaterial().isLiquid());
            blockJson.addProperty("replaceable", defaultBlockState.getMaterial().isReplaceable());
            blockJson.addProperty("solid", defaultBlockState.getMaterial().isSolid());
            blockJson.addProperty("solidBlocking", defaultBlockState.getMaterial().isSolidBlocking());
            // Block states
            JsonObject blockStates = new JsonObject();
            for (BlockState bs : block.getStateDefinition().getPossibleStates()) {
                JsonObject state = new JsonObject();
                state.addProperty("stateId", Block.BLOCK_STATE_REGISTRY.getId(bs));
                // Default values
                addDifferent(blockJson, state, "hardness", bs.getDestroySpeed(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), float.class);
                addDifferent(blockJson, state, "lightEmission", bs.getLightEmission(), int.class);
                addDifferent(blockJson, state, "pushReaction", bs.getPistonPushReaction().name(), String.class);
                addDifferent(blockJson, state, "mapColorId", bs.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).id, int.class);
                addDifferent(blockJson, state, "occludes", bs.canOcclude(), boolean.class);
                addDifferent(blockJson, state, "blocksMotion", bs.getMaterial().blocksMotion(), boolean.class);
                addDifferent(blockJson, state, "flammable", bs.getMaterial().isFlammable(), boolean.class);
                addDifferent(blockJson, state, "air", bs.isAir(), boolean.class);
                addDifferent(blockJson, state, "liquid", bs.getMaterial().isLiquid(), boolean.class);
                addDifferent(blockJson, state, "replaceable", bs.getMaterial().isReplaceable(), boolean.class);
                addDifferent(blockJson, state, "solid", bs.getMaterial().isSolid(), boolean.class);
                addDifferent(blockJson, state, "solidBlocking", bs.getMaterial().isSolidBlocking(), boolean.class);
                // Shapes (Hit-boxes)
                state.addProperty("shape", bs.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString());
                state.addProperty("collisionShape", bs.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString());
                state.addProperty("interactionShape", bs.getInteractionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString());
                state.addProperty("occlusionShape", bs.getOcclusionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString());
                state.addProperty("visualShape", bs.getOcclusionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString());

                StringBuilder stateName = new StringBuilder("[");
                boolean first = true;
                for (var propertyEntry : bs.getValues().entrySet()) {
                    if (!first) {
                        stateName.append(",");
                    } else {
                        first = false;
                    }
                    stateName.append(propertyEntry.getKey().getName().toLowerCase(Locale.ROOT))
                            .append("=")
                            .append(propertyEntry.getValue().toString().toLowerCase(Locale.ROOT));
                }
                stateName.append("]");

                blockStates.add(stateName.toString(), state);
            }
            blockJson.add("states", blockStates);
            blocks.add(location.toString(), blockJson);
        }
        // Add block entity
        for (var entry : Registry.BLOCK_ENTITY_TYPE.entrySet()) {
            final var location = entry.getKey().location();
            final var blockEntityType = entry.getValue();
            try {
                Field fcField = BlockEntityType.class.getDeclaredField("validBlocks");
                fcField.setAccessible(true);
                for (Block validBlock : (Set<Block>) fcField.get(blockEntityType)) {
                    final String namespace = Registry.BLOCK.getKey(validBlock).toString();
                    final JsonObject blockJson = blocks.get(namespace).getAsJsonObject();
                    blockJson.addProperty("blockEntity", location.toString());
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }

    private <T> void addDifferent(JsonObject main, JsonObject state, String key, T value, Class<T> valueType) {
        Gson gson = new Gson();
        if (!Objects.equals(value, gson.fromJson(main.get(key), valueType))) {
            state.addProperty("hardness", gson.toJson(value));
        }
    }
}
