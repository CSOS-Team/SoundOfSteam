package com.finchy.pipeorgans.content.pipes.bassoon;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
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

public class BassoonRenderer extends SafeBlockEntityRenderer<BassoonBlockEntity> {

    public BassoonRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(BassoonBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof BassoonBlock))
            return;

        Direction direction = blockState.getValue(BassoonBlock.FACING);
        EPipeSizes.PipeSize size = blockState.getValue(BassoonBlock.SIZE);

        PartialModel mouth = switch (size) {
            case TINY -> AllPartialModels.BASSOON_MOUTH_TINY;
            case SMALL -> AllPartialModels.BASSOON_MOUTH_SMALL;
            case MEDIUM -> AllPartialModels.BASSOON_MOUTH_MEDIUM;
            case LARGE -> AllPartialModels.BASSOON_MOUTH_LARGE;
            case HUGE -> AllPartialModels.BASSOON_MOUTH_HUGE;
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
