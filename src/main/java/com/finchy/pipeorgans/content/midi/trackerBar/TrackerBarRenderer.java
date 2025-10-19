package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TrackerBarRenderer extends KineticBlockEntityRenderer<TrackerBarBlockEntity> {
    
    public TrackerBarRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(TrackerBarBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        float angle = be.getRollerAngle(partialTicks);
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState blockState = be.getBlockState();
        Direction facing = blockState.getOptionalValue(BlockStateProperties.HORIZONTAL_FACING).orElse(Direction.NORTH);

        SuperByteBuffer roller1 = CachedBuffers.partialFacing(AllPartialModels.TRACKER_BAR_ROLLER, blockState, facing)
                .translate(0, 0, 4.5/16f);
        SuperByteBuffer roller2 = CachedBuffers.partialFacing(AllPartialModels.TRACKER_BAR_ROLLER, blockState, facing)
                .translate(0, 0, -5.5/16f);

        kineticRotationTransform(roller1, be, getRotationAxisOf(be), angle, light).renderInto(ms, vb);
        kineticRotationTransform(roller2, be, getRotationAxisOf(be), angle, light).renderInto(ms, vb);
    }

    @Override
    protected BlockState getRenderedBlockState(TrackerBarBlockEntity be) {
        return super.getRenderedBlockState(be);
    }
}
