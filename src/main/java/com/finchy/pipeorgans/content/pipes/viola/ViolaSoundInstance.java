package com.finchy.pipeorgans.content.pipes.viola;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class ViolaSoundInstance extends GenericSoundInstance {

    public ViolaSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? VIOLA_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? VIOLA_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? VIOLA_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? VIOLA_LOW : VIOLA_DEEP
        ).get());
    }
}
