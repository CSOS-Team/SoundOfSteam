package com.finchy.pipeorgans.content.pipes.haunted_whistle;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class HauntedWhistleSoundInstance extends GenericSoundInstance {

    public HauntedWhistleSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> HAUNTED_WHISTLE_SUPERHIGH;
                    case SMALL -> HAUNTED_WHISTLE_HIGH;
                    case MEDIUM -> HAUNTED_WHISTLE_MEDIUM;
                    case LARGE -> HAUNTED_WHISTLE_LOW;
                    case HUGE -> HAUNTED_WHISTLE_DEEP;
                }).get()
        );
    }
}
