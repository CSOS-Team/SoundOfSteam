package com.finchy.pipeorgans.content.piston;

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

import java.util.function.Consumer;

public class PistonVisual extends AbstractBlockEntityVisual<PistonBlockEntity> implements SimpleDynamicVisual {

    private static final int PISTON_COUNT = 12;
    private static final int COLS = 6;
    private static final int SPECIAL_COUNT = 4;
    private static final int SPECIAL_TUTTI_IDX = 2;

    private static final float[] COL_X = { 13f, 11f, 9f, 7f, 5f, 3f };
    private static final float[] ROW_Y = { 9.5f, 7.5f };
    private static final float[] SPECIAL_X = { 11f,  9f, 7f, 5f  };
    private static final float SPECIAL_Y = 5.5f;

    private static final int COLOR_SETTINGS = 0xbababa;
    private static final int COLOR_PISTONS = 0x3d3d3d;
    private static final int COLOR_TUTTI = 0xe6e68c;

    private final TransformedInstance[] pistons  = new TransformedInstance[PISTON_COUNT];
    private final TransformedInstance[] specials = new TransformedInstance[SPECIAL_COUNT];

    public PistonVisual(VisualizationContext context, PistonBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        for (int i = 0; i < PISTON_COUNT; i++) {
            pistons[i] = instancerProvider()
                    .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.PISTON_BUTTON))
                    .createInstance();
        }
        for (int i = 0; i < SPECIAL_COUNT; i++) {
            specials[i] = instancerProvider()
                    .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.PISTON_BUTTON))
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
        float yawRad = (float) Math.toRadians(yawDeg);
        int light = 0xF000F0; // max light

        for (int i = 0; i < PISTON_COUNT; i++) {
            int col = i % COLS;
            int row = i / COLS;
            pistons[i].setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(0.5f, 0.5f, 0.5f)
                    .rotateY(yawRad)
                    .translate(-0.5f, -0.5f, -0.5f)
                    .translate((COL_X[col] - 8f) / 16f, (ROW_Y[row] - 8f) / 16f, 0f)
                    .light(light)
                    .colorRgb(blockEntity.isPresetEmpty(i) ? COLOR_PISTONS : COLOR_SETTINGS)
                    .setChanged();
        }


        boolean tuttiActive = blockEntity.isTuttiActive();
        float bodyZShift = (tuttiActive) ? 0.5f / 16f : 0f;

        for (int i = 0; i < SPECIAL_COUNT; i++) {
            specials[i].setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(0.5f, 0.5f, 0.5f)
                    .rotateY(yawRad)
                    .translate(-0.5f, -0.5f, -0.5f)
                    .translate((SPECIAL_X[i] - 8f) / 16f, (SPECIAL_Y - 8f) / 16f, bodyZShift)
                    .light(light)
                    .colorRgb((i == SPECIAL_TUTTI_IDX && tuttiActive) ? COLOR_TUTTI : COLOR_SETTINGS)
                    .setChanged();
        }
    }

    @Override
    public void updateLight(float partialTick) {
        rebuildAll();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (TransformedInstance inst : pistons)  consumer.accept(inst);
        for (TransformedInstance inst : specials) consumer.accept(inst);
    }

    @Override
    protected void _delete() {
        for (TransformedInstance inst : pistons)  inst.delete();
        for (TransformedInstance inst : specials) inst.delete();
    }
}
