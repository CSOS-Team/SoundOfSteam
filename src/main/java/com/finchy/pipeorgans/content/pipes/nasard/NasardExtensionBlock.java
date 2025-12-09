package com.finchy.pipeorgans.content.pipes.nasard;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NasardExtensionBlock extends DoubleExtensionBlock {
    public NasardExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.NASARD;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.slimExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE));
    }
}
