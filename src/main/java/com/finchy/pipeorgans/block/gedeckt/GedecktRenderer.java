package com.finchy.pipeorgans.block.gedeckt;

import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlock;
import com.finchy.pipeorgans.block.genericWhistle.GenericWhistleBlockEntity;
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

public class GedecktRenderer extends SafeBlockEntityRenderer<GenericWhistleBlockEntity> {

    public GedecktRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(GenericWhistleBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof GenericWhistleBlock))
            return;

        Direction direction = blockState.getValue(GenericWhistleBlock.FACING);
        GenericWhistleBlock.WhistleSize size = blockState.getValue(GenericWhistleBlock.SIZE);

        PartialModel mouth = size == GenericWhistleBlock.WhistleSize.SMALL ? AllPartialModels.GEDECKT_MOUTH_SMALL :
                size == GenericWhistleBlock.WhistleSize.MEDIUM ? AllPartialModels.GEDECKT_MOUTH_MEDIUM :
                        size == GenericWhistleBlock.WhistleSize.LARGE ? AllPartialModels.GEDECKT_MOUTH_LARGE : AllPartialModels.GEDECKT_MOUTH_HUGE;

        float offset = be.animation.getValue(partialTicks);
        if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
            float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
            offset -= (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 16f);
        }

        CachedBufferer.partial(mouth, blockState)
                .centre()
                .rotateY(AngleHelper.horizontalAngle(direction))
                .unCentre()
                .translateY((double) 4 /16)
                .translateZ((double) switch (size) {
                    case SMALL -> 5;
                    case MEDIUM -> 4;
                    case LARGE -> 3;
                    case HUGE -> 2;
                } /16)
                .rotateX(-offset*16)
                .light(light)
                .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

    }
}
