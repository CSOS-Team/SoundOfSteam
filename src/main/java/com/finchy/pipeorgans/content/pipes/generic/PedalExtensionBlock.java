package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class PedalExtensionBlock extends GenericExtensionBlock {

    public PedalExtensionBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, GenericWhistleProperties.QuadrupleExtensionShape.QUAD)
                .setValue(SIZE, GenericWhistleProperties.WhistleSize.LARGE));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return sneakWrenchedRemove(state, context);
    }
}
