package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class TrackerBarVisual extends KineticBlockEntityVisual<TrackerBarBlockEntity> implements SimpleDynamicVisual {

    private final RotatingInstance rotatingModel;
    private final TransformedInstance roller1;
    private final TransformedInstance roller2;

    private final Matrix4f baseTransform1 = new Matrix4f();
    private final Matrix4f baseTransform2 = new Matrix4f();

    public TrackerBarVisual(VisualizationContext context, TrackerBarBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        Direction shaft = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();

        rotatingModel = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.TRACKER_BAR_SHAFT))
                .createInstance();

        rotatingModel.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(shaft)
                .setChanged();


        roller1 = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.TRACKER_BAR_ROLLER))
                .createInstance();
        roller2 = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.TRACKER_BAR_ROLLER))
                .createInstance();

        roller1.translate(getVisualPosition())
                .translate(0, 4.5/16f, 0)
                .center()
                .rotate(new Quaternionf().rotateTo(0, -1, 0, shaft.getStepX(), shaft.getStepY(), shaft.getStepZ()));
        baseTransform1.set(roller1.pose);

        roller2.translate(getVisualPosition())
                .translate(0, -5.5/16f, 0)
                .center()
                .rotate(new Quaternionf().rotateTo(0, -1, 0, shaft.getStepX(), shaft.getStepY(), shaft.getStepZ()));
        baseTransform2.set(roller2.pose);

        animateRollers(partialTick);

    }

    @Override
    public void beginFrame(Context ctx) {
        animateRollers(ctx.partialTick());
    }

    private void animateRollers(float partialTicks) {
        float angle = blockEntity.getRollerAngle(partialTicks);

        roller1.setTransform(baseTransform1)
                .rotateY(angle)
                .uncenter()
                .setChanged();

        roller2.setTransform(baseTransform2)
                .rotateY(angle)
                .uncenter()
                .setChanged();
    }

    @Override
    protected void _delete() {
        rotatingModel.delete();
        roller1.delete();
        roller2.delete();
    }

    @Override
    public void update(float partialTick) {
        rotatingModel.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(rotatingModel, roller1, roller2);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(rotatingModel);
        consumer.accept(roller1);
        consumer.accept(roller2);
    }
}
