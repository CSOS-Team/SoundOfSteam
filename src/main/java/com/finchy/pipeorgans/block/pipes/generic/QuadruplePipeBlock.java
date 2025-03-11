package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.Generic;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class QuadruplePipeBlock extends GenericPipeBlock {

    public QuadruplePipeBlock(Properties pProperties) {
        super(pProperties);
        this.extensionsPerBlock = 4;
    }

    // increase length of whistle
    public void incrementSize(LevelAccessor pLevel, BlockPos pPos, boolean playSound) {
        BlockState base = pLevel.getBlockState(pPos);
        if (!base.hasProperty(SIZE))
            return;

        Generic.WhistleSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        BlockPos currentPos = pPos.above();

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 12; i+=4) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock() instanceof QuadrupleExtensionBlock) {

                // if block above is extension
                if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) != Generic.QuadrupleExtensionShape.QUAD
                        && blockState.getValue(QuadrupleExtensionBlock.SHAPE) != Generic.QuadrupleExtensionShape.QUAD_CONNECTED) {
                    // if extension is single, double, or triple
                    pLevel.setBlock(currentPos, blockState.cycle(QuadrupleExtensionBlock.SHAPE), 3);
                    if (playSound) {
                        switch (blockState.getValue(QuadrupleExtensionBlock.SHAPE)) {
                            case SINGLE -> i+=1;
                            case DOUBLE -> i+=2;
                            case TRIPLE -> i+=3;
                        }
                        float pPitch = (float) Math.pow(2, -i / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }
                    return;
                }
                currentPos = currentPos.above();
                continue;
            }

            // if block above is not extension (air)
            if (!blockState.canBeReplaced()) {
                return;
            }

            pLevel.setBlock(currentPos, this.extensionBlock.get().defaultBlockState()
                    .setValue(SIZE, size), 3);
            if (playSound) {
                float pPitch = (float) Math.pow(2, -i / 12.0);
                pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
            }
            return;
        }
    }

}
