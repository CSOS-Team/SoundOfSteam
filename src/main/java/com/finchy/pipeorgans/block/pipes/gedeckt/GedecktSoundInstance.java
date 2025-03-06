package com.finchy.pipeorgans.block.pipes.gedeckt;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GedecktSoundInstance extends GenericSoundInstance {

    public GedecktSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? GEDECKT_SUPERHIGH :
                size == Generic.WhistleSize.SMALL ? GEDECKT_HIGH :
                size == Generic.WhistleSize.MEDIUM ? GEDECKT_MEDIUM :
                size == Generic.WhistleSize.LARGE ? GEDECKT_LOW : GEDECKT_DEEP
        ).get());
    }
}
