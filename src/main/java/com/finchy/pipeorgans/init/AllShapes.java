package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.block.GedecktBlock;
import com.finchy.pipeorgans.block.GedecktExtensionBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.finchy.pipeorgans.block.GedecktExtensionBlock.SHAPE;
import static com.finchy.pipeorgans.block.GedecktExtensionBlock.SIZE;

public class AllShapes {

    public static VoxelShape add(VoxelShape a, VoxelShape b) {
        return Shapes.join(a, b, BooleanOp.OR);
    }

    // GEDECKT

    public static VoxelShape GEDECKT_SMALL_BASE = Shapes.box(0.3125, 0.1875, 0.3125, 0.6875, 1, 0.6875);
    public static VoxelShape GEDECKT_MEDIUM_BASE = Shapes.box(0.25, 0.1875, 0.25, 0.75, 1, 0.75);

    public static VoxelShape getGedecktExtensionShape(BlockState pBlockState) {

        GedecktExtensionBlock.GedecktExtensionShape blockShape = pBlockState.getValue(SHAPE);
        GedecktBlock.WhistleSize blockSize = pBlockState.getValue(SIZE);

        if (blockShape == GedecktExtensionBlock.GedecktExtensionShape.SINGLE) {
            if (blockSize == GedecktBlock.WhistleSize.SMALL) return GEDECKT_EXTENSION_SMALL_SINGLE;
            if (blockSize == GedecktBlock.WhistleSize.MEDIUM) return GEDECKT_EXTENSION_MEDIUM_SINGLE;
            if (blockSize == GedecktBlock.WhistleSize.LARGE) return Shapes.block();
            if (blockSize == GedecktBlock.WhistleSize.HUGE) return Shapes.block();
        }
        if (blockShape == GedecktExtensionBlock.GedecktExtensionShape.DOUBLE || blockShape == GedecktExtensionBlock.GedecktExtensionShape.DOUBLE_CONNECTED) {
            if (blockSize == GedecktBlock.WhistleSize.SMALL) return GEDECKT_EXTENSION_SMALL_DOUBLE;
            if (blockSize == GedecktBlock.WhistleSize.MEDIUM) return GEDECKT_EXTENSION_MEDIUM_DOUBLE;
            if (blockSize == GedecktBlock.WhistleSize.LARGE) return Shapes.block();
            if (blockSize == GedecktBlock.WhistleSize.HUGE) return Shapes.block();

        }
        return Shapes.block();
    }

    public static VoxelShape GEDECKT_EXTENSION_SMALL_SINGLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.6875);
    public static VoxelShape GEDECKT_EXTENSION_SMALL_DOUBLE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);

    public static VoxelShape GEDECKT_EXTENSION_MEDIUM_SINGLE = Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75);
    public static VoxelShape GEDECKT_EXTENSION_MEDIUM_DOUBLE = Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75);



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

    public static VoxelShape getWhistleBase(GedecktBlock.WhistleSize size) {
        return switch (size) {
            case SMALL -> GEDECKT_SMALL_BASE;
            case MEDIUM -> GEDECKT_MEDIUM_BASE;
            case LARGE -> Shapes.block();
            case HUGE -> Shapes.block();
        };
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

}
