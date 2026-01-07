package com.finchy.pipeorgans.content.pipes.voxHumana;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxHumanaBlock extends QuadruplePipeBlock {
    public VoxHumanaBlock(Properties pProperties) {
        super(pProperties, true);
        baseBlock = AllBlocks.VOX_HUMANA;
        extensionBlock = AllBlocks.VOX_HUMANA_EXTENSION;
        blockEntityType = AllBlockEntities.VOX_HUMANA_BLOCK_ENTITY;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.slimPipeShape(pState.getValue(SIZE), pState.getValue(WALL), pState.getValue(FACING));
    }
}
