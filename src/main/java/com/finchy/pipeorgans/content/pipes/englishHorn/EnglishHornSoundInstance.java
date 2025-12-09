package com.finchy.pipeorgans.content.pipes.englishHorn;

import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class EnglishHornSoundInstance extends GenericSoundInstance {

    public EnglishHornSoundInstance(EPipeSizes.PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> ENGLISH_HORN_SUPERHIGH;
                    case SMALL -> ENGLISH_HORN_HIGH;
                    case MEDIUM -> ENGLISH_HORN_MEDIUM;
                    case LARGE -> ENGLISH_HORN_LOW;
                    case HUGE -> ENGLISH_HORN_DEEP;
                }).get()
        );
    }
}
