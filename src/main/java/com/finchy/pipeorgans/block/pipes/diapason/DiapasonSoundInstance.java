package com.finchy.pipeorgans.block.pipes.diapason;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class DiapasonSoundInstance extends GenericSoundInstance {

    public DiapasonSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? DIAPASON_SUPERHIGH :
                size == Generic.WhistleSize.SMALL ? DIAPASON_HIGH :
                size == Generic.WhistleSize.MEDIUM ? DIAPASON_MEDIUM :
                size == Generic.WhistleSize.LARGE ? DIAPASON_LOW : DIAPASON_DEEP
        ).get());
    }
}
