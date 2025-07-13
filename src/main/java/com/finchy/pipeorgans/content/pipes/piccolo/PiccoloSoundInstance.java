package com.finchy.pipeorgans.content.pipes.piccolo;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PiccoloSoundInstance extends GenericSoundInstance {

    public PiccoloSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> PICCOLO_SUPERHIGH;
                    case SMALL -> PICCOLO_HIGH;
                    case MEDIUM -> PICCOLO_MEDIUM;
                    case LARGE -> PICCOLO_LOW;
                    case HUGE -> PICCOLO_DEEP;
                }).get()
        );
    }
}
