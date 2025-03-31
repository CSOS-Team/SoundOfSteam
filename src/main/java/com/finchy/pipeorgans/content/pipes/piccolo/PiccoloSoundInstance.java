package com.finchy.pipeorgans.content.pipes.piccolo;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PiccoloSoundInstance extends GenericSoundInstance {

    public PiccoloSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? PICCOLO_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? PICCOLO_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? PICCOLO_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? PICCOLO_LOW : PICCOLO_DEEP
        ).get());
    }
}
