package com.finchy.pipeorgans.content.pipes.viola;

import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class ViolaExtensionBlock extends GenericExtensionBlock {
    public ViolaExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.VIOLA;
    }
}
