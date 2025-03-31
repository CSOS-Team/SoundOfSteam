package com.finchy.pipeorgans.content.pipes.subbass;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class SubbassSoundInstance extends GenericSoundInstance {

    public SubbassSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.SMALL ? SUBBASS_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? SUBBASS_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? SUBBASS_LOW : SUBBASS_DEEP
        ).get());
    }
}
