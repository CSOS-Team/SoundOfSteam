package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.EExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class HorizontalPipeBlock extends GenericPipeBlock {

    public HorizontalPipeBlock(Properties properties) {
        super(properties, 2);
    }

    @Override
    public void incrementSize(Level level, BlockPos pos, boolean playSound) {
        BlockState base = level.getBlockState(pos);
        if (!base.hasProperty(SIZE))
            return;

        EPipeSizes.PipeSize size = base.getValue(SIZE);
        SoundType soundType = base.getSoundType();

        Direction facing = base.getValue(FACING);
        Direction towardPlayer = facing.getOpposite();
        BlockPos currentPos = pos.relative(towardPlayer);


        float volume = (soundType.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
        SoundEvent hitSound = soundType.getHitSound();

        for (int i = 1; i <= 12; i += 2) {
            BlockState stateAtPos = level.getBlockState(currentPos);

            // Existing extension
            if (stateAtPos.getBlock() instanceof HorizontalExtensionBlock) {
                if (stateAtPos.getValue(HorizontalExtensionBlock.SHAPE) == EExtensionShapes.HorizontalShape.SINGLE) {

                    BlockState toSet = stateAtPos.cycle(HorizontalExtensionBlock.SHAPE);
                    toSet = toSet.setValue(FACING, facing);
                    level.setBlock(currentPos, toSet, 3);

                    if (playSound) {
                        if (stateAtPos.getValue(HorizontalExtensionBlock.SHAPE) == EExtensionShapes.HorizontalShape.SINGLE)
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
}
