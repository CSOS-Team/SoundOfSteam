package com.finchy.pipeorgans.content.pipes.diapason;

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

public class DiapasonBlock extends DoublePipeBlock {
    public DiapasonBlock(Properties pProperties) {
        super(pProperties, PipeMaterial.METAL);
        baseBlock = AllBlocks.DIAPASON;
        extensionBlock = AllBlocks.DIAPASON_EXTENSION;
        blockEntityType = AllBlockEntities.DIAPASON_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.genericPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
