package com.finchy.pipeorgans.block.pipes.vox_humana;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxHumanaBlock extends QuadruplePipeBlock {
    public VoxHumanaBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.VOX_HUMANA;
        this.extensionBlock = AllBlocks.VOX_HUMANA_EXTENSION;
        this.blockEntity = AllBlockEntities.VOX_HUMANA_BLOCK_ENTITY.getDelegate();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Generic.WhistleSize size = pState.getValue(SIZE);
        if (size == Generic.WhistleSize.TINY) { size = Generic.WhistleSize.SMALL; }
        VoxelShape whistle = AllShapes.getSlimBase(size);
        return Shapes.or(whistle,
                !pState.getValue(WALL) ?
                        AllShapes.BASE_FLOOR : AllShapes.getBase(pState.getValue(FACING)));
    }
}
