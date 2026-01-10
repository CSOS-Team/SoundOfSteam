package com.finchy.pipeorgans.content.pipes.diapason;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class DiapasonSoundInstance extends GenericSoundInstance {

    public DiapasonSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> DIAPASON_SUPERHIGH;
                    case SMALL -> DIAPASON_HIGH;
                    case MEDIUM -> DIAPASON_MEDIUM;
                    case LARGE -> DIAPASON_LOW;
                    case HUGE -> DIAPASON_DEEP;
                }).get()
        );
    }
}
