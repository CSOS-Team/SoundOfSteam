package com.finchy.pipeorgans.content.pipes.gedeckt;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class GedecktSoundInstance extends GenericSoundInstance {

    public GedecktSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> GEDECKT_SUPERHIGH;
                    case SMALL -> GEDECKT_HIGH;
                    case MEDIUM -> GEDECKT_MEDIUM;
                    case LARGE -> GEDECKT_LOW;
                    case HUGE -> GEDECKT_DEEP;
                }).get()
        );
    }
}
