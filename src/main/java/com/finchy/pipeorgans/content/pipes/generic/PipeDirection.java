package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public enum PipeDirection {
    HORIZONTAL {
        @Override
        public Direction getExtensionDirection(BlockState pipeState) {
            return pipeState.getValue(GenericPipeBlock.FACING).getOpposite();
        }

        @Override
        public Direction getPipeDirectionFromExtension(BlockState extensionState) {
            return extensionState.getValue(GenericPipeBlock.FACING);
        }

        @Override
        public double getExtensionClickPosition(BlockPos extensionPos, Vec3 clickLocation, Direction facing) {
            return switch (facing) {
                case DOWN, UP -> -1.0; // if this happens, something has gone very very wrong
                case NORTH -> extensionPos.getZ() - clickLocation.z;
                case SOUTH -> clickLocation.z - extensionPos.getZ();
                case WEST -> extensionPos.getX() - clickLocation.x;
                case EAST -> clickLocation.x - extensionPos.getX();
            };
        }
    }
    , VERTICAL {
        @Override
        public Direction getExtensionDirection(BlockState pipeState) {
            return Direction.UP;
        }

        @Override
        public Direction getPipeDirectionFromExtension(BlockState extensionState) {
            return Direction.DOWN;
        }

        @Override
        public double getExtensionClickPosition(BlockPos extensionPos, Vec3 clickLocation, Direction facing) {
            return clickLocation.y - extensionPos.getY();
        }
    };

    PipeDirection() {
    }

    public abstract Direction getExtensionDirection(BlockState pipeState); // the direction toward the extensions from the pipe (NOT the blockstate's facing property)

    public abstract Direction getPipeDirectionFromExtension(BlockState extensionState); // the direction toward the pipe from an extension

    public abstract double getExtensionClickPosition(BlockPos extensionPos, Vec3 clickLocation, Direction facing); // return the position clicked along the length of an extension
    // ^ used in extension onSneakWrenched()
}
