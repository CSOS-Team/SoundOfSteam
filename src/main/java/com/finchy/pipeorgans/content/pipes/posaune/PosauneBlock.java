package com.finchy.pipeorgans.content.pipes.posaune;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.SinglePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PosauneBlock extends SinglePipeBlock {
    public PosauneBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.POSAUNE;
        extensionBlock = AllBlocks.POSAUNE_EXTENSION;
        blockEntityType = AllBlockEntities.POSAUNE_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.slimPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
