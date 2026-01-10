package com.finchy.pipeorgans.content.pipes.openWood;

import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class OpenWoodSoundInstance extends GenericSoundInstance {

    public OpenWoodSoundInstance(PipeSize size, BlockPos worldPosition) {
        super(size, worldPosition,
                (switch (size) {
                    case TINY -> OPEN_WOOD_SUPERHIGH;
                    case SMALL -> OPEN_WOOD_HIGH;
                    case MEDIUM -> OPEN_WOOD_MEDIUM;
                    case LARGE -> OPEN_WOOD_LOW;
                    case HUGE -> OPEN_WOOD_DEEP;
                }).get()
        );
    }
}
