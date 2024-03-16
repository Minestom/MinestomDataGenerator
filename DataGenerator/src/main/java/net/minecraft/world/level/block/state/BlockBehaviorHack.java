package net.minecraft.world.level.block.state;

import org.jetbrains.annotations.NotNull;

public final class BlockBehaviorHack {

    public static float getMaxHorizontalOffset(@NotNull BlockBehaviour block) {
        return block.getMaxHorizontalOffset();
    }

    public static float getMaxVerticalOffset(@NotNull BlockBehaviour block) {
        return block.getMaxVerticalOffset();
    }
}
