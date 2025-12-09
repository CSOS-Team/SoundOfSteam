package com.finchy.pipeorgans.content.pipes.nasard;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class NasardSoundInstance extends GenericSoundInstance {

    public NasardSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> NASARD_SUPERHIGH;
                    case SMALL -> NASARD_HIGH;
                    case MEDIUM -> NASARD_MEDIUM;
                    case LARGE -> NASARD_LOW;
                    case HUGE -> NASARD_DEEP;
                }).get()
        );
    }
}
