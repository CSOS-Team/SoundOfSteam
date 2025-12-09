package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.EExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public abstract class QuadruplePipeBlockEntity extends GenericPipeBlockEntity {

    public QuadruplePipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void updatePitch() {
        BlockPos currentPos = worldPosition.above();
        int newPitch;
        for (newPitch = 0; newPitch <= 12; newPitch += 4) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!(blockState.getBlock() instanceof QuadrupleExtensionBlock))
                break;
            if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) == EExtensionShapes.QuadrupleShape.SINGLE) {
                newPitch++;
                break;
            }
            if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) == EExtensionShapes.QuadrupleShape.DOUBLE) {
                newPitch+=2;
                break;
            }
            if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) == EExtensionShapes.QuadrupleShape.TRIPLE) {
                newPitch+=3;
                break;
            }
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
