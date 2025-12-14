package com.finchy.pipeorgans.content.pipes.openWood;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.SinglePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OpenWoodBlock extends SinglePipeBlock {

    public OpenWoodBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.OPEN_WOOD;
        extensionBlock = AllBlocks.OPEN_WOOD_EXTENSION;
        blockEntityType = AllBlockEntities.OPEN_WOOD_BLOCK_ENTITY;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.genericPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
