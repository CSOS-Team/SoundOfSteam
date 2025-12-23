package com.finchy.pipeorgans.content.pipes.chamade;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class ChamadeSoundInstance extends GenericSoundInstance {

    public ChamadeSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> CHAMADE_SUPERHIGH;
                    case SMALL -> CHAMADE_HIGH;
                    case MEDIUM -> CHAMADE_MEDIUM;
                    case LARGE -> CHAMADE_LOW;
                    case HUGE -> CHAMADE_DEEP;
                }).get()
        );
    }
}
