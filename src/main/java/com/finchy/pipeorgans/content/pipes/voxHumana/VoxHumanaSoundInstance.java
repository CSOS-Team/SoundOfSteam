package com.finchy.pipeorgans.content.pipes.voxHumana;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class VoxHumanaSoundInstance extends GenericSoundInstance {

    public VoxHumanaSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> VOX_HUMANA_SUPERHIGH;
                    case SMALL -> VOX_HUMANA_HIGH;
                    case MEDIUM -> VOX_HUMANA_MEDIUM;
                    case LARGE -> VOX_HUMANA_LOW;
                    case HUGE -> VOX_HUMANA_DEEP;
                }).get()
        );
    }
}
