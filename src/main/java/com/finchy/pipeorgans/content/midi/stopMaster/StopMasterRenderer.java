package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class StopMasterRenderer extends SmartBlockEntityRenderer<StopMasterBlockEntity> {

    public StopMasterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(StopMasterBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);

        BlockState blockState = blockEntity.getBlockState();
        if (!(blockState.getBlock() instanceof StopMasterBlock))
            return;

        Direction direction = blockState.getValue(StopMasterBlock.FACING);
        boolean powered = blockState.getValue(StopMasterBlock.POWERED);

        PartialModel antennae = powered ? AllPartialModels.STOP_MASTER_ANTENNAE_ON : AllPartialModels.STOP_MASTER_ANTENNAE_OFF;

        CachedBuffers.partial(antennae, blockState)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                .uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

    }
}
