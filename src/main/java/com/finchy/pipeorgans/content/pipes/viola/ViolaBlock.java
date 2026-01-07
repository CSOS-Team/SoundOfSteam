package com.finchy.pipeorgans.content.pipes.viola;

import com.finchy.pipeorgans.content.pipes.generic.EPipeMaterial;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ViolaBlock extends DoublePipeBlock {
    public ViolaBlock(Properties pProperties) {
        super(pProperties, false, EPipeMaterial.PipeMaterial.METAL);
        baseBlock = AllBlocks.VIOLA;
        extensionBlock = AllBlocks.VIOLA_EXTENSION;
        blockEntityType = AllBlockEntities.VIOLA_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.stringPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
