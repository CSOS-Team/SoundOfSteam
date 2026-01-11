package com.finchy.pipeorgans.content.pipes.voxCeleste;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxCelesteExtensionBlock extends DoubleExtensionBlock {
    public VoxCelesteExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.pipeBlock = AllBlocks.VOX_CELESTE;
    }

    @Override
    public boolean isDirectional() {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.stringExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE), pState.getValue(FACING));
    }
}
