package com.finchy.pipeorgans.content.pipes.vox_humana;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxHumanaExtensionBlock extends QuadrupleExtensionBlock {
    public VoxHumanaExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.VOX_HUMANA;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        GenericWhistleProperties.WhistleSize size = pState.getValue(SIZE);
        if (size == GenericWhistleProperties.WhistleSize.TINY) { size = GenericWhistleProperties.WhistleSize.SMALL; }
        return AllShapes.getSlimExtensionShape(pState.getValue(SHAPE), size);
    }
}
