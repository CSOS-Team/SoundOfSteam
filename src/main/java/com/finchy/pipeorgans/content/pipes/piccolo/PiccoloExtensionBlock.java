package com.finchy.pipeorgans.content.pipes.piccolo;

import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class PiccoloExtensionBlock extends QuadrupleExtensionBlock {
    public PiccoloExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.PICCOLO;
    }
}
