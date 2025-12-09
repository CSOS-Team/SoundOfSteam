package com.finchy.pipeorgans.content.pipes.trompette;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class TrompetteSoundInstance extends GenericSoundInstance {

    public TrompetteSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> TROMPETTE_SUPERHIGH;
                    case SMALL -> TROMPETTE_HIGH;
                    case MEDIUM -> TROMPETTE_MEDIUM;
                    case LARGE -> TROMPETTE_LOW;
                    case HUGE -> TROMPETTE_DEEP;
                }).get()
        );
    }
}
