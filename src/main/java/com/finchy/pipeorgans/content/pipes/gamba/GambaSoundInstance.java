package com.finchy.pipeorgans.content.pipes.gamba;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GambaSoundInstance extends GenericSoundInstance {

    public GambaSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> GAMBA_SUPERHIGH;
                    case SMALL -> GAMBA_HIGH;
                    case MEDIUM -> GAMBA_MEDIUM;
                    case LARGE -> GAMBA_LOW;
                    case HUGE -> GAMBA_DEEP;
                }).get()
        );
    }
}
