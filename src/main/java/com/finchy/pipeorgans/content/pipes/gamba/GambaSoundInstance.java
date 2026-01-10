package com.finchy.pipeorgans.content.pipes.gamba;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GambaSoundInstance extends GenericSoundInstance {

    public GambaSoundInstance(PipeSize size, BlockPos worldPosition) {
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
