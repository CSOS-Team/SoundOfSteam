package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.content.windchest.WindchestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

public class PipePowerState {

    private boolean wasPoweredLastTick = false;

    public boolean tickAndCheckRisingEdge(
            Level level,
            GenericPipeBlockEntity pipe
    ) {
        boolean powered = computePowered(level, pipe);
        boolean risingEdge = powered && !wasPoweredLastTick;
        wasPoweredLastTick = powered;
        return risingEdge;
    }

    public boolean isPowered(Level level, GenericPipeBlockEntity pipe) {
        return computePowered(level, pipe);
    }

    private boolean computePowered(Level level, GenericPipeBlockEntity pipe) {
        FluidTankBlockEntity tank = pipe.getTank();
        BlockState state = pipe.getBlockState();

        BlockPos attachedPos = pipe.getBlockPos()
                .relative(pipe.pipeBlock.get().getAttachedDirection(state));
        BlockState attachedState = level.getBlockState(attachedPos);

        boolean isActive = false;
        if (attachedState.getBlock() instanceof WindchestBlock windchest) {
            isActive = windchest.isMasterActive(
                    level,
                    attachedState.getValue(GenericPipeBlock.FACING),
                    attachedPos
            );
        }

        return ((tank != null
                && tank.boiler.isActive()
                && (tank.boiler.passiveHeat || tank.boiler.activeHeat > 0))
                || isActive)
                && pipe.isPowered();
    }
}
