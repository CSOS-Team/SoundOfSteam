package com.finchy.pipeorgans.block.diapason;

import com.finchy.pipeorgans.block.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class DiapasonExtensionBlock extends GenericExtensionBlock {
    public DiapasonExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.DIAPASON;
    }
}
