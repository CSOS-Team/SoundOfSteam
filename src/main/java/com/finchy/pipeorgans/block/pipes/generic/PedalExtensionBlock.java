package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.block.Generic;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class PedalExtensionBlock extends GenericExtensionBlock {

    public PedalExtensionBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, Generic.QuadrupleExtensionShape.QUAD)
                .setValue(SIZE, Generic.WhistleSize.LARGE));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return sneakWrenchedRemove(state, context);
    }
}
