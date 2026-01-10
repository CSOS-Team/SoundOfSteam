package com.finchy.pipeorgans.content.pipes.chamade;

import com.finchy.pipeorgans.content.pipes.generic.PipeMaterial;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.HorizontalPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChamadeBlock extends HorizontalPipeBlock {
    public ChamadeBlock(Properties pProperties) {
        super(pProperties, PipeMaterial.METAL);
        baseBlock = AllBlocks.CHAMADE;
        extensionBlock = AllBlocks.CHAMADE_EXTENSION;
        blockEntityType = AllBlockEntities.CHAMADE_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.horizontalPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
