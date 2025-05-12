package com.finchy.pipeorgans.content.pipes.vox_humana;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxHumanaBlock extends QuadruplePipeBlock {
    public VoxHumanaBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.VOX_HUMANA;
        extensionBlock = AllBlocks.VOX_HUMANA_EXTENSION;
        blockEntity = AllBlockEntities.VOX_HUMANA_BLOCK_ENTITY;
        shape = GenericWhistleProperties.WhistleShape.SLIM;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        GenericWhistleProperties.WhistleSize size = pState.getValue(SIZE);
        if (size == GenericWhistleProperties.WhistleSize.TINY) { // don't allow sizes smaller than tiny
            size = GenericWhistleProperties.WhistleSize.SMALL;
        }
        return AllShapes.getCompleteWhistleShape(
                size,
                shape,
                pState.getValue(WALL),
                pState.getValue(FACING)
        );
    }
}
