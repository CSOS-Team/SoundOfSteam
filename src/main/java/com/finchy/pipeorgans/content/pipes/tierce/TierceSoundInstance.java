package com.finchy.pipeorgans.content.pipes.tierce;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class TierceSoundInstance extends GenericSoundInstance {

    public TierceSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> TIERCE_SUPERHIGH;
                    case SMALL -> TIERCE_HIGH;
                    case MEDIUM -> TIERCE_MEDIUM;
                    case LARGE -> TIERCE_LOW;
                    case HUGE -> TIERCE_DEEP;
                }).get()
        );
    }
}
