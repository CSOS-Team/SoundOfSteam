package com.finchy.pipeorgans.content.stop;

import com.finchy.pipeorgans.init.AllPartialModels;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class StopVisual extends AbstractBlockEntityVisual<StopBlockEntity> implements SimpleDynamicVisual {

    private static final int COLS = 6;
    private static final int ROWS = 3;
    private static final int COUNT = COLS * ROWS;

    private static final float[] COL_X = { 13f, 11f, 9f, 7f, 5f, 3f };
    private static final float[] ROW_Y = { 10.0f, 8.0f, 6.0f };

    private static final int COLOR_PRESSED = 0xFFFF00;
    private static final int COLOR_UNPRESSED = 0xFFFFFF;
    private static final int COLOR_UNUSED = 0x555555;

    private final TransformedInstance[] knobs = new TransformedInstance[COUNT];

    public StopVisual(VisualizationContext context, StopBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        for (int i = 0; i < COUNT; i++) {
            knobs[i] = instancerProvider()
                    .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.STOP_BUTTON))
                    .createInstance();
        }
    }

    @Override
    public void beginFrame(Context ctx) {
        rebuildAll();
    }

    @Override
    public void update(float partialTick) {
        rebuildAll();
    }

    private void rebuildAll() {
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        float yawDeg = switch (facing) {
            case EAST -> 270; case SOUTH -> 180; case WEST -> 90; default -> 0;
        };
        int light = 0xF000F0; // max light
        List<Stop> stops = blockEntity.getStops();

        for (int i = 0; i < COUNT; i++) {
            int col = i % COLS;
            int row = i / COLS;
            Stop stop = i < stops.size() ? stops.get(i) : null;
            boolean used = stop != null && !stop.isUnused();
            boolean pressed = used && stop.pressed;

            float bodyZShift = (used && pressed) ? 0.5f / 16f : 0f;

            knobs[i].setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(0.5f, 0.5f, 0.5f)
                    .rotateY((float) Math.toRadians(yawDeg))
                    .translate(-0.5f, -0.5f, -0.5f)
                    .translate((COL_X[col] - 8f) / 16f, (ROW_Y[row] - 8f) / 16f, bodyZShift)
                    .light(light)
                    .colorRgb(pressed ? COLOR_PRESSED : (used ? COLOR_UNPRESSED : COLOR_UNUSED))
                    .setChanged();
        }
    }

    @Override
    public void updateLight(float partialTick) {
        rebuildAll();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (TransformedInstance inst : knobs) consumer.accept(inst);
    }

    @Override
    protected void _delete() {
        for (TransformedInstance inst : knobs) inst.delete();
    }
}
