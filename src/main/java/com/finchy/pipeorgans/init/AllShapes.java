package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AllShapes {

    //The generic shape
    public static VoxelShape genericPipeShape(PipeSize size, boolean wall, Direction facing) {
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
    //Used for Reed Pipes (e.g. Trompette)
    public static VoxelShape slimPipeShape(PipeSize size, boolean wall, Direction facing) {
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
    //Used for string pipes (e.g. Viola)
    public static VoxelShape stringPipeShape(PipeSize size, boolean wall, Direction facing) {
        VoxelShape base = wall? BASE.get(facing.getOpposite()) : BASE.get(Direction.UP);
        return shape(switch (size) {
                    case TINY -> STRING_TINY_BASE.get(facing);
                    case SMALL -> STRING_SMALL_BASE.get(facing);
                    case MEDIUM -> STRING_MEDIUM_BASE.get(facing);
                    case LARGE -> STRING_LARGE_BASE.get(facing);
                    case HUGE -> STRING_HUGE_BASE.get(facing);
                }
        ).add(base).build();
    }
    //Special shapes for horizontal pipes (e.g. Chamade)
    public static VoxelShape horizontalPipeShape(PipeSize size, boolean wall, Direction facing) {
        VoxelShape base = wall? BASE.get(facing.getOpposite()) : BASE.get(Direction.UP);
        return shape(switch (size) {
                    case TINY -> HORIZONTAL_TINY_BASE.get(facing);
                    case SMALL -> HORIZONTAL_SMALL_BASE.get(facing);
                    case MEDIUM -> HORIZONTAL_MEDIUM_BASE.get(facing);
                    case LARGE -> HORIZONTAL_LARGE_BASE.get(facing);
                    case HUGE -> HORIZONTAL_HUGE_BASE.get(facing);
                }
        ).add(base).build();
    }

    // generic single extension
    public static VoxelShape genericExtensionShape(ExtensionShapes.Single shape, PipeSize size, Direction facing) {
        return switch (size) {
            case TINY -> GENERIC_EXTENSION_TINY_QUAD;
            case SMALL -> GENERIC_EXTENSION_SMALL_QUAD;
            case MEDIUM -> GENERIC_EXTENSION_MEDIUM_QUAD;
            case LARGE -> GENERIC_EXTENSION_LARGE_QUAD;
            case HUGE -> GENERIC_EXTENSION_HUGE_QUAD;
        };
    }

    // generic double extension
    public static VoxelShape genericExtensionShape(ExtensionShapes.Double shape, PipeSize size, Direction facing) {
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
    public static VoxelShape genericExtensionShape(ExtensionShapes.Quadruple shape, PipeSize size, Direction facing) {
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
    public static VoxelShape slimExtensionShape(ExtensionShapes.Single shape, PipeSize size, Direction facing) {
        return switch (size) {
            case TINY -> SLIM_EXTENSION_TINY_QUAD;
            case SMALL -> SLIM_EXTENSION_SMALL_QUAD;
            case MEDIUM -> SLIM_EXTENSION_MEDIUM_QUAD;
            case LARGE -> SLIM_EXTENSION_LARGE_QUAD;
            case HUGE -> SLIM_EXTENSION_HUGE_QUAD;
        };
    }

    // slim double extension
    public static VoxelShape slimExtensionShape(ExtensionShapes.Double shape, PipeSize size, Direction facing) {
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
    public static VoxelShape slimExtensionShape(ExtensionShapes.Quadruple shape, PipeSize size, Direction facing) {
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

    // slim single horizontal extension
    public static VoxelShape horizontalExtensionShape(ExtensionShapes.Single shape, PipeSize size, Direction facing) {
        return switch (size) {
            case TINY -> HORIZONTAL_EXTENSION_TINY_QUAD.get(facing);
            case SMALL -> HORIZONTAL_EXTENSION_SMALL_QUAD.get(facing);
            case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_QUAD.get(facing);
            case LARGE -> HORIZONTAL_EXTENSION_LARGE_QUAD.get(facing);
            case HUGE -> HORIZONTAL_EXTENSION_HUGE_QUAD.get(facing);
        };
    }

    // slim double horizontal extension
    public static VoxelShape horizontalExtensionShape(ExtensionShapes.Double shape, PipeSize size, Direction facing) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> HORIZONTAL_EXTENSION_TINY_DOUBLE.get(facing);
                case SMALL -> HORIZONTAL_EXTENSION_SMALL_DOUBLE.get(facing);
                case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_DOUBLE.get(facing);
                case LARGE -> HORIZONTAL_EXTENSION_LARGE_DOUBLE.get(facing);
                case HUGE -> HORIZONTAL_EXTENSION_HUGE_DOUBLE.get(facing);
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case TINY -> HORIZONTAL_EXTENSION_TINY_QUAD.get(facing);
                case SMALL -> HORIZONTAL_EXTENSION_SMALL_QUAD.get(facing);
                case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_QUAD.get(facing);
                case LARGE -> HORIZONTAL_EXTENSION_LARGE_QUAD.get(facing);
                case HUGE -> HORIZONTAL_EXTENSION_HUGE_QUAD.get(facing);
            };
        };
    }
    // haha nvm we haven't made all the voxelshapes for this one yet
    /*
    // slim quadruple horizontal extension
    public static VoxelShape horizontalExtensionShape(ExtensionShapes.Quadruple shape, PipeSize size, Direction facing) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> HORIZONTAL_EXTENSION_TINY_SINGLE.get(facing);
                case SMALL -> HORIZONTAL_EXTENSION_SMALL_SINGLE.get(facing);
                case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_SINGLE.get(facing);
                case LARGE -> HORIZONTAL_EXTENSION_LARGE_SINGLE.get(facing);
                case HUGE -> HORIZONTAL_EXTENSION_HUGE_SINGLE.get(facing);
            };
            case DOUBLE -> switch (size) {
                case TINY -> HORIZONTAL_EXTENSION_TINY_DOUBLE.get(facing);
                case SMALL -> HORIZONTAL_EXTENSION_SMALL_DOUBLE.get(facing);
                case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_DOUBLE.get(facing);
                case LARGE -> HORIZONTAL_EXTENSION_LARGE_DOUBLE.get(facing);
                case HUGE -> HORIZONTAL_EXTENSION_HUGE_DOUBLE.get(facing);
            };
            case TRIPLE -> switch (size) {
                case TINY -> HORIZONTAL_EXTENSION_TINY_TRIPLE.get(facing);
                case SMALL -> HORIZONTAL_EXTENSION_SMALL_TRIPLE.get(facing);
                case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_TRIPLE.get(facing);
                case LARGE -> HORIZONTAL_EXTENSION_LARGE_TRIPLE.get(facing);
                case HUGE -> HORIZONTAL_EXTENSION_HUGE_TRIPLE.get(facing);
            };
            case QUAD, QUAD_CONNECTED -> switch (size) {
                case TINY -> HORIZONTAL_EXTENSION_TINY_QUAD.get(facing);
                case SMALL -> HORIZONTAL_EXTENSION_SMALL_QUAD.get(facing);
                case MEDIUM -> HORIZONTAL_EXTENSION_MEDIUM_QUAD.get(facing);
                case LARGE -> HORIZONTAL_EXTENSION_LARGE_QUAD.get(facing);
                case HUGE -> HORIZONTAL_EXTENSION_HUGE_QUAD.get(facing);
            };
        };
    }
    */

    // string single extension
    public static VoxelShape stringExtensionShape(ExtensionShapes.Single shape, PipeSize size, Direction facing) {
        return switch (size) {
            case TINY -> STRING_EXTENSION_TINY_QUAD.get(facing);
            case SMALL -> STRING_EXTENSION_SMALL_QUAD.get(facing);
            case MEDIUM -> STRING_EXTENSION_MEDIUM_QUAD.get(facing);
            case LARGE -> STRING_EXTENSION_LARGE_QUAD.get(facing);
            case HUGE -> STRING_EXTENSION_HUGE_QUAD.get(facing);
        };
    }

    // string double extension
    public static VoxelShape stringExtensionShape(ExtensionShapes.Double shape, PipeSize size, Direction facing) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> STRING_EXTENSION_TINY_DOUBLE.get(facing);
                case SMALL -> STRING_EXTENSION_SMALL_DOUBLE.get(facing);
                case MEDIUM -> STRING_EXTENSION_MEDIUM_DOUBLE.get(facing);
                case LARGE -> STRING_EXTENSION_LARGE_DOUBLE.get(facing);
                case HUGE -> STRING_EXTENSION_HUGE_DOUBLE.get(facing);
            };
            case DOUBLE, DOUBLE_CONNECTED -> switch (size) {
                case TINY -> STRING_EXTENSION_TINY_QUAD.get(facing);
                case SMALL -> STRING_EXTENSION_SMALL_QUAD.get(facing);
                case MEDIUM -> STRING_EXTENSION_MEDIUM_QUAD.get(facing);
                case LARGE -> STRING_EXTENSION_LARGE_QUAD.get(facing);
                case HUGE -> STRING_EXTENSION_HUGE_QUAD.get(facing);
            };
        };
    }

    // string quadruple extension
    public static VoxelShape stringExtensionShape(ExtensionShapes.Quadruple shape, PipeSize size, Direction facing) {
        return switch (shape) {
            case SINGLE -> switch (size) {
                case TINY -> STRING_EXTENSION_TINY_SINGLE.get(facing);
                case SMALL -> STRING_EXTENSION_SMALL_SINGLE.get(facing);
                case MEDIUM -> STRING_EXTENSION_MEDIUM_SINGLE.get(facing);
                case LARGE -> STRING_EXTENSION_LARGE_SINGLE.get(facing);
                case HUGE -> STRING_EXTENSION_HUGE_SINGLE.get(facing);
            };
            case DOUBLE -> switch (size) {
                case TINY -> STRING_EXTENSION_TINY_DOUBLE.get(facing);
                case SMALL -> STRING_EXTENSION_SMALL_DOUBLE.get(facing);
                case MEDIUM -> STRING_EXTENSION_MEDIUM_DOUBLE.get(facing);
                case LARGE -> STRING_EXTENSION_LARGE_DOUBLE.get(facing);
                case HUGE -> STRING_EXTENSION_HUGE_DOUBLE.get(facing);
            };
            case TRIPLE -> switch (size) {
                case TINY -> STRING_EXTENSION_TINY_TRIPLE.get(facing);
                case SMALL -> STRING_EXTENSION_SMALL_TRIPLE.get(facing);
                case MEDIUM -> STRING_EXTENSION_MEDIUM_TRIPLE.get(facing);
                case LARGE -> STRING_EXTENSION_LARGE_TRIPLE.get(facing);
                case HUGE -> STRING_EXTENSION_HUGE_TRIPLE.get(facing);
            };
            case QUAD, QUAD_CONNECTED -> switch (size) {
                case TINY -> STRING_EXTENSION_TINY_QUAD.get(facing);
                case SMALL -> STRING_EXTENSION_SMALL_QUAD.get(facing);
                case MEDIUM -> STRING_EXTENSION_MEDIUM_QUAD.get(facing);
                case LARGE -> STRING_EXTENSION_LARGE_QUAD.get(facing);
                case HUGE -> STRING_EXTENSION_HUGE_QUAD.get(facing);
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

        // STRING PIPES

    private static final VoxelShaper // apologies for the brief interruption...

        STRING_TINY_BASE = shape(5, 3, 5, 11, 16, 11).forHorizontal(Direction.NORTH),
        STRING_SMALL_BASE = shape(5, 3, 4, 11, 16, 12).forHorizontal(Direction.NORTH),
        STRING_MEDIUM_BASE = shape(4, 3, 3, 12, 16, 13).forHorizontal(Direction.NORTH),
        STRING_LARGE_BASE = shape(3, 3, 2, 13, 16, 14).forHorizontal(Direction.NORTH),
        STRING_HUGE_BASE = shape(2, 3, 1, 14, 16, 15).forHorizontal(Direction.NORTH),

        STRING_EXTENSION_TINY_SINGLE = shape(5, 0, 5, 11, 4, 11).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_TINY_DOUBLE = shape(5, 0, 5, 11, 8, 11).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_TINY_TRIPLE = shape(5, 0, 5, 11, 12, 11).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_TINY_QUAD = shape(5, 0, 5, 11, 16, 11).forHorizontal(Direction.NORTH),

        STRING_EXTENSION_SMALL_SINGLE = shape(5, 0, 4, 11, 4, 12).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_SMALL_DOUBLE = shape(5, 0, 4, 11, 8, 12).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_SMALL_TRIPLE = shape(5, 0, 4, 11, 12, 12).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_SMALL_QUAD = shape(5, 0, 4, 11, 16, 12).forHorizontal(Direction.NORTH),

        STRING_EXTENSION_MEDIUM_SINGLE = shape(4, 0, 3, 12, 4, 13).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_MEDIUM_DOUBLE = shape(4, 0, 3, 12, 8, 13).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_MEDIUM_TRIPLE = shape(4, 0, 3, 12, 12, 13).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_MEDIUM_QUAD = shape(4, 0, 3, 12, 16, 13).forHorizontal(Direction.NORTH),

        STRING_EXTENSION_LARGE_SINGLE = shape(3, 0, 2, 13, 4, 14).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_LARGE_DOUBLE = shape(3, 0, 2, 13, 8, 14).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_LARGE_TRIPLE = shape(3, 0, 2, 13, 12, 14).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_LARGE_QUAD = shape(3, 0, 2, 13, 16, 14).forHorizontal(Direction.NORTH),

        STRING_EXTENSION_HUGE_SINGLE = shape(2, 0, 1, 14, 4, 15).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_HUGE_DOUBLE = shape(2, 0, 1, 14, 8, 15).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_HUGE_TRIPLE = shape(2, 0, 1, 14, 12, 15).forHorizontal(Direction.NORTH),
        STRING_EXTENSION_HUGE_QUAD = shape(2, 0, 1, 14, 16, 15).forHorizontal(Direction.NORTH);

    // HORIZONTAL PIPES

    private static final VoxelShaper

    //(so just the chamades)

    HORIZONTAL_TINY_BASE = shape(5, 5, 3, 11, 11, 16).forHorizontal(Direction.NORTH),
    HORIZONTAL_SMALL_BASE = shape(5, 5, 3, 11, 11, 16).forHorizontal(Direction.NORTH),
    HORIZONTAL_MEDIUM_BASE = shape(4, 4, 3, 12, 12, 16).forHorizontal(Direction.NORTH),
    HORIZONTAL_LARGE_BASE = shape(3, 3, 3, 13, 13, 16).forHorizontal(Direction.NORTH),
    HORIZONTAL_HUGE_BASE = shape(2, 2, 3, 14, 14, 16).forHorizontal(Direction.NORTH),

    HORIZONTAL_EXTENSION_TINY_DOUBLE = shape(5, 5, 0, 11, 11, 8).forHorizontal(Direction.NORTH),
    HORIZONTAL_EXTENSION_TINY_QUAD = shape(5, 5, 0, 11, 11, 16).forHorizontal(Direction.NORTH),

    HORIZONTAL_EXTENSION_SMALL_DOUBLE = shape(5, 5, 0, 11, 11, 8).forHorizontal(Direction.NORTH),
    HORIZONTAL_EXTENSION_SMALL_QUAD= shape(5, 5, 0, 11, 11, 16).forHorizontal(Direction.NORTH),

    HORIZONTAL_EXTENSION_MEDIUM_DOUBLE = shape(4, 4, 0, 12, 12, 8).forHorizontal(Direction.NORTH),
    HORIZONTAL_EXTENSION_MEDIUM_QUAD = shape(4, 4, 0, 12, 12, 16).forHorizontal(Direction.NORTH),

    HORIZONTAL_EXTENSION_LARGE_DOUBLE = shape(3, 3, 0, 13, 13, 8).forHorizontal(Direction.NORTH),
    HORIZONTAL_EXTENSION_LARGE_QUAD = shape(3, 3, 0, 13, 13, 16).forHorizontal(Direction.NORTH),

    HORIZONTAL_EXTENSION_HUGE_DOUBLE = shape(2,2,0,14,14,8).forHorizontal(Direction.NORTH),
    HORIZONTAL_EXTENSION_HUGE_QUAD = shape(2, 2, 0, 14, 14, 16).forHorizontal(Direction.NORTH);


    // ...back to our scheduled programming
    // ^ this joke will become a lot funnier once we actually add other VoxelShapes



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

        //Keyboard Relay
        KBR = shape(0, 0, 4, 16, 2, 16)
                .add(0, 2, 6, 16, 16, 16)
                .forHorizontal(Direction.NORTH),

        //Tracker Bar
        TRACKER_BAR = shape(0, 0, 0, 16, 3, 2)
                .add(0, 0, 2, 16, 16, 16)
                .add(0, 14, 0, 16, 16, 2)
                .forHorizontal(Direction.NORTH),

        //Roll Puncher
        ROLL_PUNCHER_HITBOX = shape(4, 0, 4, 12, 13, 12)
                .add(0, 10, 2, 16, 13, 6)
                .add(0, 11.5, 6, 16, 14.5, 10)
                .add(0, 13, 10, 16, 16, 14)
                .forHorizontal(Direction.NORTH),

        ROLL_PUNCHER_COLLISION_BOX = shape(4, 0, 4, 12, 12, 12)
                .add(0, 11, 10, 16, 13, 14)
                .forHorizontal(Direction.NORTH);




    private static com.simibubi.create.AllShapes.Builder shape(VoxelShape shape) {
        return new com.simibubi.create.AllShapes.Builder(shape);
    }

    private static com.simibubi.create.AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(Block.box(x1, y1, z1, x2, y2, z2));
    }

}
