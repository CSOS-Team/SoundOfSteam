package com.finchy.pipeorgans.block.pipes.gamba;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GambaSoundInstance extends GenericSoundInstance {

    public GambaSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? GAMBA_SUPERHIGH :
                size == Generic.WhistleSize.SMALL ? GAMBA_HIGH :
                size == Generic.WhistleSize.MEDIUM ? GAMBA_MEDIUM :
                size == Generic.WhistleSize.LARGE ? GAMBA_LOW : GAMBA_DEEP
        ).get());
    }
}
