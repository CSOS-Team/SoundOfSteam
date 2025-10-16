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
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class TrackerBarVisual extends KineticBlockEntityVisual<TrackerBarBlockEntity> implements SimpleDynamicVisual {

    private final RotatingInstance rotatingModel;
    private final TransformedInstance roller1;
    private final TransformedInstance roller2;

    public TrackerBarVisual(VisualizationContext context, TrackerBarBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        roller1 = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.TRACKER_BAR_ROLLER))
                .createInstance();

        roller2 = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.TRACKER_BAR_ROLLER))
                .createInstance();

        animateRollers(partialTick);

        rotatingModel = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.TRACKER_BAR_SHAFT))
                .createInstance();

        rotatingModel.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise())
                .setChanged();

    }

    @Override
    public void beginFrame(Context ctx) {
        animateRollers(ctx.partialTick());
    }

    private void animateRollers(float partialTicks) {
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();
        float angle = blockEntity.getRollerAngle(partialTicks);

        roller1.setIdentityTransform()
                .translate(getVisualPosition())
                .center()
                .rotate(angle, Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis()))
                .rotate(new Quaternionf().rotateTo(1, 0, 0, facing.getStepX(), facing.getStepY(), facing.getStepZ()))
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
