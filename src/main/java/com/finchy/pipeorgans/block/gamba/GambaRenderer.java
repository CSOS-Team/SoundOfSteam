package com.finchy.pipeorgans.block.gamba;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class GambaRenderer extends SafeBlockEntityRenderer<GambaBlockEntity> {

    public GambaRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(GambaBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof GambaBlock))
            return;

        Direction direction = blockState.getValue(GambaBlock.FACING);
        Generic.GambaWhistleSize size = blockState.getValue(GambaBlock.SIZE);

        PartialModel mouth = size == Generic.GambaWhistleSize.SMALL ? AllPartialModels.GAMBA_MOUTH_SMALL :
                size == Generic.GambaWhistleSize.MEDIUM ? AllPartialModels.GAMBA_MOUTH_MEDIUM :
                        size == Generic.GambaWhistleSize.LARGE ? AllPartialModels.GAMBA_MOUTH_LARGE : AllPartialModels.GAMBA_MOUTH_TINY;

        float offset = be.animation.getValue(partialTicks);
        if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
            float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
            offset -= (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 16f);
        }

        CachedBufferer.partial(mouth, blockState)
                .centre()
                .rotateY(AngleHelper.horizontalAngle(direction))
                .unCentre()
                .translate(0, -offset / 16f, 0)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
