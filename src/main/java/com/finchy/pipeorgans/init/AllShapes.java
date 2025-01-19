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
            case SMALL -> SLIM_SMALL_BASE;
            case MEDIUM -> SLIM_MEDIUM_BASE;
            case LARGE -> SLIM_LARGE_BASE;
            case HUGE -> SLIM_HUGE_BASE;
        };
    }

    public static VoxelShape getGedecktExtensionShape(Generic.GenericExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case SMALL -> SLIM_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> SLIM_EXTENSION_LARGE_SINGLE;
                case HUGE -> SLIM_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case SMALL -> SLIM_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SLIM_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SLIM_EXTENSION_HUGE_DOUBLE;
            };
        };
    }

    public static VoxelShape getDiapasonBase(Generic.WhistleSize size) {
        return switch (size) {
            case SMALL -> GENERIC_SMALL_BASE;
            case MEDIUM -> GENERIC_MEDIUM_BASE;
            case LARGE -> GENERIC_LARGE_BASE;
            case HUGE -> GENERIC_HUGE_BASE;
        };
    }

    public static VoxelShape getDiapasonExtensionShape(Generic.GenericExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case SMALL -> GENERIC_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_SINGLE;
                case HUGE -> GENERIC_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case SMALL -> GENERIC_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_DOUBLE;
                case HUGE -> GENERIC_EXTENSION_HUGE_DOUBLE;
            };
        };
    }

    public static VoxelShape getGambaBase(Generic.SmallWhistleSize size) {
        return switch (size) {
            case TINY -> GENERIC_TINY_BASE;
            case SMALL -> GENERIC_SMALL_BASE;
            case MEDIUM -> GENERIC_MEDIUM_BASE;
            case LARGE -> GENERIC_LARGE_BASE;

        };
    }

    public static VoxelShape getGambaExtensionShape(Generic.GenericExtensionShape shape, Generic.SmallWhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_SINGLE;
                case SMALL -> GENERIC_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_SINGLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_DOUBLE;
                case SMALL -> GENERIC_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_DOUBLE;
            };
        };
    }

    public static VoxelShape getReedBase(Generic.WhistleSize size) {
        return switch (size) {
            case SMALL -> SLIM_SMALL_BASE;
            case MEDIUM -> SLIM_MEDIUM_BASE;
            case LARGE -> SLIM_LARGE_BASE;
            case HUGE -> SLIM_HUGE_BASE;
        };
    }

    public static VoxelShape getReedExtensionShape(Generic.GenericExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case SMALL -> SLIM_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> SLIM_EXTENSION_LARGE_SINGLE;
                case HUGE -> SLIM_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case SMALL -> SLIM_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SLIM_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SLIM_EXTENSION_HUGE_DOUBLE;
            };
        };
    }

    // SLIM

    public static VoxelShape SLIM_SMALL_BASE = Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 1, 0.6875);
    public static VoxelShape SLIM_MEDIUM_BASE = Shapes.box(0.25, 0.1875, 0.25, 0.75, 1, 0.75);
    public static VoxelShape SLIM_LARGE_BASE = Shapes.box(0.1875, 0.1875, 0.1875, 0.8125, 1, 0.8125);
    public static VoxelShape SLIM_HUGE_BASE = Shapes.box(0.125, 0.1875, 0.125, 0.875, 1, 0.875);

    public static VoxelShape SLIM_EXTENSION_SMALL_SINGLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875);
    public static VoxelShape SLIM_EXTENSION_SMALL_DOUBLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);

    public static VoxelShape SLIM_EXTENSION_MEDIUM_SINGLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    public static VoxelShape SLIM_EXTENSION_MEDIUM_DOUBLE = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);

    public static VoxelShape SLIM_EXTENSION_LARGE_SINGLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125);
    public static VoxelShape SLIM_EXTENSION_LARGE_DOUBLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125);

    public static VoxelShape SLIM_EXTENSION_HUGE_SINGLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.5, 0.875);
    public static VoxelShape SLIM_EXTENSION_HUGE_DOUBLE = Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875);

    // GENERIC

    public static VoxelShape GENERIC_TINY_BASE = Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 1, 0.6875);
    public static VoxelShape GENERIC_SMALL_BASE = Shapes.box(0.25, 0.1875, 0.25, 0.75, 1, 0.75);
    public static VoxelShape GENERIC_MEDIUM_BASE = Shapes.box(0.1875, 0.1875, 0.1875, 0.8125, 1, 0.8125);
    public static VoxelShape GENERIC_LARGE_BASE = Shapes.box(0.125, 0.1875, 0.125, 0.875, 1, 0.875);
    public static VoxelShape GENERIC_HUGE_BASE = Shapes.box(0.0625, 0.1875, 0.0625, 0.9375, 1, 0.9375);

    public static VoxelShape GENERIC_EXTENSION_TINY_SINGLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875);
    public static VoxelShape GENERIC_EXTENSION_TINY_DOUBLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);

    public static VoxelShape GENERIC_EXTENSION_SMALL_SINGLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    public static VoxelShape GENERIC_EXTENSION_SMALL_DOUBLE = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);

    public static VoxelShape GENERIC_EXTENSION_MEDIUM_SINGLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125);
    public static VoxelShape GENERIC_EXTENSION_MEDIUM_DOUBLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125);

    public static VoxelShape GENERIC_EXTENSION_LARGE_SINGLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.5, 0.875);
    public static VoxelShape GENERIC_EXTENSION_LARGE_DOUBLE = Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875);

    public static VoxelShape GENERIC_EXTENSION_HUGE_SINGLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.5, 0.9375);
    public static VoxelShape GENERIC_EXTENSION_HUGE_DOUBLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375);



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
