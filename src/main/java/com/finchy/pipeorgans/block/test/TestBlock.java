package com.finchy.pipeorgans.block.test;

import com.finchy.pipeorgans.block.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class TestBlock extends GenericPipeBlock {
    public TestBlock(Properties pProperties) {
        super(pProperties);
        this.baseBlock = AllBlocks.TEST;
        this.extensionBlock = AllBlocks.TEST_EXTENSION;
        this.blockEntity = AllBlockEntities.TEST_BLOCK_ENTITY;
    }
}
