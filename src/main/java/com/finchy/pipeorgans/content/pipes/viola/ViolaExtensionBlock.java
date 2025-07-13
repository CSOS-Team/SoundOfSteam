package com.finchy.pipeorgans.content.pipes.viola;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ViolaExtensionBlock extends DoubleExtensionBlock {
    public ViolaExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.VIOLA;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.stringExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE));
    }
}
