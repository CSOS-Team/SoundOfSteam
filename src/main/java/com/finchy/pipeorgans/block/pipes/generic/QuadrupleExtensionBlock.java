package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.block.Generic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class QuadrupleExtensionBlock extends GenericExtensionBlock {

    public QuadrupleExtensionBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, Generic.QuadrupleExtensionShape.SINGLE)
                .setValue(SIZE, Generic.WhistleSize.MEDIUM));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!(world instanceof ServerLevel))
            return InteractionResult.SUCCESS;

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .25f || state.getValue(SHAPE) == Generic.QuadrupleExtensionShape.SINGLE)
            return sneakWrenchedRemove(state, context);

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .5) {
            world.setBlock(pos, state.setValue(SHAPE, Generic.QuadrupleExtensionShape.SINGLE), 3);
            playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + .75f) {
            world.setBlock(pos, state.setValue(SHAPE, Generic.QuadrupleExtensionShape.DOUBLE), 3);
            playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }

        if (context.getClickLocation().y < context.getClickedPos()
                .getY() + 1f) {
            world.setBlock(pos, state.setValue(SHAPE, Generic.QuadrupleExtensionShape.TRIPLE), 3);
            playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.SUCCESS;
    }
}
