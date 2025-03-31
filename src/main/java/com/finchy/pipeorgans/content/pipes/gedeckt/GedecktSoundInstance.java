package com.finchy.pipeorgans.content.pipes.gedeckt;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GedecktSoundInstance extends GenericSoundInstance {

    public GedecktSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? GEDECKT_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? GEDECKT_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? GEDECKT_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? GEDECKT_LOW : GEDECKT_DEEP
        ).get());
    }
}
