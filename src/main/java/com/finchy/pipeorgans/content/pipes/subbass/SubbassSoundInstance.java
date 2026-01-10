package com.finchy.pipeorgans.content.pipes.subbass;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class SubbassSoundInstance extends GenericSoundInstance {

    public SubbassSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> SUBBASS_SUPERHIGH;
                    case SMALL -> SUBBASS_HIGH;
                    case MEDIUM -> SUBBASS_MEDIUM;
                    case LARGE -> SUBBASS_LOW;
                    case HUGE -> SUBBASS_DEEP;
                }).get()
        );
    }
}
