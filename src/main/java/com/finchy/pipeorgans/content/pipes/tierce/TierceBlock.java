package com.finchy.pipeorgans.content.pipes.tierce;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TierceBlock extends QuadruplePipeBlock {
    public TierceBlock(Properties pProperties) {
        super(pProperties, false);
        baseBlock = AllBlocks.TIERCE;
        extensionBlock = AllBlocks.TIERCE_EXTENSION;
        blockEntityType = AllBlockEntities.TIERCE_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.slimPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
