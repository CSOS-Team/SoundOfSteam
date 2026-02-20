package com.finchy.pipeorgans.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class RangedPowerAdvancement {


    /**
    This helper is for triggering an advancement within a radius of a BE

     example: When you play a pipe base

    Parameters:
    @param level World level (must be ServerLevel)
    @param pos  Center position
    @param radius  The max distance from the block you want the advancement to trigger
    @param trigger  Which advancement trigger do you want to call?
     **/

    public static void trigger(
            Level level,
            BlockPos pos,
            double radius,
            Consumer<ServerPlayer> trigger
    ) {
        if (!(level instanceof ServerLevel serverLevel))
            return;

        AABB box = new AABB(pos).inflate(radius);

        for (ServerPlayer player : serverLevel.getEntitiesOfClass(ServerPlayer.class, box)) {
            trigger.accept(player);
        }
    }
}
