package com.finchy.pipeorgans.block.pipes.trompette;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TrompetteRenderer extends SafeBlockEntityRenderer<TrompetteBlockEntity> {

    public TrompetteRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(TrompetteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof TrompetteBlock))
            return;

        Direction direction = blockState.getValue(TrompetteBlock.FACING);
        Generic.WhistleSize size = blockState.getValue(TrompetteBlock.SIZE);

        PartialModel mouth = size == Generic.WhistleSize.TINY ? AllPartialModels.TROMPETTE_MOUTH_TINY :
                size == Generic.WhistleSize.SMALL ? AllPartialModels.TROMPETTE_MOUTH_SMALL :
                size == Generic.WhistleSize.MEDIUM ? AllPartialModels.TROMPETTE_MOUTH_MEDIUM :
                size == Generic.WhistleSize.LARGE ? AllPartialModels.TROMPETTE_MOUTH_LARGE : AllPartialModels.TROMPETTE_MOUTH_HUGE;

        float chaseTarget = be.animation.getChaseTarget();

        CachedBufferer.partial(mouth, blockState)
                .centre()
                .rotateY(AngleHelper.horizontalAngle(direction))
                .unCentre()
                .scale(chaseTarget)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
