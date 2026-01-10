package com.finchy.pipeorgans.content.pipes.posaune;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class PosauneSoundInstance extends GenericSoundInstance {

    public PosauneSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> POSAUNE_SUPERHIGH;
                    case SMALL -> POSAUNE_HIGH;
                    case MEDIUM -> POSAUNE_MEDIUM;
                    case LARGE -> POSAUNE_LOW;
                    case HUGE -> POSAUNE_DEEP;
                }).get()
        );
    }
}
