package com.finchy.pipeorgans.content.pipes.trompette;

import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
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

public class TrompetteRenderer extends SafeBlockEntityRenderer<TrompetteBlockEntity> {

    public TrompetteRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(TrompetteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof TrompetteBlock))
            return;

        Direction direction = blockState.getValue(TrompetteBlock.FACING);
        PipeSize size = blockState.getValue(TrompetteBlock.SIZE);

        PartialModel mouth = switch (size) {
            case TINY -> AllPartialModels.TROMPETTE_MOUTH_TINY;
            case SMALL -> AllPartialModels.TROMPETTE_MOUTH_SMALL;
            case MEDIUM -> AllPartialModels.TROMPETTE_MOUTH_MEDIUM;
            case LARGE -> AllPartialModels.TROMPETTE_MOUTH_LARGE;
            case HUGE -> AllPartialModels.TROMPETTE_MOUTH_HUGE;
        };

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
