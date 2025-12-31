package com.finchy.pipeorgans.content.pipes.trompette;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrompetteBlock extends DoublePipeBlock {
    public TrompetteBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.TROMPETTE;
        extensionBlock = AllBlocks.TROMPETTE_EXTENSION;
        blockEntityType = AllBlockEntities.TROMPETTE_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.slimPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
