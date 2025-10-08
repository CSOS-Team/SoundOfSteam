package com.finchy.pipeorgans.content.pipes.hohlflute;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class HohlfluteSoundInstance extends GenericSoundInstance {

    public HohlfluteSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> HOHLFLUTE_SUPERHIGH;
                    case SMALL -> HOHLFLUTE_HIGH;
                    case MEDIUM -> HOHLFLUTE_MEDIUM;
                    case LARGE -> HOHLFLUTE_LOW;
                    case HUGE -> HOHLFLUTE_DEEP;
                }).get()
        );
    }
}
