package com.finchy.pipeorgans.content.pipes.subbass;

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
public class SubbassRenderer extends SafeBlockEntityRenderer<SubbassBlockEntity> {

    public SubbassRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(SubbassBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof SubbassBlock))
            return;

        Direction direction = blockState.getValue(SubbassBlock.FACING);
        PipeSize size = blockState.getValue(SubbassBlock.SIZE);

        PartialModel mouth = switch (size) {
            case TINY -> AllPartialModels.SUBBASS_MOUTH_TINY;
            case SMALL -> AllPartialModels.SUBBASS_MOUTH_SMALL;
            case MEDIUM -> AllPartialModels.SUBBASS_MOUTH_MEDIUM;
            case LARGE -> AllPartialModels.SUBBASS_MOUTH_LARGE;
            case HUGE -> AllPartialModels.SUBBASS_MOUTH_HUGE;
        };

        float offset = be.animation.getValue(partialTicks);
        if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
            float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
            offset -= (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 16f);
        }

        CachedBuffers.partial(mouth, blockState)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                .uncenter()
                .translate(0, -offset*2 / 16f, 0)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
