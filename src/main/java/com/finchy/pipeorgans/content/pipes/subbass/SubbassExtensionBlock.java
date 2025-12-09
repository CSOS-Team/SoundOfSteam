package com.finchy.pipeorgans.content.pipes.subbass;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.SingleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SubbassExtensionBlock extends SingleExtensionBlock {
    public SubbassExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.SUBBASS;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.genericExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE));
    }
}
