package com.finchy.pipeorgans.content.midi.stopMaster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StopMasterSlotPositioning extends ValueBoxTransform.Sided {

    @Override
    public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
        return new Vec3(0.5, 1, 0.5);
    }

    @Override
    protected boolean isSideActive(BlockState state, Direction direction) {
        return direction.equals(Direction.UP);
    }

    @Override
    public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
        super.rotate(level, pos, state, ms);
        Direction facing = state.getValue(StopMasterBlock.FACING);
        if (getSide() != Direction.UP)
            return;
        TransformStack.of(ms)
                .rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
    }

    @Override
    protected Vec3 getSouthLocation() {
        return Vec3.ZERO;
    }
}
