package com.finchy.pipeorgans.content.pipes.subbass;

import com.finchy.pipeorgans.content.pipes.generic.PedalExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class SubbassExtensionBlock extends PedalExtensionBlock {
    public SubbassExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.SUBBASS;
    }
}
