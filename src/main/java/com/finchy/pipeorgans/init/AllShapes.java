package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.content.pipes.generic.EExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AllShapes {

    public static VoxelShape genericPipeShape(EPipeSizes.PipeSize size, boolean wall, Direction facing) {
        VoxelShape base = wall? BASE.get(facing.getOpposite()) : BASE.get(Direction.UP);
        return shape(switch (size) {
                    case TINY -> GENERIC_TINY_BASE;
                    case SMALL -> GENERIC_SMALL_BASE;
                    case MEDIUM -> GENERIC_MEDIUM_BASE;
                    case LARGE -> GENERIC_LARGE_BASE;
                    case HUGE -> GENERIC_HUGE_BASE;
                }
        ).add(base).build();
    }

    public static VoxelShape slimPipeShape(EPipeSizes.PipeSize size, boolean wall, Direction facing) {
        VoxelShape base = wall? BASE.get(facing.getOpposite()) : BASE.get(Direction.UP);
        return shape(switch (size) {
                    case TINY -> SLIM_TINY_BASE;
                    case SMALL -> SLIM_SMALL_BASE;
                    case MEDIUM -> SLIM_MEDIUM_BASE;
                    case LARGE -> SLIM_LARGE_BASE;
                    case HUGE -> SLIM_HUGE_BASE;
                }
        ).add(base).build();
    }

    // generic single extension
    public static VoxelShape genericExtensionShape(EExtensionShapes.SingleShape shape, EPipeSizes.PipeSize size) {
        return switch (size) {
            case TINY -> GENERIC_EXTENSION_TINY_QUAD;
            case SMALL -> GENERIC_EXTENSION_SMALL_QUAD;
            case MEDIUM -> GENERIC_EXTENSION_MEDIUM_QUAD;
            case LARGE -> GENERIC_EXTENSION_LARGE_QUAD;
            case HUGE -> GENERIC_EXTENSION_HUGE_QUAD;
        };
    }

    // generic double extension
    public static VoxelShape genericExtensionShape(EExtensionShapes.DoubleShape shape, EPipeSizes.PipeSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_DOUBLE;
                case SMALL -> GENERIC_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_DOUBLE;
                case HUGE -> GENERIC_EXTENSION_HUGE_DOUBLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_QUAD;
                case SMALL -> GENERIC_EXTENSION_SMALL_QUAD;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_QUAD;
                case LARGE -> GENERIC_EXTENSION_LARGE_QUAD;
                case HUGE -> GENERIC_EXTENSION_HUGE_QUAD;
            };
        };
    }

    // generic quadruple extension
    public static VoxelShape genericExtensionShape(EExtensionShapes.QuadrupleShape shape, EPipeSizes.PipeSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_SINGLE;
                case SMALL -> GENERIC_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_SINGLE;
                case HUGE -> GENERIC_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_DOUBLE;
                case SMALL -> GENERIC_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_DOUBLE;
                case HUGE -> GENERIC_EXTENSION_HUGE_DOUBLE;
            };
            case TRIPLE -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_TRIPLE;
                case SMALL -> GENERIC_EXTENSION_SMALL_TRIPLE;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_TRIPLE;
                case LARGE -> GENERIC_EXTENSION_LARGE_TRIPLE;
                case HUGE -> GENERIC_EXTENSION_HUGE_TRIPLE;
            };
            case QUAD, QUAD_CONNECTED -> switch (size) {
                case TINY -> GENERIC_EXTENSION_TINY_QUAD;
                case SMALL -> GENERIC_EXTENSION_SMALL_QUAD;
                case MEDIUM -> GENERIC_EXTENSION_MEDIUM_QUAD;
                case LARGE -> GENERIC_EXTENSION_LARGE_QUAD;
                case HUGE -> GENERIC_EXTENSION_HUGE_QUAD;
            };
        };
    }

    // slim single extension
    public static VoxelShape slimExtensionShape(EExtensionShapes.SingleShape shape, EPipeSizes.PipeSize size) {
        return switch (size) {
            case TINY -> SLIM_EXTENSION_TINY_QUAD;
            case SMALL -> SLIM_EXTENSION_SMALL_QUAD;
            case MEDIUM -> SLIM_EXTENSION_MEDIUM_QUAD;
            case LARGE -> SLIM_EXTENSION_LARGE_QUAD;
            case HUGE -> SLIM_EXTENSION_HUGE_QUAD;
        };
    }

    // slim double extension
    public static VoxelShape slimExtensionShape(EExtensionShapes.DoubleShape shape, EPipeSizes.PipeSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_DOUBLE;
                case SMALL -> SLIM_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SLIM_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SLIM_EXTENSION_HUGE_DOUBLE;
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_QUAD;
                case SMALL -> SLIM_EXTENSION_SMALL_QUAD;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_QUAD;
                case LARGE -> SLIM_EXTENSION_LARGE_QUAD;
                case HUGE -> SLIM_EXTENSION_HUGE_QUAD;
            };
        };
    }

    // slim quadruple extension
    public static VoxelShape slimExtensionShape(EExtensionShapes.QuadrupleShape shape, EPipeSizes.PipeSize size) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_SINGLE;
                case SMALL -> SLIM_EXTENSION_SMALL_SINGLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_SINGLE;
                case LARGE -> SLIM_EXTENSION_LARGE_SINGLE;
                case HUGE -> SLIM_EXTENSION_HUGE_SINGLE;
            };
            case DOUBLE -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_DOUBLE;
                case SMALL -> SLIM_EXTENSION_SMALL_DOUBLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_DOUBLE;
                case LARGE -> SLIM_EXTENSION_LARGE_DOUBLE;
                case HUGE -> SLIM_EXTENSION_HUGE_DOUBLE;
            };
            case TRIPLE -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_TRIPLE;
                case SMALL -> SLIM_EXTENSION_SMALL_TRIPLE;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_TRIPLE;
                case LARGE -> SLIM_EXTENSION_LARGE_TRIPLE;
                case HUGE -> SLIM_EXTENSION_HUGE_TRIPLE;
            };
            case QUAD, QUAD_CONNECTED -> switch (size) {
                case TINY -> SLIM_EXTENSION_TINY_QUAD;
                case SMALL -> SLIM_EXTENSION_SMALL_QUAD;
                case MEDIUM -> SLIM_EXTENSION_MEDIUM_QUAD;
                case LARGE -> SLIM_EXTENSION_LARGE_QUAD;
                case HUGE -> SLIM_EXTENSION_HUGE_QUAD;
            };
        };
    }



    // DEFINITIONS
    private static final VoxelShape
        // GENERIC
        GENERIC_TINY_BASE = shape(5, 3, 5, 11, 16, 11).build(),
        GENERIC_SMALL_BASE = shape(4, 3, 4, 12, 16, 12).build(),
        GENERIC_MEDIUM_BASE = shape(3, 3, 3, 13, 16, 13).build(),
        GENERIC_LARGE_BASE = shape(2, 3, 2, 14, 16, 14).build(),
        GENERIC_HUGE_BASE = shape(1, 3, 1, 15, 16, 15).build(),

        GENERIC_EXTENSION_TINY_SINGLE = shape(5, 0, 5, 11, 4, 11).build(),
        GENERIC_EXTENSION_TINY_DOUBLE = shape(5, 0, 5, 11, 8, 11).build(),
        GENERIC_EXTENSION_TINY_TRIPLE = shape(5, 0, 5, 11, 12, 11).build(),
        GENERIC_EXTENSION_TINY_QUAD = shape(5, 0, 5, 11, 16, 11).build(),

        GENERIC_EXTENSION_SMALL_SINGLE = shape(4, 0, 4, 12, 4, 12).build(),
        GENERIC_EXTENSION_SMALL_DOUBLE = shape(4, 0, 4, 12, 8, 12).build(),
        GENERIC_EXTENSION_SMALL_TRIPLE = shape(4, 0, 4, 12, 12, 12).build(),
        GENERIC_EXTENSION_SMALL_QUAD = shape(4, 0, 4, 12, 16, 12).build(),

        GENERIC_EXTENSION_MEDIUM_SINGLE = shape(3, 0, 3, 13, 4, 13).build(),
        GENERIC_EXTENSION_MEDIUM_DOUBLE = shape(3, 0, 3, 13, 8, 13).build(),
        GENERIC_EXTENSION_MEDIUM_TRIPLE = shape(3, 0, 3, 13, 12, 13).build(),
        GENERIC_EXTENSION_MEDIUM_QUAD = shape(3, 0, 3, 13, 16, 13).build(),

        GENERIC_EXTENSION_LARGE_SINGLE = shape(2, 0, 2, 14, 4, 14).build(),
        GENERIC_EXTENSION_LARGE_DOUBLE = shape(2, 0, 2, 14, 8, 14).build(),
        GENERIC_EXTENSION_LARGE_TRIPLE = shape(2, 0, 2, 14, 12, 14).build(),
        GENERIC_EXTENSION_LARGE_QUAD = shape(2, 0, 2, 14, 16, 14).build(),

        GENERIC_EXTENSION_HUGE_SINGLE = shape(1, 0, 1, 15, 4, 15).build(),
        GENERIC_EXTENSION_HUGE_DOUBLE = shape(1, 0, 1, 15, 8, 15).build(),
        GENERIC_EXTENSION_HUGE_TRIPLE = shape(1, 0, 1, 15, 12, 15).build(),
        GENERIC_EXTENSION_HUGE_QUAD = shape(1, 0, 1, 15, 16, 15).build(),

        // SLIM
        SLIM_TINY_BASE = shape(6, 4, 6, 10, 16, 10).build(),
        SLIM_SMALL_BASE = shape(5, 3, 5, 11, 16, 11).build(),
        SLIM_MEDIUM_BASE = shape(4, 3, 4, 12, 16, 12).build(),
        SLIM_LARGE_BASE = shape(3, 3, 3, 13, 16, 13).build(),
        SLIM_HUGE_BASE = shape(2, 3, 2, 14, 16, 14).build(),

        SLIM_EXTENSION_TINY_SINGLE = shape(6, 0, 6, 10, 4, 10).build(),
        SLIM_EXTENSION_TINY_DOUBLE = shape(6, 0, 6, 10, 8, 10).build(),
        SLIM_EXTENSION_TINY_TRIPLE = shape(6, 0, 6, 10, 12, 10).build(),
        SLIM_EXTENSION_TINY_QUAD = shape(6, 0, 6, 10, 16, 10).build(),

        SLIM_EXTENSION_SMALL_SINGLE = shape(5, 0, 5, 11, 4, 11).build(),
        SLIM_EXTENSION_SMALL_DOUBLE = shape(5, 0, 5, 11, 8, 11).build(),
        SLIM_EXTENSION_SMALL_TRIPLE = shape(5, 0, 5, 11, 12, 11).build(),
        SLIM_EXTENSION_SMALL_QUAD = shape(5, 0, 5, 11, 16, 11).build(),

        SLIM_EXTENSION_MEDIUM_SINGLE = shape(4, 0, 4, 12, 4, 12).build(),
        SLIM_EXTENSION_MEDIUM_DOUBLE = shape(4, 0, 4, 12, 8, 12).build(),
        SLIM_EXTENSION_MEDIUM_TRIPLE = shape(4, 0, 4, 12, 12, 12).build(),
        SLIM_EXTENSION_MEDIUM_QUAD = shape(4, 0, 4, 12, 16, 12).build(),

        SLIM_EXTENSION_LARGE_SINGLE = shape(3, 0, 3, 13, 4, 13).build(),
        SLIM_EXTENSION_LARGE_DOUBLE = shape(3, 0, 3, 13, 8, 13).build(),
        SLIM_EXTENSION_LARGE_TRIPLE = shape(3, 0, 3, 13, 12, 13).build(),
        SLIM_EXTENSION_LARGE_QUAD = shape(3, 0, 3, 13, 16, 13).build(),

        SLIM_EXTENSION_HUGE_SINGLE = shape(2, 0, 2, 14, 4, 14).build(),
        SLIM_EXTENSION_HUGE_DOUBLE = shape(2, 0, 2, 14, 8, 14).build(),
        SLIM_EXTENSION_HUGE_TRIPLE = shape(2, 0, 2, 14, 12, 14).build(),
        SLIM_EXTENSION_HUGE_QUAD = shape(2, 0, 2, 14, 16, 14).build();



    // OTHER VOXELSHAPERS
    public static final VoxelShaper

        BASE = shape(1, 0, 1, 15, 3, 15)
                .add(5, 3, 5, 11, 11, 11)
                .forDirectional(Direction.UP),

        // this is for the STANDALONE BLOCK called base, NOT the base VoxelShape that is used for pipes!
        // unfortunately their names are... kind of identical
        // shape for floor variant is BASE.get(UP)
        BASE_BLOCK_WALL = shape(1, 1, 0, 15, 15, 3)
                .add(5, 3, 3, 11, 9, 11)
            .add(5, 9, 5, 11, 11, 11)
                .forHorizontal(Direction.NORTH),

        KBR = shape(0, 0, 4, 16, 2, 16)
                .add(0, 2, 6, 16, 16, 16)
                .forHorizontal(Direction.NORTH);



    private static com.simibubi.create.AllShapes.Builder shape(VoxelShape shape) {
        return new com.simibubi.create.AllShapes.Builder(shape);
    }

    private static com.simibubi.create.AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(Block.box(x1, y1, z1, x2, y2, z2));
    }

}
