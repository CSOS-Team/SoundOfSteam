package com.finchy.pipeorgans.block.pipes.gamba;

import com.finchy.pipeorgans.block.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class GambaExtensionBlock extends GenericExtensionBlock {
    public GambaExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.GAMBA;
    }
}
