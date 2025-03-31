package com.finchy.pipeorgans.content.pipes.trompette;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class TrompetteSoundInstance extends GenericSoundInstance {

    public TrompetteSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? TROMPETTE_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? TROMPETTE_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? TROMPETTE_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? TROMPETTE_LOW : TROMPETTE_DEEP
        ).get());
    }
}
