package com.finchy.pipeorgans.block.pipes.posaune;

import com.finchy.pipeorgans.block.pipes.generic.PedalExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class PosauneExtensionBlock extends PedalExtensionBlock {
    public PosauneExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.POSAUNE;
    }
}
