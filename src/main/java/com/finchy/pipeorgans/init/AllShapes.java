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

    public static VoxelShape getSlimBase(Generic.WhistleSize size) {
        return switch (size) {
            case TINY -> SLIM_TINY_BASE;
            case SMALL -> SLIM_SMALL_BASE;
            case MEDIUM -> SLIM_MEDIUM_BASE;
            case LARGE -> SLIM_LARGE_BASE;
            case HUGE -> SLIM_HUGE_BASE;
        };
    }

    public static VoxelShape getSlimExtensionShape(Generic.QuadrupleExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case DOUBLE -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_SINGLE;
                case SMALL -> SLIM_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> SLIM_EXTENSION_LARGE_SINGLE;
                case HUGE -> SLIM_EXTENSION_HUGE_SINGLE;
            };
            case QUAD, QUAD_CONNECTED -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_DOUBLE;
                case SMALL -> SLIM_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SLIM_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SLIM_EXTENSION_HUGE_DOUBLE;
            };
            default -> Shapes.block();
        };
    }

    public static VoxelShape getGenericBase(Generic.WhistleSize size) {
        return switch (size) {
            case TINY -> GENERIC_TINY_BASE;
            case SMALL -> GENERIC_SMALL_BASE;
            case MEDIUM -> GENERIC_MEDIUM_BASE;
            case LARGE -> GENERIC_LARGE_BASE;
            case HUGE -> GENERIC_HUGE_BASE;
        };
    }

    public static VoxelShape getGenericBase(Generic.PedalWhistleSize size) {
        return switch (size) {
            case SMALL -> GENERIC_SMALL_BASE;
            case MEDIUM -> GENERIC_MEDIUM_BASE;
            case LARGE -> GENERIC_LARGE_BASE;
            case HUGE -> GENERIC_HUGE_BASE;
        };
    }

    @Deprecated
    public static VoxelShape getGenericExtensionShape(Generic.ExtensionShape shape, Generic.PedalWhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case SMALL -> SHORT_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SHORT_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SHORT_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SHORT_EXTENSION_HUGE_DOUBLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case SMALL -> SHORT_EXTENSION_SMALL_QUADRUPLE;
                case MEDIUM -> SHORT_EXTENSION_MEDIUM_QUADRUPLE;
                case LARGE -> SHORT_EXTENSION_LARGE_QUADRUPLE;
                case HUGE -> SHORT_EXTENSION_HUGE_QUADRUPLE;
            };
        };
    }

    public static VoxelShape getQuadrupleExtensionShape(Generic.QuadrupleExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> SHORT_EXTENSION_TINY_SINGLE;
                case SMALL -> SHORT_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> SHORT_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> SHORT_EXTENSION_LARGE_SINGLE;
                case HUGE -> SHORT_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE -> switch (size) {
                case TINY -> SHORT_EXTENSION_TINY_DOUBLE;
                case SMALL -> SHORT_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SHORT_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SHORT_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SHORT_EXTENSION_HUGE_DOUBLE;
            };
            case TRIPLE -> switch (size) {
                case TINY -> SHORT_EXTENSION_TINY_TRIPLE;
                case SMALL -> SHORT_EXTENSION_SMALL_TRIPLE;
                case MEDIUM -> SHORT_EXTENSION_MEDIUM_TRIPLE;
                case LARGE -> SHORT_EXTENSION_LARGE_TRIPLE;
                case HUGE -> SHORT_EXTENSION_HUGE_TRIPLE;
            };
            case QUAD, QUAD_CONNECTED -> switch (size) {
                case TINY -> SHORT_EXTENSION_TINY_QUADRUPLE;
                case SMALL -> SHORT_EXTENSION_SMALL_QUADRUPLE;
                case MEDIUM -> SHORT_EXTENSION_MEDIUM_QUADRUPLE;
                case LARGE -> SHORT_EXTENSION_LARGE_QUADRUPLE;
                case HUGE -> SHORT_EXTENSION_HUGE_QUADRUPLE;
            };
        };
    }

    public static VoxelShape getTrompetteBase(Generic.WhistleSize size) {
        return switch (size) {
            case TINY -> SLIM_SMALL_BASE;
            case SMALL -> TROMPETTE_SMALL_BASE;
            case MEDIUM -> TROMPETTE_MEDIUM_BASE;
            case LARGE -> TROMPETTE_LARGE_BASE;
            case HUGE -> TROMPETTE_HUGE_BASE;
        };
    }

    public static VoxelShape getTrompetteExtensionShape(Generic.ExtensionShape shape, Generic.WhistleSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY, SMALL -> TROMPETTE_SMALL_SINGLE;
                case MEDIUM -> TROMPETTE_MEDIUM_SINGLE;
                case LARGE -> TROMPETTE_LARGE_SINGLE;
                case HUGE -> TROMPETTE_HUGE_SINGLE;
            };
            case DOUBLE -> switch (size) {
                case TINY, SMALL -> TROMPETTE_SMALL_DOUBLE;
                case MEDIUM -> TROMPETTE_MEDIUM_DOUBLE;
                case LARGE -> TROMPETTE_LARGE_DOUBLE;
                case HUGE -> TROMPETTE_HUGE_DOUBLE;
            };
            case DOUBLE_CONNECTED -> switch (size) {
                case TINY, SMALL -> TROMPETTE_SMALL_DOUBLE_CONNECTED;
                case MEDIUM -> TROMPETTE_MEDIUM_DOUBLE_CONNECTED;
                case LARGE -> TROMPETTE_LARGE_DOUBLE_CONNECTED;
                case HUGE -> TROMPETTE_HUGE_DOUBLE_CONNECTED;
            };
        };
    }

    // TROMPETTE

    public static VoxelShape TROMPETTE_TINY_BASE = add(Shapes.box(0.3125, 0.6875, 0.3125, 0.6875, 0.8125, 0.6875), Shapes.box(0.375, 0.25, 0.375, 0.625, 1, 0.625));
    public static VoxelShape TROMPETTE_SMALL_BASE = add(Shapes.box(0.25, 0.6875, 0.25, 0.75, 0.8125, 0.75), Shapes.box(0.3125, 0.25, 0.3125, 0.6875, 1, 0.6875) );
    public static VoxelShape TROMPETTE_MEDIUM_BASE = add(Shapes.box(0.1875, 0.6875, 0.1875, 0.8125, 0.8125, 0.8125), Shapes.box(0.25, 0.25, 0.25, 0.75, 1, 0.75));
    public static VoxelShape TROMPETTE_LARGE_BASE = add(Shapes.box(0.125, 0.6875, 0.125, 0.875, 0.8125, 0.875), Shapes.box(0.1875, 0.25, 0.1875, 0.8125, 1, 0.8125));
    public static VoxelShape TROMPETTE_HUGE_BASE = add(Shapes.box(0.065625, 0.6875, 0.0625, 0.934375, 0.8125, 0.9375), Shapes.box(0.125, 0.25, 0.125, 0.875, 1, 0.875));

    public static VoxelShape TROMPETTE_SMALL_SINGLE = add(Shapes.box(0.375, 0, 0.375, 0.625, 0.5, 0.625), Shapes.box(0.3125, 0.5, 0.3125, 0.6875, 0.625, 0.6875));
    public static VoxelShape TROMPETTE_SMALL_DOUBLE = add(add(Shapes.box(0.375, 0.5, 0.375, 0.625, 1, 0.625), Shapes.box(0.4375, 0, 0.4375, 0.5625, 0.5, 0.5625)), Shapes.box(0.3125, 1, 0.3125, 0.6875, 1.125, 0.6875));
    public static VoxelShape TROMPETTE_SMALL_DOUBLE_CONNECTED = Shapes.box(0.4375, 0, 0.4375, 0.5625, 1, 0.5625);

    public static VoxelShape TROMPETTE_MEDIUM_SINGLE = add(Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875), Shapes.box(0.25, 0.5, 0.25, 0.75, 0.625, 0.75));
    public static VoxelShape TROMPETTE_MEDIUM_DOUBLE = add(add(Shapes.box(0.3125, 0.5, 0.3125, 0.6875, 1, 0.6875), Shapes.box(0.375, 0, 0.375, 0.625, 0.5, 0.625)), Shapes.box(0.25, 1, 0.25, 0.75, 1.125, 0.75));
    public static VoxelShape TROMPETTE_MEDIUM_DOUBLE_CONNECTED = Shapes.box(0.375, 0, 0.375, 0.625, 1, 0.625);

    public static VoxelShape TROMPETTE_LARGE_SINGLE = add(Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75), Shapes.box(0.1875, 0.5, 0.1875, 0.8125, 0.625, 0.8125));
    public static VoxelShape TROMPETTE_LARGE_DOUBLE = add(add(Shapes.box(0.25, 0.5, 0.25, 0.75, 1, 0.75), Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875)), Shapes.box(0.1875, 1, 0.1875, 0.8125, 1.125, 0.8125));
    public static VoxelShape TROMPETTE_LARGE_DOUBLE_CONNECTED = Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);

    public static VoxelShape TROMPETTE_HUGE_SINGLE = add(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125), Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125));
    public static VoxelShape TROMPETTE_HUGE_DOUBLE = add(add(Shapes.box(0.1875, 0.5, 0.1875, 0.8125, 1, 0.8125), Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75)), Shapes.box(0.125, 1, 0.125, 0.875, 1.125, 0.875));
    public static VoxelShape TROMPETTE_HUGE_DOUBLE_CONNECTED = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);

    // SLIM

    public static VoxelShape SLIM_TINY_BASE = Shapes.box(0.375, 0.25, 0.375, 0.625, 1, 0.625);
    public static VoxelShape SLIM_SMALL_BASE = Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 1, 0.6875);
    public static VoxelShape SLIM_MEDIUM_BASE = Shapes.box(0.25, 0.1875, 0.25, 0.75, 1, 0.75);
    public static VoxelShape SLIM_LARGE_BASE = Shapes.box(0.1875, 0.1875, 0.1875, 0.8125, 1, 0.8125);
    public static VoxelShape SLIM_HUGE_BASE = Shapes.box(0.125, 0.1875, 0.125, 0.875, 1, 0.875);

    public static VoxelShape SLIM_EXTENSION_TINY_SINGLE = Shapes.box(0.375, 0, 0.375, 0.625, 0.5, 0.625);
    public static VoxelShape SLIM_EXTENSION_TINY_DOUBLE = Shapes.box(0.375, 0, 0.375, 0.625, 1, 0.625);

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


    // SHORT

    public static VoxelShape SHORT_EXTENSION_TINY_SINGLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.25, 0.6875);
    public static VoxelShape SHORT_EXTENSION_TINY_DOUBLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875);
    public static VoxelShape SHORT_EXTENSION_TINY_TRIPLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.75, 0.6875);
    public static VoxelShape SHORT_EXTENSION_TINY_QUADRUPLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);

    public static VoxelShape SHORT_EXTENSION_SMALL_SINGLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.25, 0.75);
    public static VoxelShape SHORT_EXTENSION_SMALL_DOUBLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    public static VoxelShape SHORT_EXTENSION_SMALL_TRIPLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.75, 0.75);
    public static VoxelShape SHORT_EXTENSION_SMALL_QUADRUPLE = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);

    public static VoxelShape SHORT_EXTENSION_MEDIUM_SINGLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.25, 0.8125);
    public static VoxelShape SHORT_EXTENSION_MEDIUM_DOUBLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125);
    public static VoxelShape SHORT_EXTENSION_MEDIUM_TRIPLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.75, 0.8125);
    public static VoxelShape SHORT_EXTENSION_MEDIUM_QUADRUPLE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125);

    public static VoxelShape SHORT_EXTENSION_LARGE_SINGLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.25, 0.875);
    public static VoxelShape SHORT_EXTENSION_LARGE_DOUBLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.5, 0.875);
    public static VoxelShape SHORT_EXTENSION_LARGE_TRIPLE = Shapes.box(0.125, 0, 0.125, 0.875, 0.75, 0.875);
    public static VoxelShape SHORT_EXTENSION_LARGE_QUADRUPLE = Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875);

    public static VoxelShape SHORT_EXTENSION_HUGE_SINGLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.25, 0.9375);
    public static VoxelShape SHORT_EXTENSION_HUGE_DOUBLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.5, 0.9375);
    public static VoxelShape SHORT_EXTENSION_HUGE_TRIPLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.75, 0.9375);
    public static VoxelShape SHORT_EXTENSION_HUGE_QUADRUPLE = Shapes.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375);

    // BASES

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
