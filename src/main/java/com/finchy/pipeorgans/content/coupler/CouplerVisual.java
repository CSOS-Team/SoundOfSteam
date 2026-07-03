package com.finchy.pipeorgans.content.coupler;

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

public class CouplerVisual extends AbstractBlockEntityVisual<CouplerBlockEntity> implements SimpleDynamicVisual {

    private static final int COLS = 6;
    private static final int ROWS = 2;
    private static final int COUNT = COLS * ROWS;

    private static final float[] COL_X = { 13f, 11f, 9f, 7f, 5f, 3f };
    private static final float[] ROW_Y = { 9.5f, 6.5f };

    private static final int COLOR_WHITE  = 0xbababa;
    private static final int COLOR_BLUE = 0x7ec3de;
    private static final int COLOR_UNUSED = 0x4a4a4a;

    private final TransformedInstance[] bodies = new TransformedInstance[COUNT];
    private final TransformedInstance[] tops   = new TransformedInstance[COUNT];

    public CouplerVisual(VisualizationContext context, CouplerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        for (int i = 0; i < COUNT; i++) {
            bodies[i] = instancerProvider()
                    .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.COUPLER_SWITCH_BODY))
                    .createInstance();
            tops[i] = instancerProvider()
                    .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.COUPLER_SWITCH_TOP))
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
        List<Coupler> couplers = blockEntity.getCouplers();

        for (int i = 0; i < COUNT; i++) {
            int col = i % COLS;
            int row = i / COLS;
            Coupler coupler = i < couplers.size() ? couplers.get(i) : null;
            boolean used = coupler != null && !coupler.isUnused();
            boolean pressed = used && coupler.pressed;

            float bx = (COL_X[col] - 8f) / 16f;
            float by = (ROW_Y[row] - 8f) / 16f;
            float bodyZShift = (used && pressed) ? 0.5f / 16f : 0f;

            bodies[i].setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(0.5f, 0.5f, 0.5f)
                    .rotateY((float) Math.toRadians(yawDeg))
                    .translate(-0.5f, -0.5f, -0.5f)
                    .translate(bx, by, bodyZShift)
                    .light(light)
                    .colorRgb(pressed ? COLOR_BLUE : (used ? COLOR_WHITE : COLOR_UNUSED))
                    .setChanged();

            tops[i].setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(0.5f, 0.5f, 0.5f)
                    .rotateY((float) Math.toRadians(yawDeg))
                    .translate(-0.5f, -0.5f, -0.5f)
                    .translate(bx, by, bodyZShift)
                    .light(light)
                    .colorRgb(pressed ? COLOR_BLUE : (used ? COLOR_WHITE : COLOR_UNUSED))
                    .setChanged();
        }
    }

    @Override
    public void updateLight(float partialTick) {
        rebuildAll();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (int i = 0; i < COUNT; i++) {
            consumer.accept(bodies[i]);
            consumer.accept(tops[i]);
        }
    }

    @Override
    protected void _delete() {
        for (int i = 0; i < COUNT; i++) {
            bodies[i].delete();
            tops[i].delete();
        }
    }
}
