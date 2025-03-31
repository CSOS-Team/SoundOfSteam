package com.finchy.pipeorgans.content.pipes.nasard;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.init.AllPartialModels;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.animation.AnimationTickHolder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class NasardRenderer extends SafeBlockEntityRenderer<NasardBlockEntity> {

    public NasardRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(NasardBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof NasardBlock))
            return;

        Direction direction = blockState.getValue(NasardBlock.FACING);
        GenericWhistleProperties.WhistleSize size = blockState.getValue(NasardBlock.SIZE);

        PartialModel mouth = size == GenericWhistleProperties.WhistleSize.TINY ? AllPartialModels.NASARD_MOUTH_TINY :
                size == GenericWhistleProperties.WhistleSize.SMALL ? AllPartialModels.NASARD_MOUTH_SMALL :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? AllPartialModels.NASARD_MOUTH_MEDIUM :
                        size == GenericWhistleProperties.WhistleSize.LARGE ? AllPartialModels.NASARD_MOUTH_LARGE : AllPartialModels.NASARD_MOUTH_HUGE;

        float offset = be.animation.getValue(partialTicks);
        if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
            float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
            offset -= (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 16f);
        }

        CachedBuffers.partial(mouth, blockState)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                .uncenter()
                .translate(0, offset / 16f, 0)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
