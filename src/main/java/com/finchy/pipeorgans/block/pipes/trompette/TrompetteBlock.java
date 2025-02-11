package com.finchy.pipeorgans.block.pipes.trompette;

import com.finchy.pipeorgans.block.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrompetteBlock extends GenericPipeBlock {

    public TrompetteBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.TROMPETTE;
        this.extensionBlock = AllBlocks.TROMPETTE_EXTENSION;
        this.blockEntity = AllBlockEntities.TROMPETTE_BLOCK_ENTITY;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape whistle = AllShapes.getTrompetteBase(pState.getValue(SIZE));
        return Shapes.or(whistle,
                !pState.getValue(WALL) ?
                        AllShapes.BASE_FLOOR : AllShapes.getBase(pState.getValue(FACING)));
    }
}
