package com.finchy.pipeorgans.content.traps.zimblestern;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


public class ZimblesternBlock extends HorizontalKineticBlock implements IWrenchable {


    public ZimblesternBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return getRotationAxis(state) == face.getAxis();
    }
}

