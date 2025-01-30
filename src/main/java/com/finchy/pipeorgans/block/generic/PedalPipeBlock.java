package com.finchy.pipeorgans.block.generic;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.subbass.SubbassExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class PedalPipeBlock extends GenericPipeBlock {

    public PedalPipeBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, Generic.WhistleSize.LARGE));
    }

    // called when wrenched
    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState.getValue(SIZE) == Generic.WhistleSize.HUGE ?
                originalState.setValue(SIZE, Generic.WhistleSize.SMALL) : originalState.cycle(SIZE);
    }

    @Override
    // increase length of whistle
    public void incrementSize(LevelAccessor pLevel, BlockPos pPos) {
        BlockState base = pLevel.getBlockState(pPos);
        if (!base.hasProperty(SIZE))
            return;

        Generic.WhistleSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        BlockPos currentPos = pPos.above();

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 12; i+=1) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock() instanceof SubbassExtensionBlock) {
                currentPos = currentPos.above();
                continue;
            }

            // if pos is not extension (air)
            if (!blockState.canBeReplaced())
                return;

            pLevel.setBlock(currentPos, AllBlocks.SUBBASS_EXTENSION.get().defaultBlockState()
                    .setValue(SIZE, size), 3);
            if (soundtype != null) {
                float pPitch = (float) Math.pow(2, -i / 12.0);
                pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4f, pPitch);
                pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
            }
            return;
        }
    }
}
