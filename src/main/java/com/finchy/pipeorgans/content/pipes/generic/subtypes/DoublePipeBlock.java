package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.EExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class DoublePipeBlock extends GenericPipeBlock {

    protected DoublePipeBlock(Properties pProperties, boolean supportsTrem) {
        super(pProperties, supportsTrem, 2);
    }

    @Override
    public void incrementSize(Level pLevel, BlockPos pos, boolean playSound) {
        BlockState base = pLevel.getBlockState(pos);
        if (!base.hasProperty(SIZE))
            return;

        EPipeSizes.PipeSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        BlockPos currentPos = pos.above();
        Direction facing = base.getValue(FACING);

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 12; i+=2) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock() instanceof DoubleExtensionBlock) {
                if (blockState.getValue(DoubleExtensionBlock.SHAPE) == EExtensionShapes.DoubleShape.SINGLE) {

                    BlockState toSet = blockState.cycle(DoubleExtensionBlock.SHAPE); // cycle to the next shape
                    if (extensionBlock.get().isDirectional())      // only set direction if the extension is directional
                        toSet = toSet.setValue(FACING, facing);    // (would cause a crash otherwise)
                    pLevel.setBlock(currentPos, toSet, 3);

                    if (playSound) {
                        if (blockState.getValue(DoubleExtensionBlock.SHAPE) == EExtensionShapes.DoubleShape.SINGLE)
                            i++;
                        float pPitch = (float) Math.pow(2, -i / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }

                    return;
                }
                currentPos = currentPos.above();
                continue;
            }
            if (!blockState.canBeReplaced()) {
                return;
            }

            BlockState toSet = extensionBlock.get().defaultBlockState().setValue(SIZE, size);
            if (extensionBlock.get().isDirectional())      // only set direction if the extension is directional
                toSet = toSet.setValue(FACING, facing);    // (would cause a crash otherwise)
            pLevel.setBlock(currentPos, toSet, 3);

            if (playSound) {
                float pPitch = (float) Math.pow(2, -i / 12.0);
                pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
            }
            return;
        }
    }
}
