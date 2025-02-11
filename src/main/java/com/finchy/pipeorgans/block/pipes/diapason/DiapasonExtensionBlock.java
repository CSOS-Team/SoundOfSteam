package com.finchy.pipeorgans.block.pipes.diapason;

import com.finchy.pipeorgans.block.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class DiapasonExtensionBlock extends GenericExtensionBlock {
    public DiapasonExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.DIAPASON;
    }
}
