package com.finchy.pipeorgans.block.pipes.piccolo;

import com.finchy.pipeorgans.block.pipes.generic.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class PiccoloExtensionBlock extends QuadrupleExtensionBlock {
    public PiccoloExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.PICCOLO;
    }
}
