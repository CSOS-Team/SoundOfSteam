package com.finchy.pipeorgans.block.pipes.nasard;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class NasardSoundInstance extends GenericSoundInstance {

    public NasardSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? NASARD_SUPERHIGH :
                size == Generic.WhistleSize.SMALL ? NASARD_HIGH :
                size == Generic.WhistleSize.MEDIUM ? NASARD_MEDIUM :
                size == Generic.WhistleSize.LARGE ? NASARD_LOW : NASARD_DEEP
        ).get());
    }
}
