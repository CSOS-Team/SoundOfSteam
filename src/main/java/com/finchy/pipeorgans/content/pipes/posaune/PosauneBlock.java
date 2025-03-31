package com.finchy.pipeorgans.content.pipes.posaune;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.PedalPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PosauneBlock extends PedalPipeBlock {

    public PosauneBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.POSAUNE;
        this.extensionBlock = AllBlocks.POSAUNE_EXTENSION;
        this.blockEntity = AllBlockEntities.POSAUNE_BLOCK_ENTITY;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        GenericWhistleProperties.WhistleSize size = pState.getValue(SIZE);
        if (size == GenericWhistleProperties.WhistleSize.TINY) { size = GenericWhistleProperties.WhistleSize.SMALL; }
        VoxelShape whistle = AllShapes.getSlimBase(size);
        return Shapes.or(whistle,
                !pState.getValue(WALL) ?
                        AllShapes.BASE_FLOOR : AllShapes.getBase(pState.getValue(FACING)));
    }
}
