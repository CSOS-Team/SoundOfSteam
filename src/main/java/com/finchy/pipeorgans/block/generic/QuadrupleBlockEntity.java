package com.finchy.pipeorgans.block.generic;

import com.finchy.pipeorgans.block.Generic;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class QuadrupleBlockEntity extends GenericPipeBlockEntity {

    public RegistryObject<BlockEntityType<? extends GenericPipeBlockEntity>> blockEntity;

    public QuadrupleBlockEntity(BlockPos pos, BlockState blockState, RegistryObject<BlockEntityType> blockEntity) {
        super(pos, blockState, blockEntity);
    }

    @Override
    public void updatePitch() {
        BlockPos currentPos = worldPosition.above();
        int newPitch;
        for (newPitch = 0; newPitch <= 12; newPitch += 4) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!(blockState.getBlock() instanceof QuadrupleExtensionBlock))
                break;
            if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) == Generic.QuadrupleExtensionShape.SINGLE) {
                newPitch++;
                break;
            }
            if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) == Generic.QuadrupleExtensionShape.DOUBLE) {
                newPitch+=2;
                break;
            }
            if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) == Generic.QuadrupleExtensionShape.TRIPLE) {
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
