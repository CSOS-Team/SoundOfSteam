package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

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
    };

    PipeDirection() {
    }

    public abstract Direction getExtensionDirection(BlockState pipeState); // the direction toward the extensions from the pipe (NOT the blockstate's facing property)

    public abstract Direction getPipeDirectionFromExtension(BlockState extensionState); // the direction toward the pipe from an extension
}
