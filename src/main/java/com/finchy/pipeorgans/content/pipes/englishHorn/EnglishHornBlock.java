package com.finchy.pipeorgans.content.pipes.englishHorn;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnglishHornBlock extends DoublePipeBlock {
    public EnglishHornBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.ENGLISH_HORN;
        extensionBlock = AllBlocks.ENGLISH_HORN_EXTENSION;
        blockEntityType = AllBlockEntities.ENGLISH_HORN_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.slimPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
