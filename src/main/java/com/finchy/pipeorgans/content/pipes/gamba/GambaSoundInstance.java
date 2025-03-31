package com.finchy.pipeorgans.content.pipes.gamba;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GambaSoundInstance extends GenericSoundInstance {

    public GambaSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? GAMBA_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? GAMBA_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? GAMBA_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? GAMBA_LOW : GAMBA_DEEP
        ).get());
    }
}
