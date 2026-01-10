package com.finchy.pipeorgans.content.pipes.piccolo;

import com.finchy.pipeorgans.content.pipes.generic.PipeMaterial;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PiccoloBlock extends QuadruplePipeBlock {
    public PiccoloBlock(Properties pProperties) {
        super(pProperties, PipeMaterial.METAL);
        baseBlock = AllBlocks.PICCOLO;
        extensionBlock = AllBlocks.PICCOLO_EXTENSION;
        blockEntityType = AllBlockEntities.PICCOLO_BLOCK_ENTITY;
        registerDefaultState(defaultBlockState().setValue(SIZE, PipeSize.SMALL));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.genericPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
