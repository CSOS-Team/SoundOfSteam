package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class HorizontalPipeBlock extends GenericPipeBlock {

    public HorizontalPipeBlock(Properties properties, PipeMaterial material) {
        super(properties, material, 2);
    }

    @Override
    public void incrementSize(Level level, BlockPos pos, boolean playSound) {
        BlockState base = level.getBlockState(pos);
        if (!base.hasProperty(SIZE))
            return;

        PipeSize size = base.getValue(SIZE);
        SoundType soundType = base.getSoundType();

        Direction facing = base.getValue(FACING);
        Direction towardPlayer = facing.getOpposite();
        BlockPos currentPos = pos.relative(towardPlayer);


        float volume = (soundType.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = this.getGrowSound();
        SoundEvent hitSound = soundType.getPlaceSound();

        for (int i = 1; i <= 12; i += 2) {
            BlockState stateAtPos = level.getBlockState(currentPos);

            // Existing extension
            if (stateAtPos.getBlock() instanceof HorizontalExtensionBlock) {
                if (stateAtPos.getValue(HorizontalExtensionBlock.SHAPE) == ExtensionShapes.Horizontal.SINGLE) {

                    BlockState toSet = stateAtPos.cycle(HorizontalExtensionBlock.SHAPE);
                    toSet = toSet.setValue(FACING, facing);
                    level.setBlock(currentPos, toSet, 3);

                    if (playSound) {
                        if (stateAtPos.getValue(HorizontalExtensionBlock.SHAPE) == ExtensionShapes.Horizontal.SINGLE)
                            i++;
                        float pitch = (float) Math.pow(2, -i / 12.0);
                        level.playSound(null, currentPos, growSound, SoundSource.BLOCKS, volume / 4f, pitch);
                        level.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, volume, pitch);
                    }
                    return;
                }

                currentPos = currentPos.relative(towardPlayer);
                continue;
            }

            // Blocked
            if (!stateAtPos.canBeReplaced())
                return;

            // Place new extension
            BlockState toSet = extensionBlock.get().defaultBlockState().setValue(SIZE, size).setValue(FACING, facing);
            level.setBlock(currentPos, toSet, 3);

            if (playSound) {
                float pitch = (float) Math.pow(2, -i / 12.0);
                level.playSound(null, currentPos, growSound, SoundSource.BLOCKS, volume / 4f, pitch);
                level.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, volume, pitch);
            }
            return;
        }
    }

    @Override
    public boolean validateReplacementSpace(BlockState state, Level level, BlockPos pos, int pitch) {
        if (pitch > 0) { // if there are actually any extensions to place

            // check space (pitch/EPB) in front of the base, rounded up
            Direction outFacing = state.getValue(FACING).getOpposite();
            int checkDist = (int) Math.ceil(pitch/(float)EPB);
            BlockPos currentPos = pos;
            for (int i=1; i<=checkDist; i++) {
                currentPos = currentPos.relative(outFacing);
                BlockState currentState = level.getBlockState(currentPos);
                if (currentState.canBeReplaced() || (currentState.getBlock() instanceof HorizontalExtensionBlock))
                    continue;
                return false; // something in the way
            }
        }
        return true;
    }

    @Override
    public void clearOldExtensions(BlockState state, Level level, BlockPos pos, int pitch) {
        Direction outFacing = state.getValue(FACING).getOpposite();
        int removeDistance = (int) Math.ceil(pitch/(float)EPB);
        BlockPos currentPos = pos;
        for (int i=1; i<=removeDistance; i++) {
            currentPos = currentPos.relative(outFacing);
            level.destroyBlock(currentPos, false);
        }
    }
}
