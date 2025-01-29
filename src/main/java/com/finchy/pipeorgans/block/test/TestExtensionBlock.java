package com.finchy.pipeorgans.block.test;

import com.finchy.pipeorgans.block.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.init.AllBlocks;

public class TestExtensionBlock extends GenericExtensionBlock {
    public TestExtensionBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.TEST;
    }
}
