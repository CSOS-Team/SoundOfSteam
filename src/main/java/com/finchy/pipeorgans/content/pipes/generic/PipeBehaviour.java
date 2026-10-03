package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public interface PipeBehaviour {

    Direction getExtensionDirection(BlockState pipeState); // the direction toward the extensions from the pipe (NOT the blockstate's facing property)
    Direction getPipeDirectionFromExtension(BlockState extensionState); // the direction toward the pipe from an extension
    // ^ note that the extensions are always placed with the same "facing" direction as the pipe, and the pipes always face the opposite direction to how they look

    double getExtensionClickPosition(BlockPos extensionPos, Vec3 clickLocation, Direction facing); // return the position clicked along the length of an extension
    // ^ used in extension onSneakWrenched()

    Direction getAttachedDirection(BlockState pipeState); // the direction toward the block the pipe is attached to from the pipe
    int extensionsPerBlock();

    GenericExtensionBlock<?> getExtensionBlock();

    default int blockLengthForPitch(int pitch) { // how many blocks long the pipe's extensions are, given a certain pitch
        return (int) Math.ceil(pitch / (float) extensionsPerBlock());
    }

    default double exactLengthForPitch(int pitch) {
        return pitch / (double) extensionsPerBlock();
    }

    default List<BlockPos> extensionPositions(BlockState pipeState, BlockPos pipePos, int pitch) { // returns a list of each extension's BlockPos, given a certain pitch
        Direction dir = getExtensionDirection(pipeState); // direction in which to iterate
        BlockPos currentPos = pipePos; // starting position
        int count = blockLengthForPitch(pitch); // how many blocks to check
        List<BlockPos> list = new ArrayList<>(count);
        for (int i=0; i<count; i++) {
            currentPos = currentPos.relative(dir);
            list.add(currentPos);
        }
        return list;
    }

    void incrementSize(Level pLevel, BlockPos pos, boolean playSound);

    default InteractionResult substitutePipe(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player) {
        GenericPipeBlock held = (GenericPipeBlock) ((GenericPipeBlockItem) heldItem.getItem()).getBlock();
        if (level.getBlockEntity(pos) instanceof GenericPipeBlockEntity be) {

            if (held.verifyReplacementSpace(state, level, pos, be.pitch)) { // if the new pipe has space to be placed
                clearOldExtensions(state, level, pos, be.pitch); // remove the old extensions
                placeNewPipe(state, level, pos, heldItem, player, be.pitch); // place the new pipe
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    default boolean verifyReplacementSpace(BlockState baseState, Level level, BlockPos basePos, int pitch) { // confirm that there is space for the new pipe
        if (pitch > 0) { // if there are actually any extensions to place
            for (BlockPos pos : extensionPositions(baseState, basePos, pitch)) {
                BlockState state = level.getBlockState(pos);
                if (state.canBeReplaced() || state.getBlock() instanceof GenericExtensionBlock<?>)
                    continue;
                return false; // something in the way
            }
        }
        return true;
    }

    default void clearOldExtensions(BlockState baseState, Level level, BlockPos basePos, int pitch) { // remove all the previous extensions
        if (pitch > 0) { // if there are actually any extensions to remove
            for (BlockPos pos : extensionPositions(baseState, basePos, pitch)) {
                level.destroyBlock(pos, false); // iterate through each extension and destroy it
            }
        }
    }

    void placeNewPipe(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, int pitch); // place the replacement pipe

}
