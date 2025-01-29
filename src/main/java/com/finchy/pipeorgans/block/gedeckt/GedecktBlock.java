package com.finchy.pipeorgans.block.gedeckt;

import com.finchy.pipeorgans.block.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GedecktBlock extends GenericPipeBlock {
    public GedecktBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.GEDECKT;
        this.extensionBlock = AllBlocks.GEDECKT_EXTENSION;
        this.blockEntity = AllBlockEntities.GEDECKT_BLOCK_ENTITY;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape whistle = AllShapes.getSlimBase(pState.getValue(SIZE)); // get base whistle shape (temporarily medium)
        return Shapes.or(whistle,
                !pState.getValue(WALL) ?
                        AllShapes.BASE_FLOOR : AllShapes.getBase(pState.getValue(FACING)));
    }
}
