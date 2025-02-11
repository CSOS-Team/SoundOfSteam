package com.finchy.pipeorgans.block.pipes.trompette;

import com.finchy.pipeorgans.block.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrompetteExtensionBlock extends GenericExtensionBlock {

    public TrompetteExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.TROMPETTE;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.getTrompetteExtensionShape(pState.getValue(SHAPE), pState.getValue(SIZE));
    }
}
