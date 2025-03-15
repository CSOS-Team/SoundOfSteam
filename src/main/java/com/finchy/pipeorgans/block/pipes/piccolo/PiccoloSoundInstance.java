package com.finchy.pipeorgans.block.pipes.piccolo;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PiccoloSoundInstance extends GenericSoundInstance {

    public PiccoloSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? PICCOLO_SUPERHIGH :
                        size == Generic.WhistleSize.SMALL ? PICCOLO_HIGH :
                                size == Generic.WhistleSize.MEDIUM ? PICCOLO_MEDIUM :
                                        size == Generic.WhistleSize.LARGE ? PICCOLO_LOW : PICCOLO_DEEP
        ).get());
    }
}
