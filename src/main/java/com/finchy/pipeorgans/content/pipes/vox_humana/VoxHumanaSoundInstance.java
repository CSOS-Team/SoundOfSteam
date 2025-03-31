package com.finchy.pipeorgans.content.pipes.vox_humana;

import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericSoundInstance;
import net.minecraft.core.BlockPos;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class VoxHumanaSoundInstance extends GenericSoundInstance {

    public VoxHumanaSoundInstance(GenericWhistleProperties.WhistleSize size, BlockPos worldPosition) {
        super(size, worldPosition, (
                size == GenericWhistleProperties.WhistleSize.TINY ? VOX_HUMANA_SUPERHIGH :
                size == GenericWhistleProperties.WhistleSize.SMALL ? VOX_HUMANA_HIGH :
                size == GenericWhistleProperties.WhistleSize.MEDIUM ? VOX_HUMANA_MEDIUM :
                size == GenericWhistleProperties.WhistleSize.LARGE ? VOX_HUMANA_LOW : VOX_HUMANA_DEEP
        ).get());
    }
}
