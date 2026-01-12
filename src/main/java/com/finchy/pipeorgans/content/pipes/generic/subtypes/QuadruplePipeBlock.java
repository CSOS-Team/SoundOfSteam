package com.finchy.pipeorgans.content.pipes.generic.subtypes;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.PipeMaterial;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class QuadruplePipeBlock extends GenericPipeBlock {

    protected QuadruplePipeBlock(Properties pProperties, boolean supportsTrem, PipeMaterial material) {
        super(pProperties, supportsTrem, material, 4);
    }
    @Override
    public void incrementSize(Level pLevel, BlockPos pos, boolean playSound) {
        BlockState base = pLevel.getBlockState(pos);
        if (!base.hasProperty(SIZE))
            return;

        PipeSize size = base.getValue(SIZE);
        SoundType soundtype = base.getSoundType();
        BlockPos currentPos = pos.above();
        Direction facing = base.getValue(FACING);

        float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
        SoundEvent growSound = SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
        SoundEvent hitSound = soundtype.getHitSound();

        for (int i = 1; i <= 12; i+=4) {
            BlockState blockState = pLevel.getBlockState(currentPos);

            if (blockState.getBlock() instanceof QuadrupleExtensionBlock) { // if block above is extension

                if (blockState.getValue(QuadrupleExtensionBlock.SHAPE) != ExtensionShapes.Quadruple.QUAD
                        && blockState.getValue(QuadrupleExtensionBlock.SHAPE) != ExtensionShapes.Quadruple.QUAD_CONNECTED) { // if extension above is single, double, or triple

                    BlockState toSet = blockState.cycle(QuadrupleExtensionBlock.SHAPE); // cycle to the next shape
                    if (extensionBlock.get().isDirectional())      // only set direction if the extension is directional
                        toSet = toSet.setValue(FACING, facing);    // (would cause a crash otherwise)
                    pLevel.setBlock(currentPos, toSet, 3);

                    if (playSound) {
                        switch (blockState.getValue(QuadrupleExtensionBlock.SHAPE)) {
                            case SINGLE -> i++;
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
