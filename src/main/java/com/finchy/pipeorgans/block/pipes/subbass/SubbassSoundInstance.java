package com.finchy.pipeorgans.block.pipes.subbass;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class SubbassSoundInstance extends GenericSoundInstance {

    public SubbassSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.SMALL ? SUBBASS_HIGH :
                size == Generic.WhistleSize.MEDIUM ? SUBBASS_MEDIUM :
                size == Generic.WhistleSize.LARGE ? SUBBASS_LOW : SUBBASS_DEEP
        ).get());
    }
}
