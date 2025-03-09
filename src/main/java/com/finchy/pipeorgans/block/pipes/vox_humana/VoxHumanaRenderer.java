package com.finchy.pipeorgans.block.pipes.vox_humana;

import com.finchy.pipeorgans.block.Generic;
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

public class VoxHumanaRenderer extends SafeBlockEntityRenderer<VoxHumanaBlockEntity> {

    public VoxHumanaRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(VoxHumanaBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!(blockState.getBlock() instanceof VoxHumanaBlock))
            return;

        Direction direction = blockState.getValue(VoxHumanaBlock.FACING);
        Generic.WhistleSize size = blockState.getValue(VoxHumanaBlock.SIZE);

        PartialModel mouth = size == Generic.WhistleSize.TINY ? AllPartialModels.VOX_HUMANA_MOUTH_TINY :
                size == Generic.WhistleSize.SMALL ? AllPartialModels.VOX_HUMANA_MOUTH_SMALL :
                size == Generic.WhistleSize.MEDIUM ? AllPartialModels.VOX_HUMANA_MOUTH_MEDIUM :
                size == Generic.WhistleSize.LARGE ? AllPartialModels.VOX_HUMANA_MOUTH_LARGE : AllPartialModels.VOX_HUMANA_MOUTH_HUGE;

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
