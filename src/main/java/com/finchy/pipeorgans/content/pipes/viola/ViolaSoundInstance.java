package com.finchy.pipeorgans.content.pipes.viola;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class ViolaSoundInstance extends GenericSoundInstance {

    public ViolaSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> VIOLA_SUPERHIGH;
                    case SMALL -> VIOLA_HIGH;
                    case MEDIUM -> VIOLA_MEDIUM;
                    case LARGE -> VIOLA_LOW;
                    case HUGE -> VIOLA_DEEP;
                }).get()
        );
    }
}
