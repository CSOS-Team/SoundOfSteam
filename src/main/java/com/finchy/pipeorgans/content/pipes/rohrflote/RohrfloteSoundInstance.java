package com.finchy.pipeorgans.content.pipes.rohrflote;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class RohrfloteSoundInstance extends GenericSoundInstance {

    public RohrfloteSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> ROHRFLOTE_SUPERHIGH;
                    case SMALL -> ROHRFLOTE_HIGH;
                    case MEDIUM -> ROHRFLOTE_MEDIUM;
                    case LARGE -> ROHRFLOTE_LOW;
                    case HUGE -> ROHRFLOTE_DEEP;
                }).get()
        );
    }
}
