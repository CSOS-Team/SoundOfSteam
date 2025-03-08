package com.finchy.pipeorgans.block.pipes.posaune;

import com.finchy.pipeorgans.block.pipes.generic.PedalExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PosauneExtensionBlock extends PedalExtensionBlock {
    public PosauneExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.POSAUNE;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.getTrompetteExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE));
    }
}
