package com.finchy.pipeorgans.block.pipes.posaune;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.posaune.PosauneBlock;
import com.finchy.pipeorgans.block.pipes.posaune.PosauneBlockEntity;
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

public class PosauneRenderer extends SafeBlockEntityRenderer<PosauneBlockEntity> {

    public PosauneRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(PosauneBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof PosauneBlock))
            return;

        Direction direction = blockState.getValue(PosauneBlock.FACING);
        Generic.WhistleSize size = blockState.getValue(PosauneBlock.SIZE);

        PartialModel mouth = size == Generic.WhistleSize.SMALL ? AllPartialModels.POSAUNE_MOUTH_SMALL :
                size == Generic.WhistleSize.MEDIUM ? AllPartialModels.POSAUNE_MOUTH_MEDIUM :
                size == Generic.WhistleSize.LARGE ? AllPartialModels.POSAUNE_MOUTH_LARGE : AllPartialModels.POSAUNE_MOUTH_HUGE;

        float chaseTarget = be.animation.getChaseTarget();

        CachedBuffers.partial(mouth, blockState)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                .uncenter()
                .scale(chaseTarget)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
