package com.finchy.pipeorgans.content.pipes.diapason;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class DiapasonSoundInstance extends GenericSoundInstance {

    public DiapasonSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? DIAPASON_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? DIAPASON_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? DIAPASON_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? DIAPASON_LOW : DIAPASON_DEEP
        ).get());
    }
}
