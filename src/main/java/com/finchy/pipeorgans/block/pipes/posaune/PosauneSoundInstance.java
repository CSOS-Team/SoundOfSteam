package com.finchy.pipeorgans.block.pipes.posaune;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PosauneSoundInstance extends GenericSoundInstance {

    public PosauneSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.SMALL ? POSAUNE_HIGH :
                size == Generic.WhistleSize.MEDIUM ? POSAUNE_MEDIUM :
                size == Generic.WhistleSize.LARGE ? POSAUNE_LOW : POSAUNE_DEEP
        ).get());
    }
}
