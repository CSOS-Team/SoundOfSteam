package com.finchy.pipeorgans.content.pipes.rohrflote;

import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class RohrfloteRenderer extends SafeBlockEntityRenderer<RohrfloteBlockEntity> {

    public RohrfloteRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(RohrfloteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof RohrfloteBlock))
            return;

        Direction direction = blockState.getValue(RohrfloteBlock.FACING);
        PipeSize size = blockState.getValue(RohrfloteBlock.SIZE);

        PartialModel mouth = switch (size) {
            case TINY -> AllPartialModels.ROHRFLOTE_MOUTH_TINY;
            case SMALL -> AllPartialModels.ROHRFLOTE_MOUTH_SMALL;
            case MEDIUM -> AllPartialModels.ROHRFLOTE_MOUTH_MEDIUM;
            case LARGE -> AllPartialModels.ROHRFLOTE_MOUTH_LARGE;
            case HUGE -> AllPartialModels.ROHRFLOTE_MOUTH_HUGE;
        };

        float offset = be.animation.getValue(partialTicks);
        if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
            float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
            offset -= (float) (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 8f);
        }

        CachedBuffers.partial(mouth, blockState)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                .uncenter()
                .translate(0, -offset / 16f, 0)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
