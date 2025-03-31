package com.finchy.pipeorgans.content.pipes.posaune;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PosauneSoundInstance extends GenericSoundInstance {

    public PosauneSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.SMALL ? POSAUNE_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? POSAUNE_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? POSAUNE_LOW : POSAUNE_DEEP
        ).get());
    }
}
