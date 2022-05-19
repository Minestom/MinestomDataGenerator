package net.minestom.generators;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minestom.datagen.DataGenerator;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class BlockGenerator extends DataGenerator {
    @Override
    public JsonObject generate() {
        JsonObject blocks = new JsonObject();
        for (var block : Registry.BLOCK) {
            final var location = Registry.BLOCK.getKey(block);
            final var defaultBlockState = block.defaultBlockState();

            JsonObject blockJson = new JsonObject();
            blockJson.addProperty("id", Registry.BLOCK.getId(block));
            blockJson.addProperty("translationKey", block.getDescriptionId());
            blockJson.addProperty("explosionResistance", block.getExplosionResistance());
            blockJson.addProperty("friction", block.getFriction());
            addDefaultable(blockJson, "speedFactor", block.getSpeedFactor(), 1f);
            addDefaultable(blockJson, "jumpFactor", block.getJumpFactor(), 1f);
            blockJson.addProperty("defaultStateId", Block.BLOCK_STATE_REGISTRY.getId(defaultBlockState));
            addDefaultable(blockJson, "gravity", block instanceof FallingBlock, false);
            blockJson.addProperty("canRespawnIn", block.isPossibleToRespawnInThis());
            // Corresponding item
            Item correspondingItem = Item.BY_BLOCK.get(block);
            if (correspondingItem != null) { // Default = no item
                blockJson.addProperty("correspondingItem", Registry.ITEM.getKey(correspondingItem).toString());
            }
            // Random offset
            if (defaultBlockState.getOffsetType() != BlockBehaviour.OffsetType.NONE) {
                blockJson.addProperty("maxHorizontalOffset", block.getMaxHorizontalOffset());
                if (defaultBlockState.getOffsetType() == BlockBehaviour.OffsetType.XYZ) {
                    blockJson.addProperty("maxVerticalOffset", block.getMaxVerticalOffset());
                }
            }
            // Default values
            writeState(defaultBlockState, null, blockJson);
            {
                // List of properties
                JsonObject properties = new JsonObject();
                for (var property : block.getStateDefinition().getProperties()) {
                    JsonArray values = new JsonArray();
                    final String key = property.getName();
                    for (var value : property.getPossibleValues()) {
                        values.add(value.toString().toLowerCase(Locale.ROOT));
                    }
                    properties.add(key, values);
                }
                if (properties.size() > 0) {
                    blockJson.add("properties", properties);
                }
            }
            // Block states
            JsonObject blockStates = new JsonObject();
            for (BlockState bs : block.getStateDefinition().getPossibleStates()) {
                JsonObject state = new JsonObject();
                state.addProperty("stateId", Block.BLOCK_STATE_REGISTRY.getId(bs));
                writeState(bs, blockJson, state);

                StringBuilder stateName = new StringBuilder("[");
                for (var propertyEntry : bs.getValues().entrySet()) {
                    if (stateName.length() > 1) {
                        stateName.append(",");
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
        for (var blockEntityType : Registry.BLOCK_ENTITY_TYPE) {
            final var location = Registry.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
            try {
                Field fcField = BlockEntityType.class.getDeclaredField("validBlocks");
                fcField.setAccessible(true);
                for (Block validBlock : (Set<Block>) fcField.get(blockEntityType)) {
                    final String namespace = Registry.BLOCK.getKey(validBlock).toString();
                    final JsonObject blockJson = blocks.get(namespace).getAsJsonObject();

                    JsonObject blockEntityObject = new JsonObject();
                    blockEntityObject.addProperty("namespace", location.toString());
                    blockEntityObject.addProperty("id", Registry.BLOCK_ENTITY_TYPE.getId(blockEntityType));
                    blockJson.add("blockEntity", blockEntityObject);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }

    private void writeState(BlockState blockState, JsonObject blockJson, JsonObject state) {
        // Data
        appendState(blockJson, state, "hardness", blockState.getDestroySpeed(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), float.class);
        appendState(blockJson, state, "lightEmission", blockState.getLightEmission(), 0, int.class);
        appendState(blockJson, state, "pushReaction", blockState.getPistonPushReaction().name(), String.class);
        appendState(blockJson, state, "mapColorId", blockState.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).id, int.class);
        appendState(blockJson, state, "occludes", blockState.canOcclude(), boolean.class);
        appendState(blockJson, state, "blocksMotion", blockState.getMaterial().blocksMotion(), boolean.class);
        appendState(blockJson, state, "flammable", blockState.getMaterial().isFlammable(), boolean.class);
        appendState(blockJson, state, "air", blockState.isAir(), false, boolean.class);
        appendState(blockJson, state, "liquid", blockState.getMaterial().isLiquid(), false, boolean.class);
        appendState(blockJson, state, "replaceable", blockState.getMaterial().isReplaceable(), false, boolean.class);
        appendState(blockJson, state, "solid", blockState.getMaterial().isSolid(), boolean.class);
        appendState(blockJson, state, "solidBlocking", blockState.getMaterial().isSolidBlocking(), boolean.class);
        // Shapes (Hit-boxes)
        appendState(blockJson, state, "shape", blockState.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString(), String.class);
        appendState(blockJson, state, "collisionShape", blockState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString(), String.class);
        appendState(blockJson, state, "interactionShape", blockState.getInteractionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString(), String.class);
        appendState(blockJson, state, "occlusionShape", blockState.getOcclusionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).toAabbs().toString(), String.class);
        appendState(blockJson, state, "visualShape", blockState.getVisualShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty()).toAabbs().toString(), String.class);
    }

    private <T> void appendState(JsonObject main, JsonObject state, String key, T value, T defaultValue, Class<T> valueType) {
        if (Objects.equals(value, defaultValue))
            return;
        Gson gson = new Gson();
        if (main == null || !Objects.equals(value, gson.fromJson(main.get(key), valueType))) {
            if (value instanceof String s) {
                state.addProperty(key, s);
            } else if (value instanceof Integer i) {
                state.addProperty(key, i);
            } else if (value instanceof Double d) {
                state.addProperty(key, d);
            } else if (value instanceof Float f) {
                state.addProperty(key, f);
            } else if (value instanceof Boolean b) {
                state.addProperty(key, b);
            } else {
                throw new IllegalStateException("Type " + valueType + " cannot be added to the json");
            }
        }
    }

    private <T> void appendState(JsonObject main, JsonObject state, String key, T value, Class<T> valueType) {
        appendState(main, state, key, value, null, valueType);
    }
}
