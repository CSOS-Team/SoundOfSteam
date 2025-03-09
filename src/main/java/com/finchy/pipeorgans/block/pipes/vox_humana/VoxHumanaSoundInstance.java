package com.finchy.pipeorgans.block.pipes.vox_humana;

import com.finchy.pipeorgans.block.Generic;
import com.finchy.pipeorgans.block.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class VoxHumanaSoundInstance extends GenericSoundInstance {

    public VoxHumanaSoundInstance(Generic.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == Generic.WhistleSize.TINY ? VOX_HUMANA_SUPERHIGH :
                size == Generic.WhistleSize.SMALL ? VOX_HUMANA_HIGH :
                size == Generic.WhistleSize.MEDIUM ? VOX_HUMANA_MEDIUM :
                size == Generic.WhistleSize.LARGE ? VOX_HUMANA_LOW : VOX_HUMANA_DEEP
        ).get());
    }
}
