package com.finchy.pipeorgans.content.pipes.prestant;

import com.finchy.pipeorgans.content.pipes.generic.PipeMaterial;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PrestantBlock extends DoublePipeBlock {
    public PrestantBlock(Properties pProperties) {
        super(pProperties, false, PipeMaterial.METAL);
        baseBlock = AllBlocks.PRESTANT;
        extensionBlock = AllBlocks.PRESTANT_EXTENSION;
        blockEntityType = AllBlockEntities.PRESTANT_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.genericPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
