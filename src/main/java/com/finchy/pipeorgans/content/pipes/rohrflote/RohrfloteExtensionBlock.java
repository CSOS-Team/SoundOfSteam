package com.finchy.pipeorgans.content.pipes.rohrflote;

import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class RohrfloteExtensionBlock extends GenericExtensionBlock {
    public RohrfloteExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.ROHRFLOTE;
    }
}
