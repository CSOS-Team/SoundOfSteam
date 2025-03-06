package com.finchy.pipeorgans.block.pipes.trompette;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class TrompetteSoundInstance extends GenericSoundInstance {

    public TrompetteSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? TROMPETTE_SUPERHIGH :
                size == Generic.WhistleSize.SMALL ? TROMPETTE_HIGH :
                size == Generic.WhistleSize.MEDIUM ? TROMPETTE_MEDIUM :
                size == Generic.WhistleSize.LARGE ? TROMPETTE_LOW : TROMPETTE_DEEP
        ).get());
    }
}
