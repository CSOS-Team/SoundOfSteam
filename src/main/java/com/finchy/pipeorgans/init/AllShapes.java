package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.block.Generic;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AllShapes {

    public static VoxelShape add(VoxelShape a, VoxelShape b) {
        return Shapes.join(a, b, BooleanOp.OR);
    }

    public static VoxelShape getBase(Direction face) {
        return switch (face) {
            case NORTH -> BASE_NORTH;
            case EAST -> BASE_EAST;
            case SOUTH -> BASE_SOUTH;
            case WEST -> BASE_WEST;
            case UP, DOWN -> null;
        };

    }

    public static VoxelShape getGedecktBase(Generic.WhistleSize size) {
        return switch (size) {
            case SMALL -> GEDECKT_SMALL_BASE;
            case MEDIUM -> GEDECKT_MEDIUM_BASE;
            case LARGE -> GEDECKT_LARGE_BASE;
            case HUGE -> GEDECKT_HUGE_BASE;
        };
    }

    public static VoxelShape getGedecktExtensionShape(Generic.GenericExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case SMALL -> GEDECKT_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> GEDECKT_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> GEDECKT_EXTENSION_LARGE_SINGLE;
                case HUGE -> GEDECKT_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case SMALL -> GEDECKT_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> GEDECKT_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> GEDECKT_EXTENSION_LARGE_DOUBLE;
                case HUGE -> GEDECKT_EXTENSION_HUGE_DOUBLE;
            };
        };
    }

    public static VoxelShape getDiapasonBase(Generic.WhistleSize size) {
        return switch (size) {
            case SMALL -> DIAPASON_SMALL_BASE;
            case MEDIUM -> DIAPASON_MEDIUM_BASE;
            case LARGE -> DIAPASON_LARGE_BASE;
            case HUGE -> DIAPASON_HUGE_BASE;
        };
    }

    public static VoxelShape getDiapasonExtensionShape(Generic.GenericExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case SMALL -> DIAPASON_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> DIAPASON_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> DIAPASON_EXTENSION_LARGE_SINGLE;
                case HUGE -> DIAPASON_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case SMALL -> DIAPASON_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> DIAPASON_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> DIAPASON_EXTENSION_LARGE_DOUBLE;
                case HUGE -> DIAPASON_EXTENSION_HUGE_DOUBLE;
            };
        };
    }

    // GEDECKT

    public static VoxelShape GEDECKT_SMALL_BASE = Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 1, 0.6875);
    public static VoxelShape GEDECKT_MEDIUM_BASE = Shapes.box(0.25, 0.1875, 0.25, 0.75, 1, 0.75);
    public static VoxelShape GEDECKT_LARGE_BASE = Shapes.box(0.1875, 0.1875, 0.1875, 0.8125, 1, 0.8125);
    public static VoxelShape GEDECKT_HUGE_BASE = Shapes.box(0.125, 0.1875, 0.125, 0.875, 1, 0.875);

    public static VoxelShape GEDECKT_EXTENSION_SMALL_SINGLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875);
    public static VoxelShape GEDECKT_EXTENSION_SMALL_DOUBLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);

    public static VoxelShape GEDECKT_EXTENSION_MEDIUM_SINGLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    public static VoxelShape GEDECKT_EXTENSION_MEDIUM_DOUBLE = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);

    public static VoxelShape GEDECKT_EXTENSION_LARGE_SINGLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125);
    public static VoxelShape GEDECKT_EXTENSION_LARGE_DOUBLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125);

    public static VoxelShape GEDECKT_EXTENSION_HUGE_SINGLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.5, 0.875);
    public static VoxelShape GEDECKT_EXTENSION_HUGE_DOUBLE = Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875);

    // DIAPASON

    public static VoxelShape DIAPASON_SMALL_BASE = Shapes.box(0.25, 0.1875, 0.25, 0.75, 1, 0.75);
    public static VoxelShape DIAPASON_MEDIUM_BASE = Shapes.box(0.1875, 0.1875, 0.1875, 0.8125, 1, 0.8125);
    public static VoxelShape DIAPASON_LARGE_BASE = Shapes.box(0.125, 0.1875, 0.125, 0.875, 1, 0.875);
    public static VoxelShape DIAPASON_HUGE_BASE = Shapes.box(0.0625, 0.1875, 0.0625, 0.9375, 1, 0.9375);

    public static VoxelShape DIAPASON_EXTENSION_SMALL_SINGLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    public static VoxelShape DIAPASON_EXTENSION_SMALL_DOUBLE = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);

    public static VoxelShape DIAPASON_EXTENSION_MEDIUM_SINGLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125);
    public static VoxelShape DIAPASON_EXTENSION_MEDIUM_DOUBLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125);

    public static VoxelShape DIAPASON_EXTENSION_LARGE_SINGLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.5, 0.875);
    public static VoxelShape DIAPASON_EXTENSION_LARGE_DOUBLE = Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875);

    public static VoxelShape DIAPASON_EXTENSION_HUGE_SINGLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.5, 0.9375);
    public static VoxelShape DIAPASON_EXTENSION_HUGE_DOUBLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375);



    public static VoxelShape BASE_FLOOR = add(
            Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.1875, 0.9375),
            Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 0.6875, 0.6875)
    );

    public static VoxelShape BASE_NORTH = add(
            Shapes.box(0.0625, 0.0625, 0, 0.9375, 0.9375, 0.1875),
            Shapes.box(0.3125, 0.1875, 0.1875, 0.6875, 0.5625, 0.6875)
    );

    public static VoxelShape BASE_EAST = add(
            Shapes.box(0.8125, 0.0625, 0.0625, 1, 0.9375, 0.9375),
            Shapes.box(0.3125, 0.1875, 0.3125, 0.8125, 0.5625, 0.6875)
    );

    public static VoxelShape BASE_SOUTH = add(
            Shapes.box(0.0625, 0.0625, 0.8125, 0.9375, 0.9375, 1),
            Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 0.5625, 0.8125)
    );

    public static VoxelShape BASE_WEST = add(
            Shapes.box(0, 0.0625, 0.0625, 0.1875, 0.9375, 0.9375),
            Shapes.box(0.1875, 0.1875, 0.3125, 0.6875, 0.5625, 0.6875)
    );

}
