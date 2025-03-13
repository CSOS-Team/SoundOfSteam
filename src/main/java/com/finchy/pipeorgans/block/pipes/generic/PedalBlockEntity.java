package com.finchy.pipeorgans.block.pipes.generic;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PedalBlockEntity extends GenericPipeBlockEntity {
    public PedalBlockEntity(BlockPos pos, BlockState blockState, BlockEntityType<? extends GenericPipeBlockEntity> blockEntity) {
        super(pos, blockState, blockEntity);
    }

    @Override
    public void updatePitch() {
        BlockPos currentPos = worldPosition.above();
        int newPitch;
        for (newPitch = 0; newPitch <= 12; newPitch += 1) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!(blockState.getBlock() instanceof PedalExtensionBlock))
                break;

            currentPos = currentPos.above();
        }
        if (pitch == newPitch)
            return;
        pitch = newPitch;

        notifyUpdate();

        FluidTankBlockEntity tank = getTank();
        if (tank != null && tank.boiler != null)
            tank.boiler.checkPipeOrganAdvancement(tank);
    }
}
