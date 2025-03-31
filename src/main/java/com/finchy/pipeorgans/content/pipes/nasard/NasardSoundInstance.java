package com.finchy.pipeorgans.content.pipes.nasard;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class NasardSoundInstance extends GenericSoundInstance {

    public NasardSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? NASARD_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? NASARD_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? NASARD_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? NASARD_LOW : NASARD_DEEP
        ).get());
    }
}
