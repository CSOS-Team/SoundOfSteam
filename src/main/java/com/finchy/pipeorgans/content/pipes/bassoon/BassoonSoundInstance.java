package com.finchy.pipeorgans.content.pipes.bassoon;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class BassoonSoundInstance extends GenericSoundInstance {

    public BassoonSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> BASSOON_SUPERHIGH;
                    case SMALL -> BASSOON_HIGH;
                    case MEDIUM -> BASSOON_MEDIUM;
                    case LARGE -> BASSOON_LOW;
                    case HUGE -> BASSOON_DEEP;
                }).get()
        );
    }
}
