package com.finchy.pipeorgans.content.pipes.prestant;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PrestantSoundInstance extends GenericSoundInstance {

    public PrestantSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> PRESTANT_SUPERHIGH;
                    case SMALL -> PRESTANT_HIGH;
                    case MEDIUM -> PRESTANT_MEDIUM;
                    case LARGE -> PRESTANT_LOW;
                    case HUGE -> PRESTANT_DEEP;
                }).get()
        );
    }
}
