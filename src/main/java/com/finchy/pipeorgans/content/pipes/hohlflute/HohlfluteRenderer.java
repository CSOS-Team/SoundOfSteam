package com.finchy.pipeorgans.content.pipes.hohlflute;

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

public class HohlfluteRenderer extends SafeBlockEntityRenderer<HohlfluteBlockEntity> {

    public HohlfluteRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(HohlfluteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof HohlfluteBlock))
            return;

        Direction direction = blockState.getValue(HohlfluteBlock.FACING);
        PipeSize size = blockState.getValue(HohlfluteBlock.SIZE);

        PartialModel mouth = switch (size) {
            case TINY -> AllPartialModels.HOHLFLUTE_MOUTH_TINY;
            case SMALL -> AllPartialModels.HOHLFLUTE_MOUTH_SMALL;
            case MEDIUM -> AllPartialModels.HOHLFLUTE_MOUTH_MEDIUM;
            case LARGE -> AllPartialModels.HOHLFLUTE_MOUTH_LARGE;
            case HUGE -> AllPartialModels.HOHLFLUTE_MOUTH_HUGE;
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
                .translateY(4/16f)
                .translateZ(switch (size) {
                    case TINY -> 6;
                    case SMALL -> 5;
                    case MEDIUM -> 4;
                    case LARGE -> 3;
                    case HUGE -> 2;
                } /16f)
                .rotateXDegrees(-offset*16)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
