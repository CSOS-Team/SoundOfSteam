package com.finchy.pipeorgans.content.pipes.viola;

import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;

public class ViolaBlock extends GenericPipeBlock {
    public ViolaBlock(Properties pProperties) {
        super(pProperties);
        baseBlock = AllBlocks.VIOLA;
        extensionBlock = AllBlocks.VIOLA_EXTENSION;
        blockEntity = AllBlockEntities.VIOLA_BLOCK_ENTITY;
    }
}
