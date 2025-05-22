package com.finchy.pipeorgans.content.pipes.englishhorn;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteBlock;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteBlockEntity;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class EnglishHornRenderer extends SafeBlockEntityRenderer<EnglishHornBlockEntity> {

    public EnglishHornRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(EnglishHornBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof EnglishHornBlock))
            return;

        Direction direction = blockState.getValue(EnglishHornBlock.FACING);
        GenericWhistleProperties.WhistleSize size = blockState.getValue(TrompetteBlock.SIZE);

        PartialModel mouth = size == GenericWhistleProperties.WhistleSize.TINY ? AllPartialModels.ENGLISHHORN_MOUTH_TINY :
                size == GenericWhistleProperties.WhistleSize.SMALL ? AllPartialModels.ENGLISHHORN_MOUTH_SMALL :
                        size == GenericWhistleProperties.WhistleSize.MEDIUM ? AllPartialModels.ENGLISHHORN_MOUTH_MEDIUM :
                                size == GenericWhistleProperties.WhistleSize.LARGE ? AllPartialModels.ENGLISHHORN_MOUTH_LARGE : AllPartialModels.ENGLISHHORN_MOUTH_HUGE;

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
