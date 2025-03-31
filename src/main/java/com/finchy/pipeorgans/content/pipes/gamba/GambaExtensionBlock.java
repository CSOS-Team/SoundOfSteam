package com.finchy.pipeorgans.content.pipes.gamba;

import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class GambaExtensionBlock extends GenericExtensionBlock {
    public GambaExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.GAMBA;
    }
}
