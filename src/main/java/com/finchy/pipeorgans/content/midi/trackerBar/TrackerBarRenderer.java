package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllSpriteShifts;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

public class TrackerBarRenderer extends KineticBlockEntityRenderer<TrackerBarBlockEntity> {
    
    public TrackerBarRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(TrackerBarBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        Level level = be.getLevel();
        if (VisualizationManager.supportsVisualization(level))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState state = be.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Direction shaft = facing.getClockWise();

        standardKineticRotationTransform(
                CachedBuffers.partialFacingVertical(AllPartialModels.TRACKER_BAR_SHAFT, state, shaft),
                be, light)
                .renderInto(ms, vb);

        float angle = be.getRollerAngle(partialTicks);

        SuperByteBuffer roller1 = CachedBuffers.partial(AllPartialModels.TRACKER_BAR_ROLLER, state);
        roller1.translate(0, 4.5/16f, 0)
                .center()
                .rotate(new Quaternionf().rotateTo(0, -1, 0, shaft.getStepX(), shaft.getStepY(), shaft.getStepZ()))
                .rotateY(angle)
                .uncenter()
                .light(light)
                .renderInto(ms, vb);;

        SuperByteBuffer roller2 = CachedBuffers.partial(AllPartialModels.TRACKER_BAR_ROLLER, state);
        roller2.translate(facing.getStepX()/16f, -5.5/16f, facing.getStepZ()/16f)
                .center()
                .rotate(new Quaternionf().rotateTo(0, -1, 0, shaft.getStepX(), shaft.getStepY(), shaft.getStepZ()))
                .rotateY(angle)
                .uncenter()
                .light(light)
                .renderInto(ms, vb);



        if (!be.midiSequencerBehaviour.isSequenceLoaded()) return;
        SpriteShiftEntry spriteShift = AllSpriteShifts.SCROLLING_MUSIC;
        float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();

        float speed = be.getScrollSpeed();
        double vScroll = speed*AnimationTickHolder.getRenderTime(level);
        vScroll = vScroll - Math.floor(vScroll);
        vScroll *= spriteHeight/2;

        SuperByteBuffer paperBuffer = CachedBuffers.partial(AllPartialModels.TRACKER_BAR_PAPER, state);
        paperBuffer.shiftUVScrolling(spriteShift, 0, (float) vScroll);
        Direction paperFacing = facing;
        if (paperFacing.equals(Direction.NORTH) || paperFacing.equals(Direction.SOUTH)) paperFacing = paperFacing.getOpposite();
        paperBuffer.rotateCentered(paperFacing.toYRot()* Mth.DEG_TO_RAD, Axis.YP).light(light).renderInto(ms, vb);
    }
}
