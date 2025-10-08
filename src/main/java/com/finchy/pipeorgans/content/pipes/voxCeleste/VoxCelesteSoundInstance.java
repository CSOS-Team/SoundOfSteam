package com.finchy.pipeorgans.content.pipes.voxCeleste;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class VoxCelesteSoundInstance extends GenericSoundInstance {

    public VoxCelesteSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> VOX_CELESTE_SUPERHIGH;
                    case SMALL -> VOX_CELESTE_HIGH;
                    case MEDIUM -> VOX_CELESTE_MEDIUM;
                    case LARGE -> VOX_CELESTE_LOW;
                    case HUGE -> VOX_CELESTE_DEEP;
                }).get()
        );
    }
}
